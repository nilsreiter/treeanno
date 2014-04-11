package de.uniheidelberg.cl.a10.patterns;

import java.util.Collection;

/**
 * An interface for controller of multiple training threads
 * 
 * @author reiter
 * 
 * @param <T>
 */
public interface TrainerController<T> {

	/**
	 * A callback method that can be called when a training thread is finished.
	 * <b>Needs to be thread-safe!</b>
	 * 
	 * @param trainingConfiguration
	 * @param model
	 */
	void trainingFinished(final TrainingConfiguration trainingConfiguration,
			final Model<T> model);

	/**
	 * A wrapper class for a trainer that extends {@link java.lang.Thread} in
	 * order to be usable in multi-threaded training
	 * 
	 * @author reiter
	 * 
	 * @param <T>
	 */
	static class TrainerThread<T> extends Thread {
		Trainer<T> trainer;
		Collection<T> trainingInstances;
		TrainerController<T> callback;

		public TrainerThread(final TrainerController<T> cb,
				final Trainer<T> tr, final Collection<T> trInstances) {
			trainer = tr;
			trainingInstances = trInstances;
			callback = cb;
		}

		@Override
		public void run() {
			Model<T> m = trainer.train(trainingInstances.iterator());
			callback.trainingFinished(trainer.getTrainingConfiguration(), m);
		}
	}

}
