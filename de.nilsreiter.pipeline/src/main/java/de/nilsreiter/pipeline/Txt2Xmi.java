package de.nilsreiter.pipeline;

import static de.nilsreiter.pipeline.PipelineBuilder.array;
import static de.nilsreiter.pipeline.PipelineBuilder.xmi;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;
import org.kohsuke.args4j.Option;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.uniheidelberg.cl.a10.MainWithIODir;

public class Txt2Xmi extends MainWithIODir {

	@Option(name = "--language", usage = "ISO language code")
	String language = "en";

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		Txt2Xmi cx = new Txt2Xmi();
		cx.processArguments(args);
		cx.run();
	}

	public CollectionReader getTextCollectionReader()
			throws ResourceInitializationException {
		return createReader(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
				this.input.getAbsolutePath() + File.separator + "*.txt",
				TextReader.PARAM_LANGUAGE, this.language);
	}

	public void run() throws ResourceInitializationException, UIMAException,
			IOException {
		SimplePipeline.runPipeline(
				getTextCollectionReader(),
				array(xmi(new ArrayList<AnalysisEngineDescription>(),
						this.getOutputDirectory())));
	};
}
