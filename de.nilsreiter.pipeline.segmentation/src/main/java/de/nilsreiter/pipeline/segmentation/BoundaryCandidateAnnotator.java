package de.nilsreiter.pipeline.segmentation;

import java.util.LinkedList;
import java.util.List;

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
		List<Sentence> sentenceList =
				new LinkedList<Sentence>(JCasUtil.select(jcas, Sentence.class));

		for (int i = 0; i < sentenceList.size(); i++) {
			Sentence sentence = sentenceList.get(i);
			AnnotationFactory.createAnnotation(jcas, sentence.getEnd(),
					sentence.getEnd(), SegmentBoundaryCandidate.class);
		}
	}
}
