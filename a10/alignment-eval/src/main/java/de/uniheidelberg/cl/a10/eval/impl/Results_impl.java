package de.uniheidelberg.cl.a10.eval.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniheidelberg.cl.a10.eval.Results;
import de.uniheidelberg.cl.a10.eval.SingleResult;

public class Results_impl implements Results {

	List<SingleResult> lines = new LinkedList<SingleResult>();

	TreeSet<String> measures = new TreeSet<String>();

	@Override
	public List<SingleResult> getConfigurations() {
		return lines;
	}

	@Override
	public int getNumberOfMeasures() {
		return measures.size();
	}

	public int getNumberOfSingleResults() {
		return lines.size();
	}

	@Override
	public SortedSet<String> getMeasures() {
		return measures;
	}

	public void addResult(final SingleResult sr) {
		lines.add(sr);
		measures.addAll(sr.getMeasures());
	}

}
