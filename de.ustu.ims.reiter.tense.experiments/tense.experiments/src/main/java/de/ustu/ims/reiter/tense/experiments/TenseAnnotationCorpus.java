package de.ustu.ims.reiter.tense.experiments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.util.Pair;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.uima.ClearAnnotation;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.ustu.ims.reiter.tense.api.type.Aspect;
import de.ustu.ims.reiter.tense.api.type.Future;
import de.ustu.ims.reiter.tense.api.type.Past;
import de.ustu.ims.reiter.tense.api.type.Perfective;
import de.ustu.ims.reiter.tense.api.type.PerfectiveProgressive;
import de.ustu.ims.reiter.tense.api.type.Present;
import de.ustu.ims.reiter.tense.api.type.Progressive;
import de.ustu.ims.reiter.tense.api.type.Tense;

public class TenseAnnotationCorpus {

	public static void main(String[] args) throws IOException, UIMAException {
		File inputFile =
				new File(
						"/Users/reiterns/Documents/Tense/tense-annotation/CorpusAnnotatedTenseVoice-Partial.txt");

		BufferedReader br =
				new BufferedReader(new InputStreamReader(new FileInputStream(
						inputFile)));

		String line;
		int mode = 0;
		String[] lp;
		int counter = 0;
		int documentCounter = 0;
		JCas jcas = JCasFactory.createJCas();
		jcas.setDocumentLanguage("en");
		JCasBuilder b = new JCasBuilder(jcas);
		Map<Integer, Token> tokenMap = new HashMap<Integer, Token>();
		do {
			line = br.readLine();

			if (line != null && line.isEmpty()) {
				mode = 0;
				tokenMap = new HashMap<Integer, Token>();
				counter++;
				if (counter % 500 == 0) {
					b.add(0, DocumentMetaData.class).setDocumentId(
							"CorpusAnnotatedTenseVoice-Partial.txt"
									+ documentCounter++);
					b.close();
					SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
							.createEngineDescription(XmiWriter.class,
									XmiWriter.PARAM_TARGET_LOCATION,
									"target/main/resources/gold/"));
					SimplePipeline
							.runPipeline(
									jcas,
									AnalysisEngineFactory
											.createEngineDescription(
													ClearAnnotation.class,
													ClearAnnotation.PARAM_TYPE,
													de.ustu.ims.reiter.tense.api.type.Tense.class),
									AnalysisEngineFactory
											.createEngineDescription(
													ClearAnnotation.class,
													ClearAnnotation.PARAM_TYPE,
													de.ustu.ims.reiter.tense.api.type.Aspect.class),

									AnalysisEngineFactory
											.createEngineDescription(
													XmiWriter.class,
													XmiWriter.PARAM_TARGET_LOCATION,
													"target/main/resources/plain/"));
					jcas = null;
					jcas = JCasFactory.createJCas();
					jcas.setDocumentLanguage("en");

					b = new JCasBuilder(jcas);

				}
			} else if (line != null) {
				switch (mode) {
				case 0:
					lp = line.split("[ \t]");
					int s_start = b.getPosition();
					for (int j = 0; j < lp.length; j++) {
						tokenMap.put(j + 1, b.add(lp[j], Token.class));
						b.add(" ");
					}
					b.add(s_start, Sentence.class);
					b.add("\n\n");

					// /sentences.add(sent);
					break;
				case 1:
					break;
				default:
					lp = line.split("\t");
					Pair<Integer, Integer> range;
					int tenseIndex = 2;
					String[] pos = lp[0].split(" ");
					if (pos.length == 1)
						range =
								new Pair<Integer, Integer>(
										Integer.valueOf(pos[0]),
										Integer.parseInt(pos[0]));
					else
						range =
								new Pair<Integer, Integer>(
										Integer.valueOf(pos[0]),
										Integer.parseInt(pos[1]));
					String tense = lp[tenseIndex];
					int begin = tokenMap.get(range.getFirst()).getBegin();
					int end = tokenMap.get(range.getSecond()).getEnd();

					annotate(jcas, begin, end, tense);

				}

				mode++;
			}
		} while (line != null);

		br.close();

		b.add(0, DocumentMetaData.class).setDocumentId(
				"CorpusAnnotatedTenseVoice-Partial.txt" + documentCounter++);
		b.close();

		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION,
						"target/main/resources/gold/"));
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(ClearAnnotation.class,
						ClearAnnotation.PARAM_TYPE,
						de.ustu.ims.reiter.tense.api.type.Tense.class),
				AnalysisEngineFactory.createEngineDescription(
						ClearAnnotation.class, ClearAnnotation.PARAM_TYPE,
						de.ustu.ims.reiter.tense.api.type.Aspect.class),

				AnalysisEngineFactory.createEngineDescription(XmiWriter.class,
										XmiWriter.PARAM_TARGET_LOCATION,
										"target/main/resources/plain/"));
	}

	public static void annotate(JCas jcas, int begin, int end, String tense) {

		String[] tenseAsp =
				new String[] { "pres", "pres_perf", "pres_cont", "sim_past",
						"past_perf", "past_cont", "infinitiv", "fut",
						"past_perf_cont", "pres_perf_cont", "fut_perf_cont",
				"fut_perf", "fut_cont" };
		Class<? extends Aspect>[] aspects =
				new Class[] { Aspect.class, Perfective.class,
				Progressive.class, Aspect.class, Perfective.class,
				Progressive.class, Aspect.class, Aspect.class,
						PerfectiveProgressive.class,
				PerfectiveProgressive.class,
						PerfectiveProgressive.class, Perfective.class,
						Progressive.class };
		Class<? extends Tense>[] tenses =
				new Class[] { Present.class, Present.class, Present.class,
						Past.class, Past.class, Past.class, Tense.class,
						Future.class, Past.class, Present.class, Future.class,
				Future.class, Future.class };

		Class<? extends Tense> annoClassTense = null;
		Class<? extends Aspect> annoClassAspect = null;

		int ind = ArrayUtils.indexOf(tenseAsp, tense);
		if (ind > 0) {
			annoClassTense = tenses[ind];
			annoClassAspect = aspects[ind];
		}

		if (annoClassTense != null)
			AnnotationFactory
			.createAnnotation(jcas, begin, end, annoClassTense);
		else
			AnnotationFactory.createAnnotation(jcas, begin, end, Tense.class)
					.setTense(tense);
		if (annoClassAspect != null)
			AnnotationFactory.createAnnotation(jcas, begin, end,
					annoClassAspect);
		else
			AnnotationFactory.createAnnotation(jcas, begin, end, Aspect.class)
					.setAspect(tense);
	}
}
