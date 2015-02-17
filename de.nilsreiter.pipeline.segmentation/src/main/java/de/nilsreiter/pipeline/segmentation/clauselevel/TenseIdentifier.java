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

	/**
	 * source: http://www.englisch-hilfen.de/en/grammar/english_tenses.htm
	 * 
	 * @author reiterns
	 *
	 */
	enum Tense {
		/**
		 * base form
		 */
		Simple_Present,
		/**
		 * to be + infinitive-ing
		 */
		Present_Progressive,
		/**
		 * to have + past participle
		 */
		Present_Perfect,
		/**
		 * to have + been + infinitive-ing
		 */
		Present_Perfect_Progressive,

		/**
		 * infinitive-ed
		 */
		Simple_Past,
		/**
		 * had + part participle
		 */
		Simple_Past_Perfect, Past_Progressive, Past_Perfect_Progressive,
		Will_Future, going_to_Future, Unknown,
		/**
		 * will + be + infinitive + ing
		 */
		Future_Progressive,
		/**
		 * will + have + past participle
		 */
		Simple_Future_Perfect,
		/**
		 * will + have + been + infinitive + ing
		 */
		Future_Perfect_Progressive
	};

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		for (Clause clause : JCasUtil.select(aJCas, Clause.class)) {
			Collection<DepRel> coll =
					JCasUtil.selectCovered(aJCas, DepRel.class, clause);
			if (!coll.isEmpty())
				clause.setTense(getTense(coll.iterator().next()).toString());
		}
	}

	protected Tense getTense(DepRel dr) {
		List<DepRel> aux = ClauseUtil.getDependents(dr, "aux");
		String posValue = dr.getToken().getPos().getPosValue();
		if (aux.isEmpty()) {
			if (posValue.matches("^VB[PZ]$")) return Tense.Simple_Present;
			if (posValue.matches("^VBD$")) return Tense.Simple_Past;
		} else if (aux.size() == 1) {
			String auxPosValue = aux.get(0).getToken().getPos().getPosValue();
			// String auxSurface = aux.get(0).getToken().getCoveredText();
			if (posValue.matches("^VBG$")) {
				if (auxPosValue.matches("^VB[PZ]$")) {
					if (dr.getCoveredText().matches("going")) {
						DepRel xcomp = ClauseUtil.getDependent(dr, "xcomp");
						if (xcomp != null) {
							return Tense.going_to_Future;
						}
					}
					return Tense.Present_Progressive;
				}
				if (auxPosValue.matches("VBD")) return Tense.Past_Progressive;
			} else if (posValue.matches("^VBN$")) {
				if (auxPosValue.matches("^VB[PZ]$"))
					return Tense.Present_Perfect;
				if (auxPosValue.matches("^VBD$"))
					return Tense.Simple_Past_Perfect;
			} else if (posValue.matches("^VB$")) {
				if (auxPosValue.matches("MD")) return Tense.Will_Future;
			}
		} else if (aux.size() == 2) {
			String aux0PosValue = aux.get(0).getToken().getPos().getPosValue();
			String aux1PosValue = aux.get(1).getToken().getPos().getPosValue();
			if (posValue.matches("^VBG$")) {
				if (aux0PosValue.matches("^VB[PZ]$")
						&& aux1PosValue.matches("^VBN$"))
					return Tense.Present_Perfect_Progressive;
				if (aux0PosValue.matches("^VBD$")
						&& aux1PosValue.matches("^VBN$"))
					return Tense.Past_Perfect_Progressive;
				if (aux0PosValue.matches("^MD$")
						&& aux1PosValue.matches("^VB$"))
					return Tense.Future_Progressive;
			} else if (posValue.matches("^VBN$")) {
				if (aux0PosValue.matches("^MD$")
						&& aux1PosValue.matches("^VB$"))
					return Tense.Simple_Future_Perfect;
			}
		} else if (aux.size() == 3) {
			if (posValue.matches("^VBG$")) {
				if (aux.get(0).getToken().getPos().getPosValue()
						.matches("^MD$")
						&& aux.get(1).getToken().getPos().getPosValue()
						.matches("^VB$")
						&& aux.get(2).getToken().getPos().getPosValue()
						.matches("^VBN$"))
					return Tense.Future_Perfect_Progressive;
			}
		}
		return Tense.Unknown;
	}
}
