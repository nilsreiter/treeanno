package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IRealizedFrame;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.saar.coli.salsa.reiter.framenet.SemanticType;
import de.saar.coli.salsa.reiter.framenet.SemanticTypeNotFoundException;

/**
 * This class represents a lexical unit annotation in FrameNet 1.5.
 * 
 * @author reiter
 * 
 */
public class AnnotatedLexicalUnit15 extends AnnotatedLexicalUnit {

    String status = null;

    List<SubCorpus15> subCorpora = null;

    /**
     * The semantic type of this lexical unit.
     */
    SemanticType semanticType = null;

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
    public AnnotatedLexicalUnit15(final AnnotationCorpus annotationCorpus,
	    final Element element, final LexicalUnit lu)
	    throws FrameNotFoundException {
	super(annotationCorpus, lu);
	subCorpora = new LinkedList<SubCorpus15>();

	definition = element.elementTextTrim("definition");
	frame =
		annotationCorpus.getFrameNet().getFrame(
			element.attributeValue("frame"));
	status = element.attributeValue("status");
	name = element.attributeValue("name");
	setId(element.attributeValue("ID"));
	if (element.element("semType") != null) {
	    try {
		semanticType =
			annotationCorpus.getFrameNet().getSemanticType(
				element.element("semType").attributeValue(
					"name"));
	    } catch (SemanticTypeNotFoundException e) {
		e.printStackTrace();
	    }
	}
	Iterator<?> scIter = element.elementIterator("subCorpus");
	String lexemeName = element.element("lexeme").attributeValue("name");
	while (scIter.hasNext()) {
	    Element subcorpusElement = (Element) scIter.next();
	    SubCorpus15 sc15 =
		    new SubCorpus15(this, annotationCorpus, frame,
			    subcorpusElement);

	    subCorpora.add(sc15);
	}

	// TODO: Geflickt
	for (de.saar.coli.salsa.reiter.framenet.Sentence sentence : this
		.getSentences()) {
	    for (IRealizedFrame rf : sentence.getRealizedFrames()) {
		rf.getTarget().setProperty("LEMMA", lexemeName);
	    }
	}
    }

    public AnnotatedLexicalUnit15(final AnnotationCorpus annotationCorpus,
	    final LexicalUnit lexicalUnit) throws FrameNotFoundException {
	super(annotationCorpus, lexicalUnit);
	subCorpora = new LinkedList<SubCorpus15>();
    }

    /**
     * @return the status
     */
    public String getStatus() {
	return status;
    }

    /**
     * @return the subCorpora
     */
    public List<SubCorpus15> getSubCorpora() {
	return subCorpora;
    }

    /**
     * @return the semanticType
     */
    public SemanticType getSemanticType() {
	return semanticType;
    }

    /**
     * @param theStatus
     *            the status to set
     */
    public void setStatus(final String theStatus) {
	this.status = theStatus;
    }

    /**
     * @param theSemanticType
     *            the semanticType to set
     */
    public void setSemanticType(final SemanticType theSemanticType) {
	this.semanticType = theSemanticType;
    }

    /**
     * @param e
     * @return
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean addSubCorpus(final SubCorpus15 e) {
	return subCorpora.add(e);
    }

    /**
     * @param index
     * @return
     * @see java.util.List#get(int)
     */
    public SubCorpus15 getSubCorpus(final int index) {
	return subCorpora.get(index);
    }

    public SubCorpus15 getSubCorpus(final String subCorpusName) {
	for (SubCorpus15 sc : subCorpora) {
	    if (sc.getName().equals(subCorpusName)) {
		return sc;
	    }
	}
	return null;
    }

    /**
     * @return
     * @see java.util.List#size()
     */
    public int subcorpora() {
	return subCorpora.size();
    }

    /**
     * This method returns all sentences in this annotated lexical unit. The
     * sentences actually belong to sub corpora. This is just a convenience
     * method.
     * 
     * @return all sentences in this lexical unit
     */
    @Override
    public List<de.saar.coli.salsa.reiter.framenet.Sentence> getSentences() {
	List<de.saar.coli.salsa.reiter.framenet.Sentence> ret =
		new LinkedList<de.saar.coli.salsa.reiter.framenet.Sentence>();
	for (SubCorpus15 sc : subCorpora) {
	    ret.addAll(sc.getSentences());
	}
	return ret;
    }

}
