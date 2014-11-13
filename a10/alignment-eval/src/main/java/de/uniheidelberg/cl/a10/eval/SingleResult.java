package de.uniheidelberg.cl.a10.eval;

import java.util.SortedSet;

public interface SingleResult {
	String name();

	void setName(String n);

	double getScore(String measureName);

	void setScore(final String measureName, final double score);

	SortedSet<String> getMeasures();

	double p();

	double r();

	Object getIdentifier();
}
