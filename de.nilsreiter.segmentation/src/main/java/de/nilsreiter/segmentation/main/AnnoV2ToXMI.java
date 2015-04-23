package de.nilsreiter.segmentation.main;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.nilsreiter.pipeline.PipelineBuilder;
import de.nilsreiter.pipeline.io.TextReader;
import de.nilsreiter.pipeline.segmentation.annotation.AnnotationReaderV2;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.languagetool.LanguageToolSegmenter;

public class AnnoV2ToXMI {

	public static void main(String[] args) throws Exception {
		Options options = CliFactory.parseArguments(Options.class, args);

		CollectionReaderDescription crd =
				CollectionReaderFactory.createReaderDescription(
						TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
						options.getRawDirectory() + "/*.txt",
						TextReader.PARAM_LANGUAGE, options.getLanguage());

		List<AnalysisEngineDescription> pl =
				new LinkedList<AnalysisEngineDescription>();
		pl.add(AnalysisEngineFactory.createEngineDescription(
				AnnotationReaderV2.class,
				AnnotationReaderV2.PARAM_DIRECTORY_NAME, options
				.getAnnoDirectory().getAbsolutePath(),
				AnnotationReaderV2.PARAM_FILE_SUFFIX, options.getFileSuffix()));
		if (options.getAddTokens()) {
			pl.add(AnalysisEngineFactory
					.createEngineDescription(LanguageToolSegmenter.class));
		}
		pl.add(AnalysisEngineFactory.createEngineDescription(XmiWriter.class,
				XmiWriter.PARAM_TARGET_LOCATION, options.getOutputDirectory()));

		SimplePipeline.runPipeline(crd, PipelineBuilder.array(pl));
	}

	public interface Options {
		@Option
		File getRawDirectory();

		@Option
		File getAnnoDirectory();

		@Option
		File getOutputDirectory();

		@Option
		String getFileSuffix();

		@Option
		boolean getAddTokens();

		@Option
		String getLanguage();
	}

}
