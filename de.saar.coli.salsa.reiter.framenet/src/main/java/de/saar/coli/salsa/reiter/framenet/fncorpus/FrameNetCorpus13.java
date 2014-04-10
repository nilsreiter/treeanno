package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.ParsingException;

public class FrameNetCorpus13 extends FrameNetCorpus {

    /**
     * The constructor.
     * 
     * @param frameNet
     *            The FrameNet object
     * @throws FrameNotFoundException
     *             This exception is thrown when a frame is used in the
     *             annotation which does not exist in the FrameNet database
     * @throws FrameElementNotFoundException
     *             This exception is thrown when a frame element is used that
     *             does not exist in the FN database OR does not belong to the
     *             frame in which it was used
     */
    public FrameNetCorpus13(final FrameNet frameNet, final Logger logger)
	    throws FrameNotFoundException, FrameElementNotFoundException {
	super(frameNet, logger);
    }

    /**
     * This method does all the parsing stuff.
     * 
     * @throws FrameNotFoundException
     *             This exception is thrown when a frame is used in the
     *             annotation which does not exist in the FrameNet database
     * @throws FrameElementNotFoundException
     *             This exception is thrown when a frame element is used that
     *             does not exist in the FN database OR does not belong to the
     *             frame in which it was used
     */
    @Override
    public void parse(final File file) throws FrameNotFoundException,
	    FrameElementNotFoundException, ParsingException {

	// Make document
	Document document = null;
	try {
	    SAXReader reader = new SAXReader();
	    document = reader.read(file);
	} catch (DocumentException e) {
	    getLogger().severe(e.getMessage());
	}
	if (getLogger() != null) {
	    getLogger().info(
		    "XML Document (" + file.getAbsolutePath()
			    + ") has been read.");
	}

	try {

	    List<?> sentences =
		    document.selectNodes("/corpus/documents/document/paragraphs/paragraph/sentences/sentence"); // xpath.selectNodes(document);

	    for (Object sent : sentences) {
		Element sentence = (Element) sent;
		if (sent != null) {
		    Sentence s = new Sentence13(this, sentence);
		    getSentences().add(s);
		    getSentenceIndex().put(s.getIdString(), s);
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
