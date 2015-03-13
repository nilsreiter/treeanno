package de.nilsreiter.pipeline.segmentation;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundaryCandidate;

public class BoundaryCandidateAnnotator extends JCasAnnotator_ImplBase {

	public final static String PARAM_BASE_ANNOTATION = "Base Annotation";

	@ConfigurationParameter(
			name = PARAM_BASE_ANNOTATION,
			mandatory = false,
			defaultValue = "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence")
	String baseAnnotationType =
			"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence";

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		try {
			Class<? extends Annotation> annoClass =
					(Class<? extends Annotation>) Class
							.forName(baseAnnotationType);
			for (Annotation sentence : JCasUtil.select(jcas, annoClass)) {
				AnnotationFactory.createAnnotation(jcas, sentence.getBegin(),
						sentence.getEnd(), SegmentBoundaryCandidate.class);
			}
		} catch (ClassNotFoundException e) {
			throw new AnalysisEngineProcessException(e);
		}

	}
}
