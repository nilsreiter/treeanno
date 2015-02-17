package de.nilsreiter.pipeline.segmentation.clauselevel;

import java.util.Collection;

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
		Will_Future, going_to_Future, Unknown
	};

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		for (DepRel dr : JCasUtil.select(aJCas, DepRel.class)) {
			Collection<Clause> coll =
					JCasUtil.selectCovered(aJCas, Clause.class, dr);
			Tense t = getTense(dr);
			for (Clause cl : coll) {
				cl.setTense(t.toString());
			}
		}
	}

	protected Tense getTense(DepRel dr) {
		DepRel aux = ClauseUtil.getDependent(dr, "aux");
		String posValue = dr.getToken().getPos().getPosValue();
		if (aux == null) {
			if (posValue.matches("^VB[PZ]$")) return Tense.Simple_Present;
		}
		return Tense.Unknown;
	}
}
