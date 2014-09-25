package de.nilsreiter.pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.File;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.uima.Data2Exporter;
import de.tudarmstadt.ukp.dkpro.core.io.conll.Conll2006Writer;
import de.tudarmstadt.ukp.dkpro.core.io.imscwb.ImsCwbWriter;
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

	public static List<AnalysisEngineDescription> conll(
			List<AnalysisEngineDescription> pipeline, File directory)
			throws ResourceInitializationException {
		pipeline.add(createEngineDescription(Conll2006Writer.class,
				Conll2006Writer.PARAM_TARGET_LOCATION, new File(directory,
						"conll").getAbsolutePath()));
		return pipeline;
	}

	public static List<AnalysisEngineDescription> cwb(
			List<AnalysisEngineDescription> pipeline, File directory)
			throws ResourceInitializationException {
		pipeline.add(createEngineDescription(ImsCwbWriter.class,
				ImsCwbWriter.PARAM_TARGET_LOCATION,
				new File(directory, "cwb").getAbsolutePath(),
				ImsCwbWriter.PARAM_CQPWEB_COMPATIBILITY, true,
				ImsCwbWriter.PARAM_WRITE_CPOS, false));
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
