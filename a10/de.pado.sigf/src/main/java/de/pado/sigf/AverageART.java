/**
   class Average

   Copyright (C) 2008 Sebastian Pado
   
   This software is provided under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later
   version. **/

package de.pado.sigf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Compute average for observations. Each observation is a single Long.
 **/

class Average implements Statistic<Singleton<Long>> {

	// sufficient statistics:
	Long sum; // sum of all observations
	Long no; // number of observations

	@Override
	public double compute() {
		return (((double) sum) / no);
	}

	@Override
	public void add_observation(Singleton<Long> observation) {
		sum += observation.first();
		no += 1;
	}

	@Override
	public Average clone() {
		return new Average(sum, no);
	}

	Average() {
		sum = new Long(0);
		no = new Long(0);
	}

	Average(Long counter, Long noObs) {
		sum = counter;
		no = noObs;
	}

	@Override
	public long num_observations() {
		return no;
	}

}

/**
 * Computation of Averages for two datasets and comparison with a randomization
 * test This class extends the (abstract) ApproximateRandomizationTest class. It
 * only implements main(), readInput(), and calls the Average class.
 * 
 * @author pado
 * 
 */
public class AverageART extends ApproximateRandomizationTest<Singleton<Long>> {

	/**
	 * Read a vector of Long[1] (individual observations in Arrays) from a file.
	 */
	@Override
	public Vector<Singleton<Long>> readInput(InputStream filename)
			throws IOException, FileNotFoundException {

		Vector<Singleton<Long>> v = new Vector<Singleton<Long>>();
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(
				filename));
		String line;
		while ((line = fileReader.readLine()) != null) {
			if (line.length() == 0)
				continue;
			Long obs = Long.valueOf(line);
			Singleton<Long> longArray = new Singleton<Long>(obs);
			v.addElement(longArray);
		}
		return v;
	}

	/**
	 * Computation of Averages for two datasets and comparison with a
	 * randomization test.
	 * 
	 * @param <filename1>, <filename2>, <number of iterations>
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		if (args.length != 3) {
			System.err
					.println("Usage: AverageART <file1> <file2> <no. of iterations>");
			System.exit(1);
		}
		ApproximateRandomizationTest<Singleton<Long>> art = new AverageART();
		art.run(args[0], args[1], Average.class, Long.valueOf(args[2])
				.longValue());
	}

}
