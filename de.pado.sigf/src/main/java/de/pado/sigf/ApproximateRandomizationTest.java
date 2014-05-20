package de.pado.sigf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

/**
 class ApproximativeRandomizationTest

 Copyright (C) 2006 Sebastian Pado

 This software is provided under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2 of the License, or (at your option) any later
 version. **/

/** This class implements an approximate randomisation test. **/

public abstract class ApproximateRandomizationTest<Obs extends Observation<?>> {

	/**
	 * apply_test runs the test. It takes as arguments two Vectors of
	 * observations (Observation) (same size!) the Class of the statistic to be
	 * computed a Long, the number of iterations.
	 * 
	 * Assumptions: The statistic has to implement the interface Statistic; the
	 * observations (components of the observations Vector) can be fed directly
	 * into the add_observation method of the statistic.
	 * 
	 * Method: The method shuffles the input vectors and determines the number
	 * of cases the shuffle outperforms the original observations. The ratio of
	 * (outperforance cases)/(all cases) can be interpreted as a p value. After
	 * each iteration, it prints: (iteration number; outperformance case?; size
	 * of statistics differencs; ratio). At the end, it prints all the important
	 * figures to STDERR.
	 **/

	public double apply_test(Vector<Obs> orig_observations_1,
			Vector<Obs> orig_observations_2,
			Class<? extends Statistic<Obs>> statistics_class, long iterations)
			throws InstantiationException, IllegalAccessException {

		double t_o1_o2, t_x_y = 0;
		int i, j;
		long better = 0;
		Statistic<Obs> o1_stat, o2_stat, x_stat, y_stat, common_stat;
		Random r = new Random();

		// confirm length of vectors
		if (orig_observations_1.size() != orig_observations_2.size()) {
			System.err.println("Different numbers of observations! Argh.");
			System.exit(0);
		}

		// compute |stat(o1)-stat(o2)|

		o1_stat = statistics_class.newInstance();
		o2_stat = statistics_class.newInstance();

		for (i = 0; i < orig_observations_1.size(); i++) {
			Obs e1 = orig_observations_1.elementAt(i);
			Obs e2 = orig_observations_2.elementAt(i);
			o1_stat.add_observation(e1);
			o2_stat.add_observation(e2);
		}
		t_o1_o2 = Math.abs(o1_stat.compute() - o2_stat.compute());

		// identify common observations and compute partial statistic for that
		// set.

		Triple<Vector<Obs>> obs = crop_vectors(orig_observations_1,
				orig_observations_2);
		Vector<Obs> observations_1 = obs.first();
		Vector<Obs> observations_2 = obs.second();
		Vector<Obs> common_observations = obs.third();
		common_stat = statistics_class.newInstance();

		for (j = 0; j < common_observations.size(); j++) {
			Obs e = common_observations.elementAt(j);
			common_stat.add_observation(e);
		}

		// do randomisation:
		// shuffle o1 and o2 into random variables x and y
		// compute |stat(x) - stat(y)|
		// if larger than the difference above, count as "good case"

		for (i = 0; i < iterations; i++) {

			// try {
			x_stat = common_stat.clone();
			y_stat = common_stat.clone();

			// }
			// catch (InstantiationException e) {}
			// catch (IllegalAccessException e) {}

			for (j = 0; j < observations_1.size(); j++) {
				Obs e1 = observations_1.elementAt(j);
				Obs e2 = observations_2.elementAt(j);
				boolean b = r.nextBoolean();
				if (b) {
					x_stat.add_observation(e1);
					y_stat.add_observation(e2);
				} else {
					x_stat.add_observation(e2);
					y_stat.add_observation(e1);
				}
			}

			// sanity check
			if ((x_stat.num_observations() != orig_observations_1.size())
					|| (y_stat.num_observations() != orig_observations_1.size())) {
				System.err
						.println("Error: Statistic has not seen all observations!?");
				System.err.println("Statistic: " + x_stat.num_observations());
				System.err.println("Original: " + orig_observations_1.size());
				System.exit(0);
			}

			t_x_y = Math.abs(x_stat.compute() - y_stat.compute());

			if (t_x_y >= t_o1_o2)
				better += 1;

			// System.err.println((i+1)+"\t"+better+"\t"+(t_x_y-t_o1_o2)+"\t"+((better
			// + 1.0)/(i + 1.0)));
		}

		// return ration of good cases to all cases

		double p = (better + 1.0) / (iterations + 1.0);

		System.out.println("Number of observations: "
				+ orig_observations_1.size());
		System.out.println("Number of different observations: "
				+ observations_1.size());
		System.out.println("Statistic of file1: " + o1_stat.compute());
		System.out.println("Statistic of file2: " + o2_stat.compute());
		System.out.println("Difference: " + t_o1_o2);
		System.out.println("Number of iterations: " + iterations);
		System.out.println("p-value (2-tailed): " + p);

		return p;
	}

	/**
	 * crop_vectors returns Vector[3]: Vector[1]: particular observations for
	 * the first parameter vector Vector[2]: particular observations for the
	 * second parameter vector Vector[3]: common observations
	 **/

	private Triple<Vector<Obs>> crop_vectors(Vector<Obs> observations_1,
			Vector<Obs> observations_2) {
		Vector<Obs> obs_1_new = new Vector<Obs>();
		Vector<Obs> obs_2_new = new Vector<Obs>();
		Vector<Obs> obs_common = new Vector<Obs>();

		for (int i = 0; i < observations_1.size(); i++) {
			Obs e1 = observations_1.elementAt(i);
			Obs e2 = observations_2.elementAt(i);
			if (e1.equals(e2)) {
				obs_common.addElement(e1);
			} else {
				obs_1_new.addElement(e1);
				obs_2_new.addElement(e2);
			}
		}
		Triple<Vector<Obs>> rv = new Triple<Vector<Obs>>(obs_1_new, obs_2_new,
				obs_common);
		return rv;
	}

	/**
	 * Method to read observations from a file
	 * 
	 * @param filename
	 *            : File with observations (one observation per line)
	 * @return Vector<Observation>: A vector with a Observation for each
	 *         observation. Must be compatible with the used implementation of
	 *         Statistic.addObservation()
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	abstract public Vector<Obs> readInput(InputStream filename)
			throws IOException, FileNotFoundException;

	/**
	 * Start the computation of the significance of the difference between two
	 * models.
	 * 
	 * @param filename1
	 *            : Observations for model1
	 * @param filename2
	 *            : Observations for model2
	 * @param statisticClass
	 *            : Class to be used as evaluation statistic (must inherit from
	 *            Statistic)
	 * @param numberOfIterations
	 *            : Number of iterations to be used (try 10.000-100.000,
	 *            depending on size of dataset)
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void run(String filename1, String filename2,
			Class<? extends Statistic<Obs>> statisticClass,
			long numberOfIterations) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		try {
			run(new FileInputStream(filename1), new FileInputStream(filename2),
					statisticClass, numberOfIterations);
		} catch (IOException e) {

		}
	}

	public void run(InputStream filename1, InputStream filename2,
			Class<? extends Statistic<Obs>> statisticClass,
			long numberOfIterations) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Vector<Obs> model1 = new Vector<Obs>();
		Vector<Obs> model2 = new Vector<Obs>();

		try {
			model1 = readInput((filename1));
			model2 = readInput((filename2));
		} catch (IOException e) {
		}

		apply_test(model1, model2, statisticClass, numberOfIterations);
		// System.out.println(args[0]+"\t"+args[1]+"\t"+p);
	}

}
