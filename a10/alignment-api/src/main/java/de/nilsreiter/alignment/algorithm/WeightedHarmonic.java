package de.nilsreiter.alignment.algorithm;

import de.uniheidelberg.cl.a10.data2.HasDocument;

public interface WeightedHarmonic<T extends HasDocument> extends Harmonic<T>,
		WeightedBaseline<T> {

}
