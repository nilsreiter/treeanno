package de.nilsreiter.segmentation.main;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.segmentation.CSVExport;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class MakeCrowdData {

	static File inputDirectory = new File(
			"/Users/reiterns/Documents/SegNarr/CrowdFlower/exp1/txt");
	static File outputDirectory = new File(
			"/Users/reiterns/Documents/SegNarr/CrowdFlower/exp1/csv");

	// TODO: Prepare an XMI file that can be imported easily
	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		SimplePipeline.runPipeline(CollectionReaderFactory
				.createReaderDescription(TextReader.class,
						TextReader.PARAM_LANGUAGE, "en",
						TextReader.PARAM_SOURCE_LOCATION,
						inputDirectory.getAbsolutePath() + File.separatorChar
						+ "*.txt"), AnalysisEngineFactory
						.createEngineDescription(StanfordSegmenter.class),
				AnalysisEngineFactory.createEngineDescription(CSVExport.class,
								CSVExport.PARAM_OUTPUT_DIRECTORY,
								outputDirectory.getAbsolutePath(),
						CSVExport.PARAM_PREFIX_SIZE, 10));
	}

}