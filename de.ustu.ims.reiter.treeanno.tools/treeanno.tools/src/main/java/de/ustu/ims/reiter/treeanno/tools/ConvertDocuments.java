package de.ustu.ims.reiter.treeanno.tools;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class ConvertDocuments {

	public static void main(String[] args) throws UIMAException, IOException {
		Options options = CliFactory.parseArguments(Options.class, args);

		CollectionReaderDescription crd =
				CollectionReaderFactory.createReaderDescription(
						XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION,
						options.getInputDirectory() + File.separator + "*.xmi",
						XmiReader.PARAM_LENIENT, true);

		SimplePipeline.runPipeline(crd, AnalysisEngineFactory
				.createEngineDescription(MapToTreeAnnoClass.class,
						MapToTreeAnnoClass.PARAM_CLASSNAME,
						options.getSegmentClassName()), AnalysisEngineFactory
				.createEngineDescription(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION,
						options.getOutputDirectory()));
	}

	interface Options {
		@Option
		File getInputDirectory();

		@Option
		File getOutputDirectory();

		@Option
		String getSegmentClassName();
	}

}
