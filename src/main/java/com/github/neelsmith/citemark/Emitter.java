/*
 * Based on com.github.rjeschke.txtmark.Emitter,
 * copyright (c) 2011 René Jeschke <rene_jeschke@yahoo.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.neelsmith.citemark;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.StringReader;


/**
 * Emitter class responsible for generating generic markdown output.
 * 
 */
public class Emitter {

    /** Link references. */
    public final HashMap<String, LinkRef> linkRefs = new HashMap<String, LinkRef>();


    /** CITE service installations to use. */
    public final HashMap<String, Service> services = new HashMap<String, Service>();


    /** Constructor. */
    public Emitter()  {
    }


    /** Add a CTS to the map of service installations.
     * @param baseUrn The base URN of the service.
     */
    public void addCts(String baseUrn) {
	String getPassagePlus = baseUrn + "?request=GetPassagePlus&urn=";
	Service cts = new Service("cts", getPassagePlus, getPassagePlus);
	this.services.put("cts", cts);
    }



    /** Add a CITE Collection Service to the map of service installations.
     * @param baseUrn The base URN of the service.
     */
    public void addCollectionService(String baseUrn) {
	String getObjectPlus = baseUrn + "?request=GetObjectPlus&urn=";
	Service cite = new Service("cite", getObjectPlus, getObjectPlus);
	this.services.put("cite", cite);
    }


    /** Add a CITE Image Extension to the map of service installations.
     * @param baseUrn The base URN of the service.
     */
    public void addImageExtension(String baseUrn) {
	String getImagePlus = baseUrn + "?request=GetImagePlus&urn=";
	String getBinaryImage = baseUrn + "?request=GetBinaryImage&urn=";
	Service image = new Service("image", getImagePlus, getImagePlus);
	this.services.put("image", image);
    }

    /**
     * Adds a LinkRef to this set of LinkRefs, using
     * all lower-case for the map key.
     * 
     * @param key  The map key.
     * @param linkRef    The LinkRef to add.
     */
    public void addLinkRef(final String key, final LinkRef linkRef)  {
        this.linkRefs.put(key.toLowerCase(), linkRef);
    }

    /** Empty constructor. */
    public void emit() {
    }








    public void indexLine(String line) {
	if (line.length() > 0) {
	    System.err.println ("Check  for link reff: " + line);
	    // check for up to 3 spaces?
	    if (line.charAt(0) == '[') {
		StringBuilder linkId = new StringBuilder();
		StringBuilder urn = new StringBuilder();
		StringBuilder caption = new StringBuilder();
		// read until ']'
		int idx = Utils.readUntil(linkId, line, 1, ']') ;
		System.err.println("Found linkId: " + linkId);
		idx++;
		if (line.charAt(idx) == ':') {
		    idx++;
		    while ((line.charAt(idx) == ' ') && (idx < line.length())) {
			idx++;
		    }
		    idx = Utils.readUntil(urn,line,idx,' ','\n');
		}
		// consume space, with quote:
		while ((line.charAt(idx) == ' ') || (line.charAt(idx) == '"') || (line.charAt(idx) == '\n')) {
		    idx++;
		}
		idx = Utils.readUntil(caption,line,idx,'"','\n');

		System.err.println ("Linkid: " + linkId + ", urn " + urn + ", caption " + caption);

		final LinkRef lr = new LinkRef(urn.toString(), caption.toString(), false);
		this.addLinkRef(linkId.toString(), lr);
	    }
	}
    }



    /** Scans all of txt for link definitions, including for
     * multiline link definitions, and passes them to
     * indexLine method for processing.
     *
     * @param txt The citedown text to scan for link
     * definitions.
     *
     */
    public void indexLinkReff(String txt) 
	throws Exception {
	System.err.println ("Indexing link reff in " + txt);
	// accumulate txt by line, and check
	// for link def patterns
	int pos = 0;
	StringBuilder line = new StringBuilder();
	boolean multiLine = false;
	while (pos < txt.length()) {
	    final char ch = txt.charAt(pos);
	    // check for CRLF as well...
	    //if ((ch == '\n') || (ch == '\r')) {
	    if ((ch == '\n') ) {
		int blanks = 0;
		boolean inContent = false;
		//System.err.println ("Found line break..");
		while (! inContent) {
		    pos++;
		    char nxtCh = txt.charAt(pos);
		    //		    if ((nxtCh == '\n') || (nxtCh == '\r')) {
		    if ((nxtCh == '\n') ) {
			blanks++;
		    } else {
			inContent = true;
		    }
		}
		//System.err.println ("Total blank lines: " + blanks + " at |" + line.toString() + "|" + " (mulit? " + multiLine + ")") ;

		if (blanks > 0) {
		    //System.err.println("Index single line: " + line.toString());
		    indexLine(line.toString());
		    line.setLength(0);

		} else {
		    if (multiLine == true) {
			//System.err.println ("Index multiline: " + line.toString());
			indexLine(line.toString());
			line.setLength(0);
		    }
		    multiLine = true;
		}

	    } else {
		line.append(ch);
		pos++;
	    }

	}
    }




