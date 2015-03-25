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
import de.nilsreiter.pipeline.segmentation.wc.type.TimeAdverb;
import de.nilsreiter.pipeline.segmentation.wc.type.TimeNoun;
import de.nilsreiter.pipeline.tense.type.Aspect;
import de.nilsreiter.pipeline.tense.type.Tense;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

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

			List<Sentence> previousSentences =
					JCasUtil.selectPreceding(jcas, Sentence.class, candidate, 1);
			Sentence previousSentence = null;
			if (!previousSentences.isEmpty())
				previousSentence = previousSentences.get(0);

			// previous tense
			if (previousSentence != null) {
				Collection<Tense> prec =
						JCasUtil.selectCovered(jcas, Tense.class,
								previousSentence);
				if (prec.size() == 1)
					fv.setPreviousTense(prec.iterator().next().getTense());
			}
			// current tense
			List<Tense> tenses =
					JCasUtil.selectCovered(jcas, Tense.class, candidate);
			if (!tenses.isEmpty())
				fv.setCurrentTense(tenses.get(0).getTense());

			List<Aspect> aspects;
			// previous aspect
			if (previousSentence != null) {

				aspects =
						JCasUtil.selectCovered(jcas, Aspect.class,
								previousSentence);

				if (aspects.size() == 1)
					fv.setPreviousAspect(aspects.get(0).getAspect());
			}
			// current aspect
			aspects = JCasUtil.selectCovered(jcas, Aspect.class, candidate);
			if (!aspects.isEmpty())
				fv.setCurrentAspect(aspects.get(0).getAspect());

			// time adverb
			List<TimeAdverb> timeAdverbs =
					JCasUtil.selectCovered(jcas, TimeAdverb.class, candidate);
			if (!timeAdverbs.isEmpty()) {
				fv.setTimeAdverb(timeAdverbs.get(0).getCoveredText());
			}

			// time noun
			List<TimeNoun> timeNouns =
					JCasUtil.selectCovered(jcas, TimeNoun.class, candidate);
			if (!timeNouns.isEmpty())
				fv.setTimeNoun(timeNouns.get(0).getCoveredText());
		}
	}
}
