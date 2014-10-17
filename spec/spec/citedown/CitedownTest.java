package spec.citedown;

import com.github.neelsmith.citemark.*;
import java.io.StringReader;

import org.concordion.integration.junit3.ConcordionTestCase;

public class CitedownTest extends ConcordionTestCase {


    // check that marrkdown passed through unchanged
    public boolean mdOK(String markdown) {
	StringBuilder builtMarkdown = new StringBuilder();
	Converter emitter = new Converter();
        int res = emitter.recursiveEmitLine(builtMarkdown,markdown,0,MarkToken.NONE);
	return (builtMarkdown.toString().equals(markdown));
    }

    public boolean hasQuote(String citedown) {
	Converter emitter = new Converter();
        int pos = 0;

        while (pos < citedown.length()) {
	    MarkToken mt = emitter.getToken(citedown, pos) ;
            if (mt == MarkToken.QUOTE) {
                return true;
	    }
            pos++;
        }
	return false;
    }

    public String resolveRef(String citedown, String ref) {
	System.err.println("Resolve ref in: " + citedown + "\n for ref " + ref);
	Converter emitter = new Converter();
	try {
	    emitter.indexLinkReff(citedown);
	} catch (Exception e) {
	    System.err.println ("Exception indexing links: " + e.toString());
	}
	LinkRef refObj = emitter.linkRefs.get(ref);
	return refObj.link;
    }

    public String findLink(String citedown) {
	Converter emitter = new Converter();
	int start = emitter.findToken(citedown,0,MarkToken.LINK);
	String linkRef = emitter.getLinkReference(citedown,start);
	return  linkRef;
    }

    public boolean processLine(String citedown) {
	Converter emitter = new Converter();
        int res = emitter.recursiveEmitLine(new StringBuilder(),citedown,0,MarkToken.NONE);

	return false;
    }




    public boolean hasCite(String citedown) {
	Converter emitter = new Converter();
        int pos = 0;

        while (pos < citedown.length()) {
	    MarkToken mt = emitter.getToken(citedown, pos) ;
            if (mt == MarkToken.CITE) {
                return true;
	    } else {
		//System.err.println ("At char " +  pos + ", got token " + mt.toString());
	    }
            pos++;
        }
	return false;

    }



    public boolean typeForString(String tokenStr) {
	//Converter emitter = new Converter(Configuration.DEFAULT);
	Converter emitter = new Converter();
	MarkToken mToken = emitter.getToken(tokenStr, 0);
	//System.err.println ("GOt tokekn " + mToken.toString());
	return false;
    }

    public boolean getUrn(String ref) {
	/*
	Processor p = new Processor(new StringReader(ref), Configuration.DEFAULT);
	
	try {
	    Block block = p.readLines();
	    if (block.lines == null) {
		System.err.println ("block lines is null.");
	    } else {
		System.err.println ("block lines is " + block.lines );
		System.err.println ("and its value is " + block.lines.value );
		System.err.println("of type " + block.lines.getLineType(false).toString());
	    }

	    // readlines puts links in the  hashmap of he processor's emitter.
	    // need to examine it after calling readinlines.
	    System.err.println("Links map at this point is " + p.emitter.linkRefs.toString());

	}  catch (Exception e) {
	    System.err.println ("Exception getting block via readLines() or plucking lines from block.");
	}
	String retrievedUrn = Processor.process(ref, Configuration.DEFAULT);

	return (retrievedUrn == ref);*/
	return false;
    }
}
