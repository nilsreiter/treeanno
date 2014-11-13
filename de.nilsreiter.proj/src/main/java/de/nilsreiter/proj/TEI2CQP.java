package de.nilsreiter.proj;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.kohsuke.args4j.Option;

import de.nilsreiter.pipeline.PipelineBuilder;
import de.tudarmstadt.ukp.dkpro.core.io.tei.TeiReader;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;
import de.uniheidelberg.cl.a10.MainWithIODir;

public class TEI2CQP extends MainWithIODir {

	@Option(name = "--language")
	String language;

	public static void main(String[] args) throws UIMAException, IOException {
		TEI2CQP t2c = new TEI2CQP();
		t2c.processArguments(args);
		t2c.run();
	}

	public void run() throws UIMAException, IOException {
		ArrayList<AnalysisEngineDescription> ae =
				new ArrayList<AnalysisEngineDescription>();
		ae.add(createEngineDescription(LanguageToolSegmenter.class));

		runPipeline(
				createReader(TeiReader.class, TeiReader.PARAM_SOURCE_LOCATION,
						this.getInputDirectory() + File.separator + "*.xml",
						TeiReader.PARAM_LANGUAGE, language,
						TeiReader.PARAM_WRITE_TOKEN, true,
						TeiReader.PARAM_USE_FILENAME_ID, true),
						PipelineBuilder.array(PipelineBuilder.xmi(
								PipelineBuilder.cwb(ae, getOutputDirectory()),
								this.getOutputDirectory())));
	}
}
