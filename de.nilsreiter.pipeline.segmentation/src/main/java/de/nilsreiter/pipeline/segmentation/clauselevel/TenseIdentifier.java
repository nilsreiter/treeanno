package de.nilsreiter.pipeline.segmentation.clauselevel;

import java.util.Collection;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.clauselevel.type.Clause;
import de.nilsreiter.pipeline.segmentation.clauselevel.type.DepRel;

public class TenseIdentifier extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		for (Clause clause : JCasUtil.select(aJCas, Clause.class)) {
			Collection<DepRel> coll =
					JCasUtil.selectCovered(aJCas, DepRel.class, clause);
			if (!coll.isEmpty())
				clause.setTense(getTense(coll.iterator().next()).toString());
		}
	}

	protected EnglishTense getTense(DepRel dr) {
		List<DepRel> aux = ClauseUtil.getDependents(dr, "aux");
		String posValue = dr.getToken().getPos().getPosValue();
		if (aux.isEmpty()) {
			if (posValue.matches("^VB[PZ]$")) return EnglishTense.Simple_Present;
			if (posValue.matches("^VBD$")) return EnglishTense.Simple_Past;
		} else if (aux.size() == 1) {
			String auxPosValue = aux.get(0).getToken().getPos().getPosValue();
			// String auxSurface = aux.get(0).getToken().getCoveredText();
			if (posValue.matches("^VBG$")) {
				if (auxPosValue.matches("^VB[PZ]$")) {
					if (dr.getCoveredText().matches("going")) {
						DepRel xcomp = ClauseUtil.getDependent(dr, "xcomp");
						if (xcomp != null) {
							return EnglishTense.going_to_Future;
						}
					}
					return EnglishTense.Present_Progressive;
				}
				if (auxPosValue.matches("VBD")) return EnglishTense.Past_Progressive;
			} else if (posValue.matches("^VBN$")) {
				if (auxPosValue.matches("^VB[PZ]$"))
					return EnglishTense.Present_Perfect;
				if (auxPosValue.matches("^VBD$"))
					return EnglishTense.Simple_Past_Perfect;
			} else if (posValue.matches("^VB$")) {
				if (auxPosValue.matches("MD")) return EnglishTense.Will_Future;
			}
		} else if (aux.size() == 2) {
			String aux0PosValue = aux.get(0).getToken().getPos().getPosValue();
			String aux1PosValue = aux.get(1).getToken().getPos().getPosValue();
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
		} else if (aux.size() == 3) {
			if (posValue.matches("^VBG$")) {
				if (aux.get(0).getToken().getPos().getPosValue()
						.matches("^MD$")
						&& aux.get(1).getToken().getPos().getPosValue()
						.matches("^VB$")
						&& aux.get(2).getToken().getPos().getPosValue()
						.matches("^VBN$"))
					return EnglishTense.Future_Perfect_Progressive;
			}
		}
		return EnglishTense.Unknown;
	}
}
