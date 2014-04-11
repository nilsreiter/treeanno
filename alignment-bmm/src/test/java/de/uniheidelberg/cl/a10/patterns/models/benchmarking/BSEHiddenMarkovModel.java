package de.uniheidelberg.cl.a10.patterns.models.benchmarking;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;

import de.uniheidelberg.cl.a10.patterns.models.impl.SEHiddenMarkovModel_impl;

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
 * <td>3020</td>
 * <td>Baseline</td>
 * <td>0.17</td>
 * <td>21</td>
 * <td>0.21</td>
 * </tr>
 * <tr>
 * <td>3021</td>
 * <td>Now using parallelViterbi() as basis for getProbability()</td>
 * <td>0.01</td>
 * <td>0</td>
 * <td>0.0</td>
 * </tr>
 * <tr>
 * <td>3051</td>
 * <td>Now using Probability class</td>
 * <td>0.12</td>
 * <td>3</td>
 * <td>0.02</td>
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
 * <td>3020</td>
 * <td>Baseline</td>
 * <td>0.43</td>
 * <td>106</td>
 * <td>0.13</td>
 * </tr>
 * <tr>
 * <td>3051</td>
 * <td>Baseline</td>
 * <td>0.58</td>
 * <td>229</td>
 * <td>0.23</td>
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
 * <td>3020</td>
 * <td>Baseline</td>
 * <td>0.0</td>
 * <td>0</td>
 * <td>0.0</td>
 * </tr>
 * <tr>
 * <td>3051</td>
 * <td>Baseline</td>
 * <td>0.0</td>
 * <td>0</td>
 * <td>0.0</td>
 * </tr>
 * </table>
 * 
 * @author reiter
 * 
 */
public class BSEHiddenMarkovModel {

	List<List<Integer>> fixedLists = null;

	SEHiddenMarkovModel_impl<Integer> hiddenMarkovModel;

	@Rule
	public BenchmarkRule benchmarkRun = new BenchmarkRule();

	@Before
	public void setUp() throws Exception {
		hiddenMarkovModel = new SEHiddenMarkovModel_impl<Integer>();
		hiddenMarkovModel.setStartSymbol(-1);
		hiddenMarkovModel.setEndSymbol(-2);
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
