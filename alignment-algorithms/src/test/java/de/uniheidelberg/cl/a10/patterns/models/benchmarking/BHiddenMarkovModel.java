package de.uniheidelberg.cl.a10.patterns.models.benchmarking;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import de.uniheidelberg.cl.a10.patterns.models.impl.HiddenMarkovModel_impl;

/**
 * <h2>Benchmark Results</h2> <h3>getProbability()</h3>
 * <table border="1">
 * <tr>
 * <th>SVN Revision</th>
 * <th>Changes</th>
 * <th>Round</th>
 * <th>GC.calls</th>
 * <th>GC.time</th>
 * </tr>
 * <tr>
 * <td>3018</td>
 * <td>Baseline</td>
 * <td>0.12</td>
 * <td>19</td>
 * <td>0.10</td>
 * </tr>
 * </table>
 * 
 * <h3>forwardViterbi()</h3>
 * <table border="1">
 * <tr>
 * <th>SVN Revision</th>
 * <th>Changes</th>
 * <th>Round</th>
 * <th>GC.calls</th>
 * <th>GC.time</th>
 * </tr>
 * <tr>
 * <td>3018</td>
 * <td>Baseline</td>
 * <td>0.35</td>
 * <td>75</td>
 * <td>0.09</td>
 * </tr>
 * </table>
 * 
 * <h3>parallelViterbi()</h3>
 * <table border="1">
 * <tr>
 * <th>SVN Revision</th>
 * <th>Changes</th>
 * <th>Round</th>
 * <th>GC.calls</th>
 * <th>GC.time</th>
 * </tr>
 * <tr>
 * <td>3018</td>
 * <td>Baseline</td>
 * <td>0.35</td>
 * <td>75</td>
 * <td>0.09</td>
 * </tr>
 * </table>
 * 
 * @author reiter
 * 
 */
public class BHiddenMarkovModel {

	List<List<Integer>> fixedLists = null;

	HiddenMarkovModel_impl<Integer> hiddenMarkovModel;

	@Rule
	public BenchmarkRule benchmarkRun = new BenchmarkRule();

	@Before
	public void setUp() throws Exception {
		hiddenMarkovModel = new HiddenMarkovModel_impl<Integer>();
		fixedLists = new LinkedList<List<Integer>>();
		fixedLists.add(Arrays.asList(0, 3, 6, 5, 0, 3, 9, 6, 1, 5, 6, 1, 0, 3,
				2, 3, 8, 1, 5, 0, 8, 7, 1, 5, 9, 5, 2, 4, 5, 5, 8, 4, 0, 8, 5,
				7, 2, 5, 9, 9, 9, 7, 8, 9, 7, 4, 0, 8, 7, 3));
		fixedLists.add(Arrays.asList(9, 9, 8, 1, 6, 8, 3, 7, 6, 3, 4, 4, 4, 0,
				2, 9, 3, 3, 4, 5, 2, 5, 1, 9, 1, 6, 6, 6, 9, 1, 5, 4, 3, 5, 5,
				8, 2, 0, 8, 9, 4, 7, 6, 3, 8, 6, 5, 2, 9, 8));
		fixedLists.add(Arrays.asList(9, 6, 5, 5, 8, 8, 3, 3, 9, 1, 9, 3, 8, 1,
				5, 3, 8, 4, 8, 5, 6, 2, 5, 9, 2, 1, 5, 5, 4, 4, 8, 5, 1, 3, 5,
				8, 8, 1, 9, 4, 6, 9, 4, 8, 5, 7, 1, 4, 6, 7));
		this.hiddenMarkovModel.init(fixedLists.get(0));
		this.hiddenMarkovModel.init(fixedLists.get(1));
		this.hiddenMarkovModel.init(fixedLists.get(2));
	}

	@Test
	public void benchmarkGetProbability() {
		this.hiddenMarkovModel.p(Arrays.asList(5, 10));
		this.hiddenMarkovModel.p(fixedLists.get(0));
	}
}
