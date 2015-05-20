package de.ustu.creta.segmentation.evaluation.util;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.ustu.ims.segmentation.type.SegmentBoundary;

public class SegmentBoundaryAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_ANNOTATION_TYPE = "Annotation Type";

	@ConfigurationParameter(name = PARAM_ANNOTATION_TYPE)
	String annotationTypeName = "";
	Class<? extends Annotation> annotationType;

	@SuppressWarnings("unchecked")
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		try {
			Class<?> clazz = Class.forName(annotationTypeName);
			annotationType = (Class<? extends Annotation>) clazz;
		} catch (ClassNotFoundException e) {
			throw new AnalysisEngineProcessException(e);
		}
		for (Annotation anno : JCasUtil.select(jcas, annotationType)) {
			int b = anno.getBegin();
			AnnotationFactory.createAnnotation(jcas, b, b,
					SegmentBoundary.class);
		}
	}

}
