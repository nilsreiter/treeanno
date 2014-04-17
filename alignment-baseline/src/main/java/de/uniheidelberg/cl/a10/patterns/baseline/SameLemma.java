package de.uniheidelberg.cl.a10.patterns.baseline;

import de.uniheidelberg.cl.a10.HasTarget;
import de.uniheidelberg.cl.a10.data2.HasDocument;

public interface SameLemma<T extends HasTarget & HasDocument> extends
		Baseline<T> {

}
