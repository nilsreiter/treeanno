package de.nilsreiter.pipeline.segmentation.clauselevel;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause;
import de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * Identifies clauses by looking at some dependency relations
 * 
 * @author reiterns
 *
 */
public class PrepareClauseAnnotations extends JCasAnnotator_ImplBase {
	String patternDependencyType = "^(root|ccomp|advcl)$";

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		for (DepRel dr : JCasUtil.select(aJCas, DepRel.class)) {
			if (dr.getRelation().matches(patternDependencyType)) {
				Clause clause =
						AnnotationFactory.createAnnotation(aJCas,
								dr.getBegin(), dr.getEnd(), Clause.class);

				List<Token> extentList = getDependentTokens(dr);
				FSArray arr = new FSArray(aJCas, extentList.size());
				for (int i = 0; i < extentList.size(); i++) {
					arr.set(i, extentList.get(i));
				}
				arr.addToIndexes();
				clause.setExtent(arr);
				clause.setHead(dr);
			}
		}
	}

	List<Token> getDependentTokens(DepRel dr) {
		List<Token> tokList = new LinkedList<Token>();
		if (dr.getDependents() != null)
			for (int i = 0; i < dr.getDependents().size(); i++) {
				DepRel dtr = dr.getDependents(i);
				if (!dtr.getRelation().matches(patternDependencyType)) {
					tokList.add(dtr.getToken());
					tokList.addAll(getDependentTokens(dtr));
				}
			}
		return tokList;
	}
}
