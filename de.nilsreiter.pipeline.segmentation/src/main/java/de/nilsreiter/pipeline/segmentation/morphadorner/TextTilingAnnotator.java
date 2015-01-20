package de.nilsreiter.pipeline.segmentation.morphadorner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import edu.northwestern.at.morphadorner.corpuslinguistics.stopwords.BaseStopWords;
import edu.northwestern.at.morphadorner.corpuslinguistics.textsegmenter.struct.RawText;
import edu.northwestern.at.morphadorner.corpuslinguistics.textsegmenter.texttiling.TextTiling;

@TypeCapability(
		inputs = { "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token",
		"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence" },
		outputs = { "de.nilsreiter.pipeline.segmentation.type.SegmentBoundary" })
public class TextTilingAnnotator extends JCasAnnotator_ImplBase {

	public static final String PARAM_SEGMENTATION_BASETYPE =
			"Segmentation Base Type";
	public static final String PARAM_STEP_SIZE = "Step Size";
	public static final String PARAM_WINDOW_SIZE = "Window Size";

	@ConfigurationParameter(name = PARAM_STEP_SIZE)
	int stepSize = 10;

	@ConfigurationParameter(name = PARAM_WINDOW_SIZE)
	int windowSize = 100;

	@ConfigurationParameter(name = PARAM_SEGMENTATION_BASETYPE)
	Type type = Type.Sentence;

	enum Type {
		Token, Sentence
	};

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		List<List<String>> tokenSurfaces = new LinkedList<List<String>>();
		List<Annotation> annotationList = new LinkedList<Annotation>();

		switch (type) {
		case Token:
			for (Token token : JCasUtil.select(aJCas, Token.class)) {
				tokenSurfaces.add(Arrays.asList(token.getCoveredText()));
				annotationList.add(token);
			}
			break;
		default:
			for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) {
				List<String> sentenceList = new LinkedList<String>();
				for (Token token : JCasUtil.selectCovered(aJCas, Token.class,
						sentence)) {
					sentenceList.add(token.getCoveredText());
					annotationList.add(token);
				}
				tokenSurfaces.add(sentenceList);
			}
		}

		RawText rt = new RawText(tokenSurfaces);

		TextTiling tt = new TextTiling(rt, new BaseStopWords());
		tt.setStepSize(stepSize);
		tt.setWindowSize(windowSize);
		tt.similarityDetermination();
		tt.depthScore();
		tt.boundaryIdentification();
		List<Integer> segments = tt.getSegmentation();

		for (Integer i : segments) {
			int b = annotationList.get(i).getBegin();
			AnnotationFactory.createAnnotation(aJCas, b, b + 1,
					SegmentBoundary.class);
		}

	}

	protected List<List<String>> getSurfaces(JCas aJCas, Type type) {
		List<List<String>> tokenSurfaces = new LinkedList<List<String>>();
		switch (type) {
		case Token:
			for (Token token : JCasUtil.select(aJCas, Token.class)) {
				tokenSurfaces.add(Arrays.asList(token.getCoveredText()));
			}
			break;
		default:
			for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) {
				List<String> sentenceList = new LinkedList<String>();
				for (Token token : JCasUtil.selectCovered(aJCas, Token.class,
						sentence)) {
					sentenceList.add(token.getCoveredText());
				}
				tokenSurfaces.add(sentenceList);
			}
		}
		return tokenSurfaces;
	}

	public void bla(JCas aJCas) {
		List<List<String>> tokenSurfaces = new LinkedList<List<String>>();
		List<Token> tokens = new LinkedList<Token>();

		Iterator<Token> iterator = JCasUtil.iterator(aJCas, Token.class);
		while (iterator.hasNext()) {
			Token token = iterator.next();
			tokens.add(token);
			tokenSurfaces.add(Arrays.asList(token.getCoveredText()));
		}

		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) {
			List<String> sentenceList = new LinkedList<String>();
			for (Token token : JCasUtil.selectCovered(aJCas, Token.class,
					sentence)) {
				sentenceList.add(token.getCoveredText());
			}
			tokenSurfaces.add(sentenceList);
		}
	}

}
