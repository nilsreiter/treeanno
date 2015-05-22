package de.ustu.ims.reiter.tense.annotator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.ustu.ims.reiter.tense.api.type.Future;
import de.ustu.ims.reiter.tense.api.type.Past;
import de.ustu.ims.reiter.tense.api.type.Present;
import de.ustu.ims.reiter.tense.api.type.Tense;

@TypeCapability(inputs = {
		"de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS",
		"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence" },
		outputs = { "de.nilsreiter.pipeline.tense.type.Tense" })
public class TenseAnnotator extends JCasAnnotator_ImplBase {

	Map<String, Class<? extends Tense>> tenseMapping =
			new HashMap<String, Class<? extends Tense>>();

	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);

		tenseMapping.put("VBP", Present.class);
		tenseMapping.put("VBZ", Present.class);
		tenseMapping.put("VBD", Past.class);
		tenseMapping.put("VBZ VBG", Present.class);
		tenseMapping.put("VBP VBG", Present.class);
		tenseMapping.put("VBD VBG", Past.class);
		tenseMapping.put("VBZ VBN", Present.class);
		tenseMapping.put("VBP VBN", Present.class);
		tenseMapping.put("VBD VBN", Past.class);
		tenseMapping.put("MD VB", Future.class);
		tenseMapping.put("VBZ VBN VBG", Present.class);
		tenseMapping.put("VBP VBN VBG", Present.class);
		tenseMapping.put("VBD VBN VBG", Past.class);
		tenseMapping.put("MD VB VBG", Future.class);
		tenseMapping.put("MD VB VBN", Future.class);
		tenseMapping.put("MD VB VBN VBG", Future.class);
	}

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
				if (!l.isEmpty()) {
					Class<? extends Tense> tenseClass = getTenseClass(l);
					AnnotationFactory.createAnnotation(jcas, l.get(0)
							.getBegin(), l.get(l.size() - 1).getEnd(),
							tenseClass);
				}

			}

		}
	}

	Class<? extends Tense> getTenseClass(List<POS> posList) {
		if (posList.isEmpty()) return null;
		StringBuilder b = new StringBuilder();
		for (POS pos : posList) {
			b.append(pos.getPosValue());
			b.append(" ");
		}
		if (tenseMapping.containsKey(b.toString().trim()))
			return tenseMapping.get(b.toString().trim());
		return Tense.class;
	}

	@Deprecated
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