    /**
     */
    public String getLinkReference(final String in, int start)  {
        boolean isAbbrev = false;
        int pos = start ;
        final StringBuilder temp = new StringBuilder();
        temp.setLength(0);
        pos = Utils.readMdLinkId(temp, in, pos);
        if (pos < start) {
	    System.err.println("Could not read link from " + in + " from index " + start);
            return "";
	}
	// The three components of a link:
        String name = temp.toString();
	return (name);
    }


    /**
     * Checks if there is a valid markdown link definition.
     * 
     * @param out
     *            The StringBuilder containing the generated output.
     * @param in
     *            Input String.
     * @param start
     *            Starting position.
     * @param token
     *            Either LINK or IMAGE.
     * @return The new position or -1 if there is no valid markdown link.
     */
    public int checkLink(final StringBuilder out, final String in, int start, MarkToken token)  {
        boolean isAbbrev = false;
        int pos = start + (token == MarkToken.LINK ? 1 : 2);
        final StringBuilder temp = new StringBuilder();

        temp.setLength(0);
        pos = Utils.readMdLinkId(temp, in, pos);
        if (pos < start) {
            return -1;
	}
	// The three components of a link:
        String name = temp.toString();
	String link = null; 
	String comment = null;


        final int oldPos = pos++;
        pos = Utils.skipSpaces(in, pos);
	// skip ahead to content:
        if (pos < start) {
            final LinkRef lr = this.linkRefs.get(name.toLowerCase());
            if (lr != null) {
                isAbbrev = lr.isAbbrev;
                link = lr.link;
                comment = lr.title;
                pos = oldPos;
            }  else   {
                return -1;
            }


        } else if(in.charAt(pos) == '[')  {
            pos++;
            temp.setLength(0);
            pos = Utils.readRawUntil(temp, in, pos, ']');
            if(pos < start)
                return -1;
            final String id = temp.length() > 0 ? temp.toString() : name;
            final LinkRef lr = this.linkRefs.get(id.toLowerCase());
            if (lr != null)  {
                link = lr.link;
                comment = lr.title;
            }
        } else   {
            final LinkRef lr = this.linkRefs.get(name.toLowerCase());
            if (lr != null)   {
                isAbbrev = lr.isAbbrev;
                link = lr.link;
                comment = lr.title;
                pos = oldPos;
            }
            else
            {
                return -1;
            }
        }

        if (link == null) {
            return -1;
	}



	// PROCESS LINK:
        if (token == MarkToken.LINK)  {
            if (isAbbrev && comment != null)      {

            } else  {
	    }

        } else   {
        }
        return pos;
    }


