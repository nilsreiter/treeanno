package de.nilsreiter.segmentation.main;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;
import org.kohsuke.args4j.Option;

import de.nilsreiter.pipeline.segmentation.AddCFAnnotations;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.uniheidelberg.cl.a10.MainWithIODir;

public class CF2Xmi extends MainWithIODir {

	@Option(name = "--csv-directory", aliases = { "-csv" }, required = true)
	File csvDir;

	@Option(name = "--threshold", required = false)
	float threshold = 0.0f;

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		CF2Xmi c = new CF2Xmi();
		c.processArguments(args);
		System.exit(c.run());
	}

	public int run() throws ResourceInitializationException, UIMAException,
	IOException {
		SimplePipeline.runPipeline(
				CollectionReaderFactory.createReader(TextReader.class,
						TextReader.PARAM_SOURCE_LOCATION, this
						.getInputDirectory().getAbsolutePath()
						+ File.separator + "*.txt",
						TextReader.PARAM_LANGUAGE, "en"),
						createEngineDescription(StanfordSegmenter.class),
						createEngineDescription(AddCFAnnotations.class,
								AddCFAnnotations.PARAM_INPUT_DIRECTORY,
								csvDir.getAbsolutePath(),
						AddCFAnnotations.PARAM_THRESHOLD, threshold),
								createEngineDescription(XmiWriter.class,
										XmiWriter.PARAM_TARGET_LOCATION, this
										.getOutputDirectory().getAbsolutePath()));
		return 0;
	}
}
