package de.uniheidelberg.cl.a10.patterns;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

/**
 * A generic model prior, i.e., an interface providing methods to judge the
 * probability of a model.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public interface ModelPrior<T extends Model<?>> {

	/**
	 * Returns a probability value for a given model.
	 * 
	 * @param model
	 *            The model to judge
	 * @return A probability value
	 */
	Probability getModelProbability(final T model);
}
