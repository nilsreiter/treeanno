package de.ustu.ims.reiter.treeanno.tools;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class MapToTreeAnnoClass extends JCasAnnotator_ImplBase {

	public static final String PARAM_CLASSNAME = "Class name";

	@ConfigurationParameter(name = PARAM_CLASSNAME)
	String className;

	Class<? extends Annotation> annoClass;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);

		Class<?> cl;
		try {
			cl = Class.forName(className);
			if (Annotation.class.isAssignableFrom(cl)) {
				annoClass = (Class<? extends Annotation>) cl;
			} else {
				throw new ResourceInitializationException();
			}
		} catch (ClassNotFoundException e) {
			throw new ResourceInitializationException(e);
		}

	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		int c = 0;
		for (Annotation anno : JCasUtil.select(jcas, annoClass)) {
			AnnotationFactory.createAnnotation(jcas, anno.getBegin(),
					anno.getEnd(), TreeSegment.class).setId(c++);
		}

	}
}