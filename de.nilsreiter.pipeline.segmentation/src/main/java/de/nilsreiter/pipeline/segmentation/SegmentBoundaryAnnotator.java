package de.nilsreiter.pipeline.segmentation;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;

public class SegmentBoundaryAnnotator extends JCasAnnotator_ImplBase {
	public final static String PARAM_BASE_ANNOTATION = "Segment Annotation";

	@ConfigurationParameter(name = PARAM_BASE_ANNOTATION, mandatory = false,
			defaultValue = "de.nilsreiter.pipeline.segmentation.type.Segment")
	String baseAnnotationType = Segment.class.getCanonicalName();

	Class<? extends Annotation> annotationType;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		try {
			Class<?> clazz = Class.forName(baseAnnotationType);
			annotationType = (Class<? extends Annotation>) clazz;
		} catch (ClassNotFoundException e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		int end = 0;
		for (Annotation anno : JCasUtil.select(jcas, annotationType)) {
			AnnotationFactory.createAnnotation(jcas, anno.getBegin(),
					anno.getBegin(), SegmentBoundary.class);
			end = anno.getEnd();
		}
		AnnotationFactory.createAnnotation(jcas, end, end,
				SegmentBoundary.class);
	}
}
