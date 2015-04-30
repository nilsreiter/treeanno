package de.nilsreiter.pipeline.segmentation.annotation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel1;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel2;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryLevel3;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

@TypeCapability(inputs = {}, outputs = {})
public class AnnotationCleanerV2 extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		for (Token token : JCasUtil.select(jcas, Token.class)) {
			Collection<SegmentBoundaryLevel3> coll =
					JCasUtil.selectCovered(jcas, SegmentBoundaryLevel3.class,
							token);
			if (coll.size() > 1) {
				filterAnnotations(coll);
			}
		}

	}

	private void filterAnnotations(Collection<SegmentBoundaryLevel3> coll) {
		Set<Annotation> toRemove = new HashSet<Annotation>();
		SegmentBoundaryLevel3 keep = null;
		for (SegmentBoundaryLevel3 sb : coll) {
			if (keep == null)
				keep = sb;
			else {
				if (sb.getClass().equals(SegmentBoundaryLevel2.class)
						&& keep.getClass().equals(SegmentBoundaryLevel3.class)) {
					toRemove.add(keep);
					keep = sb;
				} else if (sb.getClass().equals(SegmentBoundaryLevel1.class)
						&& keep.getClass().equals(SegmentBoundaryLevel3.class)) {
					toRemove.add(keep);
					keep = sb;
				} else if (sb.getClass().equals(SegmentBoundaryLevel1.class)
						&& keep.getClass().equals(SegmentBoundaryLevel2.class)) {
					toRemove.add(keep);
					keep = sb;
				} else {
					toRemove.add(sb);
				}
			}
		}
		for (Annotation anno : toRemove) {
			anno.removeFromIndexes();
		}
	}
}
