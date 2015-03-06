package de.nilsreiter.pipeline.segmentation.v1;

import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryCandidate;
import de.nilsreiter.pipeline.segmentation.type.v1.FeatureVector;
import de.nilsreiter.pipeline.tense.type.Tense;

public class FeatureExtractor extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (SegmentBoundaryCandidate candidate : JCasUtil.select(jcas,
				SegmentBoundaryCandidate.class)) {
			FeatureVector fv =
					AnnotationFactory.createAnnotation(jcas,
							candidate.getBegin(), candidate.getEnd(),
							FeatureVector.class);
			fv.setNewSegment(JCasUtil.selectCovered(jcas,
					SegmentBoundary.class, candidate).size() > 0);

			// previous tense
			Collection<Tense> prec =
					JCasUtil.selectPreceding(Tense.class, candidate, 1);
			if (prec.size() == 1)
				fv.setPreviousTense(prec.iterator().next().getTense());

			// current tense
			List<Tense> tenses =
					JCasUtil.selectCovered(jcas, Tense.class, candidate);
			if (!tenses.isEmpty())
				fv.setCurrentTense(tenses.get(0).getTense());
		}
	}
}
