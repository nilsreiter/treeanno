package de.uniheidelberg.cl.a10.cluster.measure;

import de.uniheidelberg.cl.a10.cluster.measure.impl.One_impl;
import de.uniheidelberg.cl.a10.cluster.measure.impl.Three_impl;
import de.uniheidelberg.cl.a10.cluster.measure.impl.Two_impl;

public class DocumentSimilarityMeasureFactory {
	public DocumentSimilarityMeasure getDocumentSimilarityMeasure(
			final DocumentSimilarityMeasure.Type type) {
		switch (type) {
		case THREE:
			return new Three_impl();
		case TWO:
			return new Two_impl();
		default:
			return new One_impl();
		}
	}
}
