package de.ustu.ims.reiter.tense.annotator;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.ustu.ims.reiter.tense.api.type.Future;
import de.ustu.ims.reiter.tense.api.type.Past;
import de.ustu.ims.reiter.tense.api.type.Present;

@TypeCapability(inputs = {
		"de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS",
		"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence" },
		outputs = { "de.nilsreiter.pipeline.tense.type.Tense" })
public class TenseAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			// Counter<ETense> tc = new Counter<ETense>();

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
				ETense t = getTense(l);
				if (t != null)
					switch (t) {
					case PAST:
						AnnotationFactory.createAnnotation(jcas,
								l.get(0).getBegin(),
								l.get(l.size() - 1).getEnd(), Past.class)
								.setTense(java.util.Objects.toString(t));
						break;
					case FUTURE:
						AnnotationFactory.createAnnotation(jcas,
								l.get(0).getBegin(),
								l.get(l.size() - 1).getEnd(), Future.class)
								.setTense(java.util.Objects.toString(t));
						break;
					default:
						AnnotationFactory.createAnnotation(jcas,
								l.get(0).getBegin(),
								l.get(l.size() - 1).getEnd(), Present.class)
								.setTense(java.util.Objects.toString(t));
					}

			}

		}
	}

	ETense getTense(List<POS> posList) {
		if (posList.isEmpty()) return null;
		String posValue = posList.get(posList.size() - 1).getPosValue();

		if (posList.size() == 1) {
			if (posValue.matches("^VB[PZ]$")) return ETense.PRESENT;
			if (posValue.matches("^VBD$")) return ETense.PAST;
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
					return ETense.PRESENT;
				}
				if (auxPosValue.matches("VBD")) return ETense.PAST;
			} else if (posValue.matches("^VBN$")) {
				if (auxPosValue.matches("^VB[PZ]$")) return ETense.PRESENT;
				if (auxPosValue.matches("^VBD$")) return ETense.PAST;
			} else if (posValue.matches("^VB$")) {
				if (auxPosValue.matches("MD")) return ETense.FUTURE;
			}
		} else if (posList.size() == 3) {
			String aux0PosValue = posList.get(0).getPosValue();
			String aux1PosValue = posList.get(1).getPosValue();
			if (posValue.matches("^VBG$")) {
				if (aux0PosValue.matches("^VB[PZ]$")
						&& aux1PosValue.matches("^VBN$"))
					return ETense.PRESENT;
				if (aux0PosValue.matches("^VBD$")
						&& aux1PosValue.matches("^VBN$")) return ETense.PAST;
				if (aux0PosValue.matches("^MD$")
						&& aux1PosValue.matches("^VB$")) return ETense.FUTURE;
			} else if (posValue.matches("^VBN$")) {
				if (aux0PosValue.matches("^MD$")
						&& aux1PosValue.matches("^VB$")) return ETense.FUTURE;
			}
		} else if (posList.size() == 3) {
			if (posValue.matches("^VBG$")) {
				if (posList.get(0).getPosValue().matches("^MD$")
						&& posList.get(1).getPosValue().matches("^VB$")
						&& posList.get(2).getPosValue().matches("^VBN$"))
					return ETense.FUTURE;
			}
		}

		return null;
	}

}
