package de.nilsreiter.pipeline.tense;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.Pair;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.clauselevel.EnglishTense;
import de.nilsreiter.pipeline.tense.type.Tense;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.uniheidelberg.cl.reiter.util.Counter;

public class TenseAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			Counter<EnglishTense> tc = new Counter<EnglishTense>();

			List<List<POS>> posPatterns = new LinkedList<List<POS>>();
			List<POS> poss = new LinkedList<POS>();
			for (POS pos : JCasUtil.selectCovered(jcas, POS.class, sentence)) {
				if (pos.getPosValue().startsWith("V")
						|| pos.getPosValue().startsWith("M")) {
					poss.add(pos);
				} else {
					posPatterns.add(poss);
					poss = new LinkedList<POS>();
				}
			}

			for (List<POS> l : posPatterns) {
				EnglishTense t = getTense(l);
				if (t != null) tc.add(t);
			}
			Pair<Integer, Set<EnglishTense>> res = tc.getMax();
			if (res.getSecond().size() == 1) {
				Tense tenseAnnotation =
						AnnotationFactory.createAnnotation(jcas,
								sentence.getBegin(), sentence.getEnd(),
								Tense.class);
				tenseAnnotation.setTense(res.getSecond().iterator().next()
						.toString());
			}
		}
	}

	EnglishTense getTense(List<POS> posList) {
		if (posList.isEmpty()) return null;
		String posValue = posList.get(posList.size() - 1).getPosValue();

		if (posList.size() == 1) {
			if (posValue.matches("^VB[PZ]$"))
				return EnglishTense.Simple_Present;
			if (posValue.matches("^VBD$")) return EnglishTense.Simple_Past;
		} else if (posList.size() == 2) {
			String auxPosValue = posList.get(0).getPosValue();
			// String auxSurface = aux.get(0).getToken().getCoveredText();
			if (posValue.matches("^VBG$")) {
				if (auxPosValue.matches("^VB[PZ]$")) {
					/*
					 * if (dr.getCoveredText().matches("going")) { DepRel xcomp
					 * = ClauseUtil.getDependent(dr, "xcomp"); if (xcomp !=
					 * null) { return EnglishTense.going_to_Future; } }
					 */
					return EnglishTense.Present_Progressive;
				}
				if (auxPosValue.matches("VBD"))
					return EnglishTense.Past_Progressive;
			} else if (posValue.matches("^VBN$")) {
				if (auxPosValue.matches("^VB[PZ]$"))
					return EnglishTense.Present_Perfect;
				if (auxPosValue.matches("^VBD$"))
					return EnglishTense.Simple_Past_Perfect;
			} else if (posValue.matches("^VB$")) {
				if (auxPosValue.matches("MD")) return EnglishTense.Will_Future;
			}
		} else if (posList.size() == 3) {
			String aux0PosValue = posList.get(0).getPosValue();
			String aux1PosValue = posList.get(1).getPosValue();
			if (posValue.matches("^VBG$")) {
				if (aux0PosValue.matches("^VB[PZ]$")
						&& aux1PosValue.matches("^VBN$"))
					return EnglishTense.Present_Perfect_Progressive;
				if (aux0PosValue.matches("^VBD$")
						&& aux1PosValue.matches("^VBN$"))
					return EnglishTense.Past_Perfect_Progressive;
				if (aux0PosValue.matches("^MD$")
						&& aux1PosValue.matches("^VB$"))
					return EnglishTense.Future_Progressive;
			} else if (posValue.matches("^VBN$")) {
				if (aux0PosValue.matches("^MD$")
						&& aux1PosValue.matches("^VB$"))
					return EnglishTense.Simple_Future_Perfect;
			}
		} else if (posList.size() == 3) {
			if (posValue.matches("^VBG$")) {
				if (posList.get(0).getPosValue().matches("^MD$")
						&& posList.get(1).getPosValue().matches("^VB$")
						&& posList.get(2).getPosValue().matches("^VBN$"))
					return EnglishTense.Future_Perfect_Progressive;
			}
		}

		return EnglishTense.Unknown;
	}

}
