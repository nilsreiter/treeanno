package de.nilsreiter.pipeline.uima.ocr.detect;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.lm.NGramModel;
import de.nilsreiter.lm.io.ModelReader;
import de.nilsreiter.pipeline.uima.ocr.type.OCRError;
import de.nilsreiter.util.StringUtil;

public class OCRErrorDetectionLMBased extends JCasAnnotator_ImplBase {
	public static final String PARAM_LM_FILE = "Model File";
	@ConfigurationParameter(name = PARAM_LM_FILE)
	File modelFile;

	NGramModel<Character> model;

	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		ModelReader<Character> modelReader = new ModelReader<Character>();
		try {
			model = modelReader.read(modelFile);
		} catch (IOException e) {
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		char[] text = jcas.getDocumentText().toCharArray();

		int n = model.getN();

		Character[] history = new Character[n - 1];
		Arrays.fill(history, 0, history.length, '@');

		for (int i = 0; i < text.length; i++) {
			Character current = text[i];

			double prob = model.getProbability(current, history);
			if (prob < 1e-2) {
				OCRError error = new OCRError(jcas);
				error.setBegin(i);
				error.setEnd(i + 1);
				error.setDescription("P(" + current + "|"
						+ StringUtil.join(history, ",", 0) + ") = " + prob
						+ "\n" + findSubstitution(current, history, prob));
				error.addToIndexes();
			}
			add(current, history);
		}
	}

	protected char findSubstitution(Character ch, Character[] history,
			double prob) {
		for (int i = 0; i < 1256; i++) {
			double p =
					model.getProbability(Character.valueOf((char) i), history);
			if (p > prob) ch = Character.valueOf((char) i);
		}
		return ch;
	}

	public Character[] add(Character ch, Character[] arr) {
		for (int i = 1; i < arr.length; i++) {
			arr[i - 1] = arr[i];
		}
		/*
		 * for (int i = arr.length - 1; i > 0; i--) { arr[i] = arr[i - 1]; }
		 */
		arr[arr.length - 1] = ch;
		return arr;
	}
}
