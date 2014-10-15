/* Based on com.github.rjeschke.txtmark.Utils
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

/**
 * Utilities.
 * 
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
class Utils
{
    /** Random number generator value. */
    private static int RND = (int)System.nanoTime();

    /**
     * LCG random number generator.
     * 
     * @return A pseudo random number between 0 and 1023
     */
    public final static int rnd()
    {
        return (RND = RND * 1664525 + 1013904223) >>> 22;
    }

    /**
     * Skips spaces in the given String.
     * 
     * @param in
     *            Input String.
     * @param start
     *            Starting position.
     * @return The new position or -1 if EOL has been reached.
     */
    public final static int skipSpaces(final String in, final int start)
    {
        int pos = start;
        while (pos < in.length() && (in.charAt(pos) == ' ' || in.charAt(pos) == '\n'))
            pos++;
        return pos < in.length() ? pos : -1;
    }


    /**
     * Reads characters until any 'end' character is encountered.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            The Input String.
     * @param start
     *            Starting position.
     * @param end
     *            End characters.
     * @return The new position or -1 if no 'end' char was found.
     */
    public final static int readUntil(final StringBuilder out, final String in, final int start, final char... end)
    {
        int pos = start;
        while (pos < in.length()) {
            final char ch = in.charAt(pos);
	    boolean endReached = false;
	    for (int n = 0; n < end.length; n++)   {
		if (ch == end[n])     {
		    endReached = true;
		    break;
		}
	    }
	    if (endReached) break;
	    out.append(ch);
            pos++;
        }
        return (pos == in.length()) ? -1 : pos;
    }

    /**
     * Reads characters until the 'end' character is encountered.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            The Input String.
     * @param start
     *            Starting position.
     * @param end
     *            End characters.
     * @return The new position or -1 if no 'end' char was found.
     */
    public final static int readUntil(final StringBuilder out, final String in, final int start, final char end)
    {
        int pos = start;
        while (pos < in.length()) {
            final char ch = in.charAt(pos);
	    if (ch == end) break;
	    out.append(ch);
            pos++;
        }
        return (pos == in.length()) ? -1 : pos;
    }

    /**
     * Reads a markdown link.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            Input String.
     * @param start
     *            Starting position.
     * @return The new position or -1 if this is no valid markdown link.
     */
    public final static int readMdLink(final StringBuilder out, final String in, final int start)
    {
        int pos = start;
        int counter = 1;
        while (pos < in.length()) {
            final char ch = in.charAt(pos);
	    boolean endReached = false;
	    switch (ch) {
	    case '(':
		counter++;
		break;
	    case ' ':
		if (counter == 1) endReached = true;
		break;
	    case ')':
		counter--;
		if (counter == 0) endReached = true;
		break;
	    }
	    if (endReached) break;
	    out.append(ch);
	pos++;
	}
        return (pos == in.length()) ? -1 : pos;
    }

    /**
     * Reads a markdown link ID.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            Input String.
     * @param start
     *            Starting position.
     * @return The new position or -1 if this is no valid markdown link ID.
     */
    public final static int readMdLinkId(final StringBuilder out, final String in, final int start)
    {
        int pos = start;
        int counter = 1;
        while (pos < in.length())
        {
            final char ch = in.charAt(pos);
            boolean endReached = false;
            switch (ch)
            {
            case '\n':
                out.append(' ');
                break;
            case '[':
                counter++;
                out.append(ch);
                break;
            case ']':
                counter--;
                if (counter == 0)
                    endReached = true;
                else out.append(ch);
                break;
            default:
                out.append(ch);
                break;
            }
            if (endReached) break;
            pos++;
        }

        return (pos == in.length()) ? -1 : pos;
    }

    /**
     * Reads characters until any 'end' character is encountered, ignoring
     * escape sequences.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            The Input String.
     * @param start
     *            Starting position.
     * @param end
     *            End characters.
     * @return The new position or -1 if no 'end' char was found.
     */
    public final static int readRawUntil(final StringBuilder out, final String in, final int start, final char... end)
    {
        int pos = start;
        while (pos < in.length())
        {
            final char ch = in.charAt(pos);
            boolean endReached = false;
            for (int n = 0; n < end.length; n++)
            {
                if (ch == end[n])
                {
                    endReached = true;
                    break;
                }
            }
            if (endReached) break;
            out.append(ch);
            pos++;
        }

        return (pos == in.length()) ? -1 : pos;
    }

    /**
     * Reads characters until the end character is encountered, taking care of
     * HTML/XML strings.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            The Input String.
     * @param start
     *            Starting position.
     * @param end
     *            End characters.
     * @return The new position or -1 if no 'end' char was found.
     */
    public final static int readRawUntil(final StringBuilder out, final String in, final int start, final char end)
    {
        int pos = start;
        while (pos < in.length())
        {
            final char ch = in.charAt(pos);
            if (ch == end) break;
            out.append(ch);
            pos++;
        }

        return (pos == in.length()) ? -1 : pos;
    }

    /**
     * Reads characters until any 'end' character is encountered, ignoring
     * escape sequences.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            The Input String.
     * @param start
     *            Starting position.
     * @param end
     *            End characters.
     * @return The new position or -1 if no 'end' char was found.
     */
    public final static int readXMLUntil(final StringBuilder out, final String in, final int start, final char... end)
    {
        int pos = start;
        boolean inString = false;
        char stringChar = 0;
        while (pos < in.length())
        {
            final char ch = in.charAt(pos);
            if (inString)
            {
                if (ch == '\\')
                {
                    out.append(ch);
                    pos++;
                    if (pos < in.length())
                    {
                        out.append(ch);
                        pos++;
                    }
                    continue;
                }
                if (ch == stringChar)
                {
                    inString = false;
                    out.append(ch);
                    pos++;
                    continue;
                }
            }
            switch (ch)
            {
            case '"':
            case '\'':
                inString = true;
                stringChar = ch;
                break;
            }
            if (!inString)
            {
                boolean endReached = false;
                for (int n = 0; n < end.length; n++)
                {
                    if (ch == end[n])
                    {
                        endReached = true;
                        break;
                    }
                }
                if (endReached) break;
            }
            out.append(ch);
            pos++;
        }

        return (pos == in.length()) ? -1 : pos;
    }

    /**
     * Extracts the tag from an XML element.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            Input StringBuilder.
     */
    public final static void getXMLTag(final StringBuilder out, final StringBuilder in)
    {
        int pos = 1;
        if (in.charAt(1) == '/') pos++;
        while (Character.isLetterOrDigit(in.charAt(pos)))
        {
            out.append(in.charAt(pos++));
        }
    }

    /**
     * Extracts the tag from an XML element.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            Input String.
     */
    public final static void getXMLTag(final StringBuilder out, final String in)
    {
        int pos = 1;
        if (in.charAt(1) == '/') pos++;
        while (Character.isLetterOrDigit(in.charAt(pos)))
        {
            out.append(in.charAt(pos++));
        }
    }

    /**
     * Reads an XML element.
     * 
     * @param out
     *            The StringBuilder to write to.
     * @param in
     *            Input String.
     * @param start
     *            Starting position.
     * @param safeMode
     *            Whether to escape unsafe HTML tags or not
     * @return The new position or -1 if this is no valid XML element.
     */
    /*
    public final static int readXML(final StringBuilder out, final String in, final int start, final boolean safeMode)
    {
        int pos;
        final boolean isCloseTag;
        try
        {
            if (in.charAt(start + 1) == '/')
            {
                isCloseTag = true;
                pos = start + 2;
            }
            else if (in.charAt(start + 1) == '!')
            {
                out.append("<!");
                return start + 1;
            }
            else
            {
                isCloseTag = false;
                pos = start + 1;
            }
            if (safeMode)
            {
                final StringBuilder temp = new StringBuilder();
                pos = readXMLUntil(temp, in, pos, ' ', '/', '>');
                if (pos == -1) return -1;
                final String tag = temp.toString().trim().toLowerCase();
                if (HTML.isUnsafeHtmlElement(tag))
                {
                    out.append("&lt;");
                    if (isCloseTag) out.append('/');
                    out.append(temp);
                }
            }
            else
            {
                out.append('<');
                if (isCloseTag) out.append('/');
                pos = readXMLUntil(out, in, pos, ' ', '/', '>');
            }
            if (pos == -1) return -1;
            pos = readXMLUntil(out, in, pos, '/', '>');
            if (in.charAt(pos) == '/')
            {
                out.append(" /");
                pos = readXMLUntil(out, in, pos + 1, '>');
                if (pos == -1) return -1;
            }
            if (in.charAt(pos) == '>')
            {
                out.append('>');
                return pos;
            }
        }
        catch (StringIndexOutOfBoundsException e)
        {
            return -1;
        }
        return -1;
	}*/
}
