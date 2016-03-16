package de.ustu.ims.reiter.treeanno.tools;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;

public class SyntaxAnnotator {

	public static void main(String[] args) throws UIMAException, IOException {
		File inputDirectory =
				new File(
						"/Users/reiterns/Documents/treeanno/projects/eckbert/xmi");

		CollectionReaderDescription crd =
				CollectionReaderFactory.createReaderDescription(
						XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION,
						inputDirectory.getAbsolutePath() + File.separator
								+ "*.xmi");

		SimplePipeline
				.runPipeline(
						crd,
						createEngineDescription(LanguageToolSegmenter.class),
						createEngineDescription(StanfordParser.class),
						createEngineDescription(XmiWriter.class,
								XmiWriter.PARAM_TARGET_LOCATION,
								"/Users/reiterns/Documents/treeanno/projects/eckbert/xmi2"));
	}

}
