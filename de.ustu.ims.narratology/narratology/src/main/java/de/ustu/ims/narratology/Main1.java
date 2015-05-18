package de.ustu.ims.narratology;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class Main1 {

	public static void main(String[] args) throws UIMAException, IOException {

		CollectionReaderDescription crd =
				CollectionReaderFactory.createReaderDescription(
						TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
						"src/main/resources/Seaman.txt",
						TextReader.PARAM_LANGUAGE, "en");

		AnalysisEngineDescription tokenizer =
				AnalysisEngineFactory
				.createEngineDescription(StanfordSegmenter.class);
		AnalysisEngineDescription posTagger =
				AnalysisEngineFactory
				.createEngineDescription(StanfordPosTagger.class);

		AnalysisEngineDescription myComponent =
				AnalysisEngineFactory.createEngineDescription(Component1.class);

		AnalysisEngineDescription xmiWriter =
				AnalysisEngineFactory.createEngineDescription(XmiWriter.class);

		SimplePipeline.runPipeline(crd, tokenizer, posTagger, myComponent,
				xmiWriter);
	}
}
