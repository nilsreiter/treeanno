package de.nilsreiter.pipeline.segmentation;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryCandidate;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

public class BoundaryCandidateAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			AnnotationFactory.createAnnotation(jcas, sentence.getBegin(),
					sentence.getEnd(), SegmentBoundaryCandidate.class);
		}
	}
}
