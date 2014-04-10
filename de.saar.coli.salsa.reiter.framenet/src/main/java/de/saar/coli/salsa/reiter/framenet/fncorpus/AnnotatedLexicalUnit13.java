package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.Iterator;

import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;

/**
 * This class represents a lexical unit annotation in FrameNet 1.3.
 * 
 * @author reiter
 * 
 */
public class AnnotatedLexicalUnit13 extends AnnotatedLexicalUnit {

    /**
     * The constructor for this class.
     * 
     * @param annotationCorpus
     *            The corpus this annotation belongs to
     * @param element
     *            The XML element
     * @param lu
     *            The lexical unit
     * @throws FrameNotFoundException
     *             If a frame is no defined in the FrameNet database
     */
    public AnnotatedLexicalUnit13(final AnnotationCorpus annotationCorpus,
	    final Element element, final LexicalUnit lu)
	    throws FrameNotFoundException {
	super(annotationCorpus, lu);
	incorporatedFE = element.attributeValue("incorporatedFE");
	definition = element.valueOf("definition");
	frame =
		annotationCorpus.getFrameNet().getFrame(
			element.attributeValue("frame"));

	name = element.attributeValue("name");
	Iterator<?> scIter = element.elementIterator("subcorpus");
	while (scIter.hasNext()) {
	    Element subcorpus = (Element) scIter.next();

	    Iterator<?> asIter = subcorpus.elementIterator("annotationSet");
	    while (asIter.hasNext()) {
		Element annotationSetElement = (Element) asIter.next();
		Sentence sentence =
			new Sentence13(this.annotationCorpus,
				annotationSetElement.element("sentence"));
		sentences.add(sentence);
		try {
		    new AnnotationSet13(sentence, annotationSetElement, frame,
			    lexicalUnit);
		} catch (FrameElementNotFoundException e) {
		    e.printStackTrace();
		}
	    }

	}
    }
}
