package de.nilsreiter.pipeline.segmentation.infinitejest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;

public class IJEvalPreparer extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		Set<Segment> toRemove = new HashSet<Segment>();
		Set<Segment> toAdd = new HashSet<Segment>();
		toRemove.addAll(JCasUtil.select(jcas, Segment.class));

		for (Paragraph para : JCasUtil.select(jcas, Paragraph.class)) {
			Collection<Segment> segments =
					JCasUtil.selectCovering(jcas, Segment.class, para);
			Segment nSegment = new Segment(jcas);
			nSegment.setBegin(para.getBegin());
			nSegment.setEnd(para.getEnd());
			if (segments.isEmpty()) {
				nSegment.setValue(null);
			} else {
				StringBuilder b = new StringBuilder();

				for (Segment seg : segments) {
					b.append(seg.getValue());
					b.append(',');
				}
				String s = b.toString().substring(0, b.length() - 1);
				getLogger().debug("Assigning value " + s + " to paragraph.");
				nSegment.setValue(s);
			}
			toAdd.add(nSegment);
		}

		/*
		 * for (Segment segment : JCasUtil.select(jcas, Segment.class)) { for
		 * (Paragraph para : JCasUtil.selectCovered(jcas, Paragraph.class,
		 * segment)) { Segment nSegment = new Segment(jcas);
		 * nSegment.setBegin(para.getBegin()); nSegment.setEnd(para.getEnd());
		 * nSegment.setValue(segment.getValue()); toAdd.add(nSegment); } }
		 */

		for (Segment seg : toRemove) {
			seg.removeFromIndexes();
		}

		for (Segment seg : toAdd) {
			seg.addToIndexes();
		}
	}
}
