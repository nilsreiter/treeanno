package de.uniheidelberg.cl.a10.semafortraining.uima;

import java.util.SortedSet;
import java.util.TreeSet;

public abstract class SemaforAnnotation {

	protected SortedSet<Integer> targetTokens;

	public SemaforAnnotation() {
		this.targetTokens = new TreeSet<Integer>();
	}

	public abstract String getTargetIdString() throws Exception;

	public void addTargetToken(final Integer i) {
		targetTokens.add(i);
	}

	public SortedSet<Integer> getTargetTokens() {
		return targetTokens;
	}
}
