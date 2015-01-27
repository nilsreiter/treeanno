package de.nilsreiter.pipeline.segmentation;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.Segment;

@TypeCapability(inputs = {})
public class SegmentMerger extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Set<Segment> toRemove = new HashSet<Segment>();
		Set<Segment> toAdd = new HashSet<Segment>();
		Segment prev = null;
		Segment begin = null;
		for (Segment segment : JCasUtil.select(jcas, Segment.class)) {
			if (prev != null) {
				if (!segment.getValue().equalsIgnoreCase(prev.getValue())) {
					Segment nSeg = new Segment(jcas);
					nSeg.setBegin(begin.getBegin());
					nSeg.setEnd(prev.getEnd());
					toAdd.add(nSeg);
					begin = segment;
				}
			}
			if (begin == null) begin = segment;
			toRemove.add(segment);
			prev = segment;
		}

		Segment nSeg = new Segment(jcas);
		nSeg.setBegin(begin.getBegin());
		nSeg.setEnd(prev.getEnd());
		toAdd.add(nSeg);

		for (Segment segment : toRemove) {
			segment.removeFromIndexes();
		}

		for (Segment segment : toAdd) {
			segment.addToIndexes();
		}
	}
}
