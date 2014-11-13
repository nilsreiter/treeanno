/**
   class FScore

   Copyright (C) 2006 Sebastian Pado
   
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
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Compute F-Score (beta=1) for observations. Each observation is a triple of
 * longs (long[3]): - number of true positives - total number of positives
 * according to model - total number of positives according to gold standard
 **/

class FScore implements Statistic<Triple<Long>> {

	Long true_pos; // all true positives
	Long model_pos; // all positives assigned by the model
	Long gold_pos; // all positives in the gold standard
	Long no; // number of observations

	@Override
	public double compute() {
		double recall;
		double precision;

		if (gold_pos == 0)
			recall = 0;
		else
			recall = ((double) true_pos / gold_pos);

		if (model_pos == 0)
			precision = 0;
		else
			precision = ((double) true_pos / model_pos);

		if ((precision == 0) && (recall == 0))
			return 0;
		else
			return (2 * precision * recall) / (precision + recall);
	}

	@Override
	public void add_observation(Triple<Long> observation) {
		true_pos += observation.first();
		model_pos += observation.second();
		gold_pos += observation.third();
		no += 1;
	}

	@Override
	public FScore clone() {
		return new FScore(true_pos, model_pos, gold_pos, no);
	}

	FScore() {
		true_pos = new Long(0);
		model_pos = new Long(0);
		gold_pos = new Long(0);
		no = new Long(0);
	}

	FScore(long tp, long mp, long gp, long num_obs) {
		true_pos = tp;
		model_pos = mp;
		gold_pos = gp;
		no = num_obs;
	}

	@Override
	public long num_observations() {
		return no;
	}

}

/**
 * Computation of F-Scores for two datasets and comparison with a randomization
 * test This class extends the (abstract) ApproximateRandomizationTest class. It
 * only implements main(), readInput(), and calls the FScore class.
 * 
 * @author pado
 * 
 */
public class FScoreART extends ApproximateRandomizationTest<Triple<Long>> {

	/**
	 * Read a vector of triples from a file.
	 */
	public Vector<Triple<Long>> readInput(InputStream filename)
			throws IOException, FileNotFoundException {

		Vector<Triple<Long>> v = new Vector<Triple<Long>>();
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(
				filename));
		String line;
		while ((line = fileReader.readLine()) != null) {
			if (line.length() == 0)
				continue;
			StringTokenizer linet = new StringTokenizer(line, " ");
			Long true_pos = Long.valueOf(linet.nextToken());
			Long model_pos = Long.valueOf(linet.nextToken());
			Long gold_pos = Long.valueOf(linet.nextToken());

			Triple<Long> triple = new Triple<Long>(true_pos, model_pos,
					gold_pos);
			v.addElement(triple);
		}
		return v;
	}

	/**
	 * Computation of F-Scores for two datasets and comparison with a
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
					.println("Usage: FScoreART <file1> <file2> <no. of iterations");
			System.exit(1);
		}
		ApproximateRandomizationTest<Triple<Long>> art = new FScoreART();
		art.run(args[0], args[1], FScore.class, Long.valueOf(args[2])
				.longValue());
	}

}
