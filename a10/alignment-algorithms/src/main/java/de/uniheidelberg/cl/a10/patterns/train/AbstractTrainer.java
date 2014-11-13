package de.uniheidelberg.cl.a10.patterns.train;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniheidelberg.cl.a10.patterns.Model;
import de.uniheidelberg.cl.a10.patterns.Trainer;

/**
 * A default trainer for generic models. This implementation initializes a
 * logger with a console handler.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public abstract class AbstractTrainer<T> implements Trainer<T> {

	protected transient Logger logger;

	public AbstractTrainer() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public abstract Model<T> train(Iterator<T> trainingInstances);

	public Model<T> init(final Iterator<T> trainingInstances) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public void setLogger(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String toString() {
		return this.getClass().getCanonicalName();
	}

}
