package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.Iterator;

import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.CorpusReader;
import de.saar.coli.salsa.reiter.framenet.Frame;

/**
 * 
 * @author reiter
 * 
 */
public class SubCorpus15 extends AbstractSentenceContainer<Sentence15> {
    /**
     * The name of the subcorpus.
     */
    String name = null;

    /**
     * The annotated lexical unit.
     */
    AnnotatedLexicalUnit15 annotatedLexicalUnit = null;

    /**
     * 
     * @param alu
     *            The annotated lexical unit
     * @param cr
     *            The corpus reader
     * @param frame
     *            The frame
     * @param subcorpusElement
     *            The XML element representing this sub corpus
     */
    public SubCorpus15(final AnnotatedLexicalUnit15 alu, final CorpusReader cr,
	    final Frame frame, final Element subcorpusElement) {
	annotatedLexicalUnit = alu;
	name = subcorpusElement.attributeValue("name");

	Iterator<?> sentenceIter = subcorpusElement.elementIterator("sentence");
	while (sentenceIter.hasNext()) {
	    Element sentenceElement = (Element) sentenceIter.next();
	    Sentence15 sentence =
		    new Sentence15(alu, cr, frame, this, sentenceElement);
	    if (sentence.isSane()) {
		alu.addSentence(sentence);
		sentences.add(sentence);
	    }
	}
    }

    /**
     * A constructor without need for an XML element.
     * 
     * @param theName
     *            The name of the subcorpus
     * @param alu
     *            The annotated lexical unit
     */
    public SubCorpus15(final AnnotatedLexicalUnit15 alu, final String theName) {
	annotatedLexicalUnit = alu;
	name = theName;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param theName
     *            the name to set
     */
    public void setName(final String theName) {
	this.name = theName;
    }

    /**
     * @return the annotatedLexicalUnit
     */
    public AnnotatedLexicalUnit15 getAnnotatedLexicalUnit() {
	return annotatedLexicalUnit;
    }

    /**
     * @param annotatedLexicalUnit
     *            the annotatedLexicalUnit to set
     */
    public void setAnnotatedLexicalUnit(
	    final AnnotatedLexicalUnit15 annotatedLexicalUnit) {
	this.annotatedLexicalUnit = annotatedLexicalUnit;
    }

    protected void setLemma(final String lemma) {

    }

}
