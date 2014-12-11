package de.nilsreiter.segmentation;

import java.io.IOException;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;

public class GoldSegmentReader extends XmiReader {
	@Override
	public void getNext(CAS cas) throws IOException, CollectionException {
		super.getNext(cas);

		JCas jcas;
		try {
			jcas = cas.getJCas();
		} catch (CASException e) {
			throw new CollectionException(e);
		}

		for (Segment segment : JCasUtil.select(jcas, Segment.class)) {
			boolean first = true;
			for (Sentence sentence : JCasUtil.selectCovered(jcas,
					Sentence.class, segment)) {
				TextClassificationUnit unit =
						new TextClassificationUnit(jcas, sentence.getBegin(),
								sentence.getEnd());
				unit.addToIndexes();

				TextClassificationOutcome outcome =
						new TextClassificationOutcome(jcas,
								sentence.getBegin(), sentence.getEnd());
				if (first) {
					outcome.setOutcome("true");
					first = false;
				} else {
					outcome.setOutcome("false");
				}
				outcome.addToIndexes();
			}
		}
	}
}
