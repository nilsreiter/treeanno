package de.nilsreiter.pipeline.uima.entitydetection;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;

public class RelationUtil {
	public static Token getLCS(JCas jcas, Token token1, Token token2) {
		Token tok1 = token1;

		while (tok1 != null) {
			Token tok2 = token2;
			while (tok2 != null) {
				if (tok1 == tok2) return tok1;
				tok2 = getGovernor(jcas, tok2);
			}
			tok1 = getGovernor(jcas, tok1);
		}
		return null;

	}

	public static Token getGovernor(JCas jcas, Token token) {
		try {
			Dependency dep =
					JCasUtil.selectCovered(jcas, Dependency.class, token)
					.get(0);
			if (dep != null) return dep.getGovernor();
		} catch (IndexOutOfBoundsException e) {

		}

		return null;
	}
}
