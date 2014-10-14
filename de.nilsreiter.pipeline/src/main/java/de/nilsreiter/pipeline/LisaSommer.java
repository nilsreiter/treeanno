package de.nilsreiter.pipeline;

import static de.nilsreiter.pipeline.PipelineBuilder.array;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.kohsuke.args4j.Option;

import de.nilsreiter.pipeline.langdetect.LanguageAnnotator;
import de.nilsreiter.pipeline.uima.TableExporter;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.uniheidelberg.cl.a10.MainWithIODir;

public class LisaSommer extends MainWithIODir {

	@Option(name = "--profiles", required = true,
			usage = "The directory of the language profiles")
	File languageProfilesDirectory;

	public static void main(String[] args) throws Exception {
		LisaSommer sm = new LisaSommer();
		sm.processArguments(args);
		sm.run();
	}

	protected void run() throws UIMAException, IOException {
		ArrayList<AnalysisEngineDescription> ae =
				new ArrayList<AnalysisEngineDescription>();
		ae.add(createEngineDescription(LanguageToolSegmenter.class));
		ae.add(createEngineDescription(LanguageAnnotator.class,
				LanguageAnnotator.PARAM_PROFILES_DIRECTORY,
				this.languageProfilesDirectory));
		ae.add(createEngineDescription(TableExporter.class,
				TableExporter.PARAM_OUTPUTDIR, this.getOutputDirectory()));
		runPipeline(
				createReader(TextReader.class,
						TextReader.PARAM_SOURCE_LOCATION,
						this.getInputDirectory(), TextReader.PARAM_LANGUAGE,
						"de"), array(ae));

	}
}
