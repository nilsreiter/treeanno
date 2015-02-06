package de.nilsreiter.pipeline.segmentation.topicmodeling;

import java.net.URI;
import java.util.Iterator;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import cc.mallet.types.Instance;
import de.nilsreiter.pipeline.segmentation.type.SegmentationSubUnit;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;

public class SegmentationUnitIterator implements Iterator<Instance> {
	JCas jcas;
	int index = 0;

	Iterator<SegmentationUnit> baseIterator;

	public SegmentationUnitIterator(JCas aJCas,
			Iterator<SegmentationUnit> bIterator) {
		baseIterator = bIterator;
		jcas = aJCas;
	}

	public boolean hasNext() {
		return baseIterator.hasNext();
	}

	public Instance next() {
		SegmentationUnit su = baseIterator.next();
		StringBuilder b = new StringBuilder();
		for (SegmentationSubUnit sub : JCasUtil.selectCovered(jcas,
				SegmentationSubUnit.class, su)) {
			b.append(sub.getCoveredText());
			b.append(" ");
		}
		URI uri = null;
		try {
			uri = new URI("iterator:" + index);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
		return new Instance(b.toString(), null, uri, null);
	}
}