package de.uniheidelberg.cl.a10.parser;

import java.io.File;
import java.util.List;

import de.uniheidelberg.cl.a10.data.BaseSentence;

/**
 * Interface for all trainable parsers.
 * 
 * @author reiter
 * 
 * @param <T>
 *            The style of part of speech tags
 */
public interface ITrainable {

	/**
	 * Trains a new model for the parser. The corpus is just a list of
	 * sentences.
	 * 
	 * @param corpus
	 *            A list of training sentences
	 * @param modelFile
	 *            The file to store the model to
	 * @param options
	 *            A variable list of options. TODO: This will change
	 */
	public void train(List<BaseSentence> corpus, File modelFile,
			Object... options);
}
