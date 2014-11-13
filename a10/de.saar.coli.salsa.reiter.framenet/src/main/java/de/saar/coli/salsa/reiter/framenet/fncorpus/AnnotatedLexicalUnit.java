package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.List;

import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IHasID;
import de.saar.coli.salsa.reiter.framenet.Lexeme;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;

/**
 * This class represents one luXML-file, i.e., a lexical unit with its example
 * annotations.
 * 
 * @author reiter
 * @since 0.4
 * 
 */
public abstract class AnnotatedLexicalUnit extends
	AbstractSentenceContainer<de.saar.coli.salsa.reiter.framenet.Sentence>
	implements IHasID {
    /**
     * The lexical unit in the FrameNet database.
     */
    LexicalUnit lexicalUnit = null;

    /**
     * The definition string.
     */
    String definition = null;

    /**
     * A list of incorporated FEs.
     */
    String incorporatedFE = null;

    /**
     * The corpus.
     */
    AnnotationCorpus annotationCorpus = null;

    /**
     * The frame in which this lexical unit is listed.
     */
    Frame frame = null;

    /**
     * The name of the lexical unit, e.g., <i>bark.v</i>.
     */
    String name = null;

    /**
     * 
     */
    int id = -1;

    /**
     * The constructor processes the XML node(s).
     * 
     * @param theAnnotationCorpus
     *            The corpus this unit belongs to
     * @param lu
     *            The lexical unit
     * @throws FrameNotFoundException
     *             If a frame is used undefined
     */
    protected AnnotatedLexicalUnit(final AnnotationCorpus theAnnotationCorpus,
	    final LexicalUnit lu) throws FrameNotFoundException {
	super();
	lexicalUnit = lu;
	annotationCorpus = theAnnotationCorpus;
    }

    /**
     * Returns the definition of the lexical unit.
     * 
     * @return a string
     */
    public String getDefinition() {
	return definition;
    }

    /**
     * Returns the incorporared frame element.
     * 
     * @return A string
     */
    public String getIncorporatedFE() {
	return incorporatedFE;
    }

    /**
     * Returns a collection of lexemes in this lexical unit.
     * 
     * @return The lexemes of this lexical unit
     */
    public List<Lexeme> getLexemes() {
	return lexicalUnit.getLexemes();
    }

    /**
     * Returns a list of sentences containing this lexical unit.
     * 
     * @return A list of sentences
     */
    @Override
    public List<de.saar.coli.salsa.reiter.framenet.Sentence> getSentences() {
	return sentences;
    }

    /**
     * Returns the frame.
     * 
     * @return a frame
     */
    public Frame getFrame() {
	return frame;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * The name of a lexical unit, e.g., <i>bark.v</i>.
     * 
     * @param theName
     *            the name to set
     */
    public void setName(final String theName) {
	this.name = theName;
    }

    /**
     * @return the id
     */
    @Override
    public String getIdString() {
	return Integer.toString(id);
    }

    public int getId() {
	return id;
    }

    /**
     * @return the lexicalUnit
     */
    public LexicalUnit getLexicalUnit() {
	return lexicalUnit;
    }

    /**
     * @param theLexicalUnit
     *            the lexicalUnit to set
     */
    public void setLexicalUnit(final LexicalUnit theLexicalUnit) {
	this.lexicalUnit = theLexicalUnit;
    }

    /**
     * @param theDefinition
     *            the definition to set
     */
    public void setDefinition(final String theDefinition) {
	this.definition = theDefinition;
    }

    /**
     * @param theIncorporatedFE
     *            the incorporatedFE to set
     */
    public void setIncorporatedFE(final String theIncorporatedFE) {
	this.incorporatedFE = theIncorporatedFE;
    }

    /**
     * @param theFrame
     *            the frame to set
     */
    public void setFrame(final Frame theFrame) {
	this.frame = theFrame;
    }

    /**
     * @param theId
     *            the id to set
     */
    public void setId(final String theId) {
	this.id = Integer.parseInt(theId);
    }

    public void setId(final int theId) {
	this.id = theId;
    }

    /**
     * @return the partOfSpeech
     */
    public IPartOfSpeech getPartOfSpeech() {
	return lexicalUnit.getPartOfSpeech();
    }

    @Override
    public String toString() {
	return this.getName();
    }

}
