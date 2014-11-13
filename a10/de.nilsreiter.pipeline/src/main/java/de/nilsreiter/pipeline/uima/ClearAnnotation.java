package de.nilsreiter.pipeline.uima;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class ClearAnnotation extends JCasAnnotator_ImplBase {

	public static final String PARAM_TYPE = "Type to Remove";

	@ConfigurationParameter(name = PARAM_TYPE)
	String type = null;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		try {
			Class<? extends Annotation> cl =
					(Class<? extends Annotation>) Class.forName(type);

			List<Annotation> annList =
					new LinkedList<Annotation>(JCasUtil.select(jcas, cl));
			for (Annotation anno : annList) {
				anno.removeFromIndexes();
			}
		} catch (ClassNotFoundException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

}
