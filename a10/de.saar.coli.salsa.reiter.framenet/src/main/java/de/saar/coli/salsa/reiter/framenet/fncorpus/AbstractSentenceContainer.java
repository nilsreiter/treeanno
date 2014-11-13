package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.LinkedList;
import java.util.List;

import de.saar.coli.salsa.reiter.framenet.ISentence;

/**
 * 
 * @author reiter
 * 
 * @param <T>
 */
public abstract class AbstractSentenceContainer<T extends ISentence> {

    /**
     * The list of sentences.
     */
    List<T> sentences = null;

    /**
     * The default constructor.
     */
    protected AbstractSentenceContainer() {
	sentences = new LinkedList<T>();
    }

    /**
     * @param theSentence
     *            The sentence to add
     * @return a boolean
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean addSentence(final T theSentence) {
	return sentences.add(theSentence);
    }

    /**
     * @param index
     *            the index
     * @return The sentence at index
     * @see java.util.List#get(int)
     */
    public ISentence getSentence(final int index) {
	return sentences.get(index);
    }

    /**
     * @return The number of sentences
     * @see java.util.List#size()
     */
    public int sentences() {
	return sentences.size();
    }

    /**
     * 
     * 
     * @return the list of sentences
     */
    public List<T> getSentences() {
	return sentences;
    }

}
