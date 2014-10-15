/*
 * Based on com.github.rjeschke.txtmark.Emitter,
 * copyright (C) 2011 René Jeschke <rene_jeschke@yahoo.de>
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

/**
 * Emitter class responsible for generating generic markdown output.
 * 
 */
public class Emitter
{
    /** Link references. */
    public final HashMap<String, LinkRef> linkRefs = new HashMap<String, LinkRef>();

    /** The configuration. */
    //private final Configuration config;

    /** Constructor. */
    public Emitter()  {
    }
    /*
    public Emitter(final Configuration config)  {
        this.config = config;
	}*/

    /**
     * Adds a LinkRef to this set of LinkRefs.
     * 
     * @param key
     *            The key/id.
     * @param linkRef
     *            The LinkRef.
     */
    public void addLinkRef(final String key, final LinkRef linkRef)
    {
        this.linkRefs.put(key.toLowerCase(), linkRef);
    }

    /**
     * Transforms the given block recursively into HTML.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param root
     *            The Block to process.
     */
    //public void emit(final StringBuilder out, final Block root) {
    public void emit() {
    }


    /**
     * Finds the position of the given Token in the given String.
     * 
     * @param in
     *            The String to search on.
     * @param start
     *            The starting character position.
     * @param token
     *            The token to find.
     * @return The position of the token or -1 if none could be found.
     */
    private int findToken(final String in, int start, MarkToken token)
    {
        int pos = start;
        while(pos < in.length())
        {
            if(this.getToken(in, pos) == token)
                return pos;
            pos++;
        }
        return -1;
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
    private int checkLink(final StringBuilder out, final String in, int start, MarkToken token)  {
        boolean isAbbrev = false;
        int pos = start + (token == MarkToken.LINK ? 1 : 2);
        final StringBuilder temp = new StringBuilder();

        temp.setLength(0);
        pos = Utils.readMdLinkId(temp, in, pos);
        if (pos < start) {
            return -1;
	}
        String name = temp.toString(), link = null, comment = null;
        final int oldPos = pos++;
        pos = Utils.skipSpaces(in, pos);
	// skip ahead to content:
        if (pos < start) {
            final LinkRef lr = this.linkRefs.get(name.toLowerCase());
            if(lr != null) {
                isAbbrev = lr.isAbbrev;
                link = lr.link;
                comment = lr.title;
                pos = oldPos;
            }  else   {
                return -1;
            }


	    // check for explicit URL
        } else if (in.charAt(pos) == '(')   {
            pos++;
            pos = Utils.skipSpaces(in, pos);
            if (pos < start) {
                return -1;
	    }
            temp.setLength(0);
            boolean useLt = in.charAt(pos) == '<';
            pos = useLt ? Utils.readUntil(temp, in, pos + 1, '>') : Utils.readMdLink(temp, in, pos);
            if(pos < start)
                return -1;
            if(useLt)
                pos++;
            link = temp.toString();

            if(in.charAt(pos) == ' ') {
                pos = Utils.skipSpaces(in, pos);
                if(pos > start && in.charAt(pos) == '"') {
                    pos++;
                    temp.setLength(0);
                    pos = Utils.readUntil(temp, in, pos, '"');
                    if(pos < start) {
                        return -1;
		    }
                    comment = temp.toString();
                    pos++;
                    pos = Utils.skipSpaces(in, pos);
                    if (pos == -1) {
                        return -1;
		    }
		}
	    }
	    if (in.charAt(pos) != ')') {
                return -1;
	    }



	    // check for reference link
        } else if(in.charAt(pos) == '[')  {
            pos++;
            temp.setLength(0);
            pos = Utils.readRawUntil(temp, in, pos, ']');
            if(pos < start)
                return -1;
            final String id = temp.length() > 0 ? temp.toString() : name;
            final LinkRef lr = this.linkRefs.get(id.toLowerCase());
            if(lr != null)  {
                link = lr.link;
                comment = lr.title;
            }
        } else   {
            final LinkRef lr = this.linkRefs.get(name.toLowerCase());
            if(lr != null)
            {
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
                //if(!this.useExtensions)
		//  return -1;
		/*
                out.append("<abbr title=\"");
                Utils.appendValue(out, comment, 0, comment.length());
                out.append("\">");
                this.recursiveEmitLine(out, name, 0, MarkToken.NONE);
                out.append("</abbr>");
		*/


            } else  {
		/*
                this.config.decorator.openLink(out);
                out.append(" href=\"");
                Utils.appendValue(out, link, 0, link.length());
                out.append('"');
                if(comment != null) {
                    out.append(" title=\"");
                    Utils.appendValue(out, comment, 0, comment.length());
                    out.append('"');
                }
                out.append('>');
                this.recursiveEmitLine(out, name, 0, MarkToken.NONE);
                this.config.decorator.closeLink(out);
		*/
	    }

        } else   {
	    // if not a link, then an iimage
	    /*
            this.config.decorator.openImage(out);
            out.append(" src=\"");
            Utils.appendValue(out, link, 0, link.length());
            out.append("\" alt=\"");
            Utils.appendValue(out, name, 0, name.length());
            out.append('"');
            if(comment != null)
            {
                out.append(" title=\"");
                Utils.appendValue(out, comment, 0, comment.length());
                out.append('"');
            }
            this.config.decorator.closeImage(out);
	    */
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
    private int recursiveEmitLine(final StringBuilder out, final String in, int start, MarkToken token)
    {
        int pos = start, a, b;
        final StringBuilder temp = new StringBuilder();
        while(pos < in.length())
        {
            final MarkToken mt = this.getToken(in, pos);
            if (token != MarkToken.NONE)
                return pos;

	    if ((mt == MarkToken.IMAGE) || (mt == MarkToken.LINK)) {
		temp.setLength(0);
                b = this.checkLink(temp, in, pos, mt);
                if(b > 0)  {
                    out.append(temp);
                    pos = b;
                } else {
                    out.append(in.charAt(pos));
                }

	    } else {
                out.append(in.charAt(pos));
	    }
            pos++;
        }
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
    public MarkToken getToken(final String in, final int pos)
    {
        final char c0 = pos > 0 ? whitespaceToSpace(in.charAt(pos - 1)) : ' ';
        final char c = whitespaceToSpace(in.charAt(pos));
        final char c1 = pos + 1 < in.length() ? whitespaceToSpace(in.charAt(pos + 1)) : ' ';
        final char c2 = pos + 2 < in.length() ? whitespaceToSpace(in.charAt(pos + 2)) : ' ';
        final char c3 = pos + 3 < in.length() ? whitespaceToSpace(in.charAt(pos + 3)) : ' ';

        switch(c)  {
        case '{':
	    return MarkToken.CITE;
        case '!':
            if(c1 == '[') {
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
}