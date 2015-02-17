package de.nilsreiter.pipeline.segmentation.clauselevel;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause;
import de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel;

public class PrepareClauseAnnotations extends JCasAnnotator_ImplBase {
	String patternDependencyType = "^(root|ccomp)$";

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		for (DepRel dr : JCasUtil.select(aJCas, DepRel.class)) {
			if (dr.getRelation().matches(patternDependencyType)) {
				AnnotationFactory.createAnnotation(aJCas, dr.getBegin(),
						dr.getEnd(), Clause.class);
			}

		}
	}
}
