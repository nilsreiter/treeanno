package de.nilsreiter.pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.File;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.uima.Data2Exporter;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class PipelineBuilder {

	public static AnalysisEngineDescription[] array(
			List<AnalysisEngineDescription> pipeline) {
		AnalysisEngineDescription[] arr =
				new AnalysisEngineDescription[pipeline.size()];
		return pipeline.toArray(arr);
	}

	public static List<AnalysisEngineDescription> xmi(
			List<AnalysisEngineDescription> pipeline, File directory)
					throws ResourceInitializationException {
		pipeline.add(createEngineDescription(XmiWriter.class,
				XmiWriter.PARAM_TARGET_LOCATION,
				new File(directory, "xmi").getAbsolutePath()));
		return pipeline;
	}

	public static List<AnalysisEngineDescription> data2(
			List<AnalysisEngineDescription> pipeline, File directory)
					throws ResourceInitializationException {
		pipeline.add(createEngineDescription(Data2Exporter.class,
				Data2Exporter.PARAM_OUTPUT_DIRECTORY, new File(directory,
						"data2").getAbsolutePath()));
		return pipeline;
	}

}