    /**
     * Recursively scans through the given line, taking care of any markdown
     * stuff.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            Input String.
     * @param start
     *            Start position.
     * @param token
     *            The matching Token (for e.g. '*')
     * @return The position of the matching Token or -1 if token was NONE or no
     *         Token could be found.
     */
    public int recursiveEmitLine(final StringBuilder out, final String in, int start, MarkToken token) {
        int pos = start, idx;
        final StringBuilder temp = new StringBuilder();
        while(pos < in.length()) {
            final MarkToken mt = this.getToken(in, pos);
            if (token != MarkToken.NONE) {
		//System.err.println ("At char " + pos + ", returinging with buildder at " + out.toString());
                return pos;
	    }
	    //	    if ((mt == MarkToken.IMAGE) || (mt == MarkToken.LINK)) {
	    if (mt == MarkToken.LINK) {
		temp.setLength(0);
                idx = this.checkLink(temp, in, pos, mt);
                if (idx > 0)  {
                    out.append(temp);
                    pos = idx;
                } else {
                    out.append(in.charAt(pos));
                }

	    }  else if (mt == MarkToken.QUOTE) {
		// distinguish IMAGE from QUOTE
		if (in.charAt(pos + 1)  == '{') {
		    out.append("[");
		    pos = pos + 1;
		} else if (in.charAt(pos + 1) == '[') {
		    out.append("![");
		    pos = pos + 1;
		} else {
		    // ???
		}

		int nxt = Utils.readUntil(out, in, pos + 1, '}' );
		//System.err.println("Found '}' at " + nxt);
		out.append("]");
		pos = nxt;

	    } else {
		//System.err.println ("At char " + pos + ", handle token " + mt.toString());
                out.append(in.charAt(pos));
	    }
            pos++;
        }
	//System.err.println ("After whole string, returinging with buildder at " + out.toString());
        return -1;
    }

    /**
     * Turns every whitespace character into a space character.
     * 
     * @param c
     *            Character to check
     * @return 32 is c was a whitespace, c otherwise
     */
    private static char whitespaceToSpace(char c)
    {
        return Character.isWhitespace(c) ? ' ' : c;
    }

    /**
     * Check if there is any markdown Token.
     * 
     * @param in
     *            Input String.
     * @param pos
     *            Starting position.
     * @return The Token.
     */
    public MarkToken getToken(final String in, final int pos)  {
        final char c0 = pos > 0 ? whitespaceToSpace(in.charAt(pos - 1)) : ' ';
        final char c = whitespaceToSpace(in.charAt(pos));
        final char c1 = pos + 1 < in.length() ? whitespaceToSpace(in.charAt(pos + 1)) : ' ';
        final char c2 = pos + 2 < in.length() ? whitespaceToSpace(in.charAt(pos + 2)) : ' ';
        final char c3 = pos + 3 < in.length() ? whitespaceToSpace(in.charAt(pos + 3)) : ' ';

        switch(c)  {
        case '{':
	    return MarkToken.CITE;
        case '!':
            if (c1 == '[') {
		return MarkToken.NONE;
	    } else if (c1 == '{') {
		return MarkToken.QUOTE;
	    }
        case '[':
            return MarkToken.LINK;
        case ']':
            return MarkToken.NONE;
        default:
            return MarkToken.NONE;
        }
    }

    /**
     * Writes a set of markdown lines into the StringBuilder.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param lines
     *            The lines to write.
     */
    /*
    private void emitMarkedLines(final StringBuilder out, final Line lines)
    {
        final StringBuilder in = new StringBuilder();
        Line line = lines;
        while(line != null)
        {
            if(!line.isEmpty)
            {
                in.append(line.value.substring(line.leading, line.value.length() - line.trailing));
                if(line.trailing >= 2)
                    in.append("<br />");
            }
            if(line.next != null)
                in.append('\n');
            line = line.next;
        }

        this.recursiveEmitLine(out, in.toString(), 0, MarkToken.NONE);
    }
    */
    /**
     * Writes a set of raw lines into the StringBuilder.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param lines
     *            The lines to write.
     */
    /*
    private void emitRawLines(final StringBuilder out, final Line lines)
    {
        Line line = lines;
        if(this.config.safeMode)
        {
            final StringBuilder temp = new StringBuilder();
            while(line != null)
            {
                if(!line.isEmpty)
                {
                    temp.append(line.value);
                }
                temp.append('\n');
                line = line.next;
            }
            final String in = temp.toString();
            for(int pos = 0; pos < in.length(); pos++)
            {
                    out.append(in.charAt(pos));
            }
        }
        else
        {
            while(line != null)
            {
                if(!line.isEmpty)
                {
                    out.append(line.value);
                }
                out.append('\n');
                line = line.next;
            }
        }
	}*/



    /**
     * Finds the position of the given Token in the given String.
     * 
     * @param in  The String to search.
     * @param start The starting character position.
     * @param token The token to find.
     * @return The position of the token or -1 if none could be found.
     */
    public int findToken(final String in, int start, MarkToken token)  {
        int pos = start;
        while (pos < in.length()) {
            if (this.getToken(in, pos) == token) {
                return pos;
	    }
            pos++;
        }
        return -1;
    }

}
