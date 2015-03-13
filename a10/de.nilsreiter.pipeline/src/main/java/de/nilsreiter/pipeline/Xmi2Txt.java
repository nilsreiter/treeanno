package de.nilsreiter.pipeline;

import static de.nilsreiter.pipeline.PipelineBuilder.array;
import static de.nilsreiter.pipeline.PipelineBuilder.txt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.uniheidelberg.cl.a10.MainWithIODir;

public class Xmi2Txt extends MainWithIODir {
	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		Xmi2Txt cx = new Xmi2Txt();
		cx.processArguments(args);
		cx.run();
	}

	private void run() throws ResourceInitializationException, UIMAException,
			IOException {
		/*
		 * TypeSystemDescription tsd;
		 * 
		 * tsd = CasCreationUtils.mergeTypeSystems(Arrays.asList(
		 * TypeSystemDescriptionFactory .createTypeSystemDescription(),
		 * TypeSystemDescriptionFactory .createTypeSystemDescriptionFromPath(new
		 * File( this.getInputDirectory(),
		 * "typesystem.xml").toURI().toString())));
		 */
		SimplePipeline.runPipeline(
				CollectionReaderFactory.createReaderDescription(
						XmiReader.class, XmiReader.PARAM_SOURCE_LOCATION, this
								.getInputDirectory().getAbsolutePath()
								+ File.separator + "*.xmi",
						XmiReader.PARAM_LENIENT, true),
				array(txt(new ArrayList<AnalysisEngineDescription>(),
						this.getOutputDirectory())));

	}
}
