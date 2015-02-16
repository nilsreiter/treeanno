package de.nilsreiter.pipeline.segmentation.clauselevel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;

public class TreeMaker extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			Map<Token, DepRel> tokenMap = new HashMap<Token, DepRel>();
			Map<DepRel, List<DepRel>> tokenDependents =
					new HashMap<DepRel, List<DepRel>>();
			for (Dependency dep : JCasUtil.selectCovered(jcas,
					Dependency.class, sentence)) {
				Token token = dep.getDependent();
				Token govToken = dep.getGovernor();
				DepRel dr = getToken(jcas, tokenMap, token);
				dr.setToken(token);
				dr.setRelation(dep.getDependencyType());
				DepRel governor = getToken(jcas, tokenMap, govToken);
				dr.setGovenor(governor);
				if (!tokenDependents.containsKey(governor))
					tokenDependents.put(governor, new LinkedList<DepRel>());
				tokenDependents.get(governor).add(dr);
			}

			for (DepRel gov : tokenDependents.keySet()) {
				List<DepRel> lis = tokenDependents.get(gov);
				gov.setDependents(new FSArray(jcas, lis.size()));
				for (int i = 0; i < lis.size(); i++)
					gov.setDependents(i, lis.get(i));
			}
		}
	}

	private DepRel getToken(JCas jcas, Map<Token, DepRel> map, Token token) {
		if (map.containsKey(token)) return map.get(token);
		DepRel dr =
				AnnotationFactory.createAnnotation(jcas, token.getBegin(),
						token.getEnd(), DepRel.class);
		dr.setToken(token);
		map.put(token, dr);
		return dr;
	}

}
