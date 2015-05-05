package de.nilsreiter.segmentation.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.nilsreiter.pipeline.segmentation.SegmentBoundaryAnnotator;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class AddSegmentBoundaries {

	public static void main(String[] args) throws UIMAException, IOException {
		Options options = CliFactory.parseArguments(Options.class, args);
		String[] locations = TypeSystemDescriptionFactory.scanTypeDescriptors();

		File typesystemFile;
		if (options.getInputFile().isDirectory()) {
			typesystemFile = new File(options.getInputFile(), "typesystem.xml");
		} else {
			typesystemFile =
					new File(options.getInputFile().getParentFile(),
							"typesystem.xml");
		}

		locations = ArrayUtils.add(locations, typesystemFile.getAbsolutePath());
		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory
						.createTypeSystemDescriptionFromPath(locations);
		AnalysisEngineDescription aed =
				AnalysisEngineFactory.createEngineDescription(
						SegmentBoundaryAnnotator.class,
						SegmentBoundaryAnnotator.PARAM_BASE_ANNOTATION,
						options.getSegmentClassName());

		if (options.getInputFile().isDirectory()) {
			String inputLocation =
					options.getInputFile().getAbsolutePath() + File.separator
							+ "*.xmi";
			SimplePipeline.runPipeline(CollectionReaderFactory
					.createReaderDescription(XmiReader.class, tsd,
							XmiReader.PARAM_SOURCE_LOCATION, inputLocation),
					aed, AnalysisEngineFactory.createEngineDescription(
							XmiWriter.class, XmiWriter.PARAM_TARGET_LOCATION,
							options.getOutputDirectory()));
		} else {
			JCas jcas =
					JCasFactory.createJCas(options.getInputFile()
							.getAbsolutePath(), tsd);

			SimplePipeline.runPipeline(jcas, aed, AnalysisEngineFactory
					.createEngineDescription(XmiWriter.class,
							XmiWriter.PARAM_TARGET_LOCATION,
							options.getOutputDirectory()));
		}
	}

	public interface Options {
		@Option
		File getInputFile();

		@Option
		File getOutputDirectory();

		@Option
		String getSegmentClassName();
	}

}
