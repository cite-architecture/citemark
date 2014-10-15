package spec.citedown;

import com.github.neelsmith.citemark.*;
import java.io.StringReader;

import org.concordion.integration.junit3.ConcordionTestCase;

public class CitedownTest extends ConcordionTestCase {


    public boolean hasQuote(String citedown) {
	Emitter emitter = new Emitter();
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


    public boolean hasCite(String citedown) {
	Emitter emitter = new Emitter();
        int pos = 0;

        while (pos < citedown.length()) {
	    MarkToken mt = emitter.getToken(citedown, pos) ;
            if (mt == MarkToken.CITE) {
                return true;
	    } else {
		System.err.println ("At char " +  pos + ", got token " + mt.toString());
	    }
            pos++;
        }
	return false;

    }



    public boolean typeForString(String tokenStr) {
	//Emitter emitter = new Emitter(Configuration.DEFAULT);
	Emitter emitter = new Emitter();
	MarkToken mToken = emitter.getToken(tokenStr, 0);
	System.err.println ("GOt tokekn " + mToken.toString());
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