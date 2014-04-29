package de.uniheidelberg.cl.a10.patterns.models.benchmarking;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import de.uniheidelberg.cl.a10.patterns.models.impl.MarkovModel_impl;

public class BMarkovModel {
	List<String> sequence1 = new ArrayList<String>();
	List<String> sequence2 = new ArrayList<String>();
	MarkovModel_impl<String> markovModel = new MarkovModel_impl<String>();
	@Rule
	public BenchmarkRule benchmarkRun = new BenchmarkRule();

	@Before
	public void setUp() throws Exception {
		RandomStringFactory rsf = new RandomStringFactory();

		rsf.numberOfStrings = 1000;
		while (rsf.hasNext()) {
			sequence1.add(rsf.next());
		}

		rsf.numberOfStrings = 1000;
		while (rsf.hasNext()) {
			sequence2.add(rsf.next());
		}

		markovModel.learn(sequence1);
	}

	@Test
	public void benchmarkLearning() {
		MarkovModel_impl<String> mm = new MarkovModel_impl<String>();
		mm.learn(sequence1);
		mm.learn(sequence2);
	}

	@Test
	public void benchmarkGetProbability() {
		markovModel.p("5", "10", "15");
		org.junit.Assert.assertTrue(true);
	}

}
