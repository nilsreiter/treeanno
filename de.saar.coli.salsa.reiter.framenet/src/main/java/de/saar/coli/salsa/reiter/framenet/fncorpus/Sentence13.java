package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.List;

import org.dom4j.Element;
import org.jaxen.JaxenException;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.uniheidelberg.cl.reiter.util.CharacterRange;

public class Sentence13 extends Sentence {
    /**
     * This constructor is to be used with AnnotationCorpus.
     * 
     * @param annotationCorpus
     *            The corpus
     * @param element
     *            The XML element of the sentence
     * @throws JaxenException
     */
    public Sentence13(final AnnotationCorpus annotationCorpus,
	    final Element element) {
	super(element.attributeValue("ID"), element.element("text").getText());
	corpusReader = annotationCorpus;
	List<?> allTokens =
		element.getParent().selectNodes(
			"layers/layer/labels/label[@start and @end]");

	for (Object tokenObj : allTokens) {
	    Element token = (Element) tokenObj;
	    CharacterRange range =
		    new CharacterRange(new Integer(
			    token.attributeValue("start")), new Integer(
			    token.attributeValue("end")) + 1);
	    addToken(range);
	}
    }

    /**
     * This constructor takes a FrameNetCorpus object and the XML element node
     * representing a single sentence in the data file.
     * 
     * @param reader
     *            The FrameNetCorps
     * @param node
     *            The sentence node
     * @throws FrameElementNotFoundException
     *             If a frame element is not defined
     * @throws FrameNotFoundException
     *             If a frame is not defined
     * @throws ParsingException
     *             If an XML exception occurs while parsing
     * 
     */
    protected Sentence13(final FrameNetCorpus reader, final Element node)
	    throws FrameElementNotFoundException, FrameNotFoundException,
	    ParsingException {
	super(node.attributeValue("ID"), node.element("text").getText());
	this.corpusReader = reader;

	// Search for all tokens, add them to the list

	String text = this.getText();

	int end = 0;
	int begin = 0;
	while (end < text.length()) {
	    end = text.indexOf(" ", begin);
	    if (end == -1) {
		end = text.length();
	    }
	    System.err.println(text.substring(begin, end));
	    addToken(new CharacterRange(begin, end));
	    begin = end + 1;
	}

	/*
	 * List<?> allTokens = node.selectNodes(
	 * "annotationSets/annotationSet/layers/layer/labels/label[@start and @end]"
	 * ); for (Object tokenObj : allTokens) { Element token = (Element)
	 * tokenObj; Range range = new Range(new
	 * Integer(token.attributeValue("start")), new
	 * Integer(token.attributeValue("end")) + 1); addToken(range); }
	 */

	List<?> asets =
		node.selectNodes("annotationSets/annotationSet[@frameName]");

	// NodeList asets = node.getElementsByTagName("annotationSet");
	// if (asets != null) {
	for (Object aseto : asets) {
	    Element aset = (Element) aseto;
	    if (aset.attributeValue("frameName") != "") {
		try {
		    annotationSets.add(new AnnotationSet13(this, aset));
		} catch (ParsingException e) {
		    getCorpus().getLogger().severe(e.getMessage());
		    if (reader.isAbortOnError()) {
			throw new ParsingException(e.getMessage());
		    }
		} catch (FrameNotFoundException e) {
		    getCorpus().getLogger().warning(e.getMessage());
		    if (reader.isAbortOnError()) {
			throw new FrameNotFoundException(e.getFrameName());
		    }
		}
	    }
	}

    }

}
