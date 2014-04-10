package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.ParsingException;

public class FrameNetCorpus15 extends FrameNetCorpus {

    public FrameNetCorpus15(final FrameNet frameNet, final Logger logger) {
	super(frameNet, logger);

    }

    @Override
    public void parse(final File file) throws FrameNotFoundException,
	    FrameElementNotFoundException, ParsingException {
	if (file.isFile()) {
	    this.parseFile(file);
	} else if (file.isDirectory()) {
	    for (File f : file.listFiles(new FilenameFilter() {

		@Override
		public boolean accept(final File arg0, final String arg1) {
		    return arg1.endsWith(".xml") && !arg1.startsWith(".");
		}
	    })) {
		this.parseFile(f);
	    }
	}
    }

    protected void parseFile(final File file) throws FrameNotFoundException,
	    FrameElementNotFoundException, ParsingException {
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
	    Element rootElement = document.getRootElement();

	    Iterator<?> sentenceIterator =
		    rootElement.elementIterator("sentence");

	    while (sentenceIterator.hasNext()) {
		Element sentenceElement = (Element) sentenceIterator.next();
		Sentence15 sentence = new Sentence15(this, sentenceElement);
		this.getSentences().add(sentence);
		getSentenceIndex().put(sentence.getIdString(), sentence);
	    }

	} catch (NumberFormatException e) {
	    e.printStackTrace();
	}
    }

}
