package de.nilsreiter.pipeline.segmentation.morphadorner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import edu.northwestern.at.morphadorner.corpuslinguistics.stopwords.BaseStopWords;
import edu.northwestern.at.morphadorner.corpuslinguistics.textsegmenter.struct.RawText;
import edu.northwestern.at.morphadorner.corpuslinguistics.textsegmenter.texttiling.TextTiling;

@TypeCapability(inputs = {})
public class TextTilingAnnotator extends JCasAnnotator_ImplBase {

	int stepSize = 10;
	int windowSize = 100;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		List<List<String>> tokenSurfaces = new LinkedList<List<String>>();
		List<Token> tokens = new LinkedList<Token>();

		Iterator<Token> iterator = JCasUtil.iterator(aJCas, Token.class);
		while (iterator.hasNext()) {
			Token token = iterator.next();
			tokens.add(token);
			tokenSurfaces.add(Arrays.asList(token.getCoveredText()));
		}

		RawText rt = new RawText(tokenSurfaces);

		TextTiling tt = new TextTiling(rt, new BaseStopWords());
		tt.similarityDetermination();
		tt.depthScore();
		tt.boundaryIdentification();
		List<Integer> segments = tt.getSegmentation();
		System.err.println(segments);
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
