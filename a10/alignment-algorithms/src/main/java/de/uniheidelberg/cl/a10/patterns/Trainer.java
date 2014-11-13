package de.uniheidelberg.cl.a10.patterns;

import java.util.Iterator;

/**
 * General interface for training methods.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public interface Trainer<T> {

	/**
	 * Trains the model based on the given training instances.
	 * 
	 * @param trainingInstances
	 * @return
	 */
	public Model<T> train(Iterator<T> trainingInstances);

	/**
	 * Returns the configuration used for training
	 * 
	 * @return
	 */
	public TrainingConfiguration getTrainingConfiguration();

}
