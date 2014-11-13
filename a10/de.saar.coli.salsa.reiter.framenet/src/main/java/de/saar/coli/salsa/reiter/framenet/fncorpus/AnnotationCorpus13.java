package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.io.File;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;

/**
 * Parses the annotated corpora of FrameNet 1.3.
 * 
 * @author reiter
 * 
 */
public class AnnotationCorpus13 extends AnnotationCorpus {

    public AnnotationCorpus13(final FrameNet frameNet, final Logger logger) {
	super(frameNet, logger);
    }

    @Override
    protected void parseFile(final File file) {
	// Make document
	Document document = null;
	try {
	    SAXReader reader = new SAXReader();
	    document = reader.read(file);
	} catch (DocumentException e) {
	    getLogger().severe(e.getMessage());
	}
	getLogger().fine(
		"XML Document " + file.getAbsolutePath() + " has been read.");

	try {
	    Element luanno = document.getRootElement();

	    LexicalUnit lu =
		    frameNet.getLexicalUnit(Integer.valueOf(luanno
			    .attributeValue("ID")));
	    addAnnotation(lu, new AnnotatedLexicalUnit13(this, luanno, lu));

	} catch (FrameNotFoundException e) {
	    e.printStackTrace();
	}
    }

}
