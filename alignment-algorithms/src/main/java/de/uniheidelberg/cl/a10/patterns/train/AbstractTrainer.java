package de.uniheidelberg.cl.a10.patterns.train;

import java.util.Iterator;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		this.logger = Logger.getLogger(this.toString());
		this.logger.setUseParentHandlers(false);
		this.logger.addHandler(new ConsoleHandler());
	}

	@Override
	public abstract Model<T> train(Iterator<T> trainingInstances);

	public Model<T> init(final Iterator<T> trainingInstances) {
		throw new UnsupportedOperationException();
	}

	public void setLogLevel(final Level level) {
		this.logger.setLevel(level);
		for (Handler h : logger.getHandlers()) {
			h.setLevel(level);
		}
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
