package de.nilsreiter.segmentation.main.eckbert;

import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;

public class ConvertEckbertToHTML {

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {

		SimplePipeline
		.runPipeline(
				createReaderDescription(
						XmiReader.class,
						XmiReader.PARAM_SOURCE_LOCATION,
						"/Users/reiterns/Documents/Workspace/Eckbert/corpus/Der%20Blonde%20Eckbert.txt.xmi"),
						AnalysisEngineFactory.createEngineDescription(
								HTMLExport.class,
								HTMLExport.PARAM_OUTPUT_DIRECTORY,
								"/Users/reiterns"));
	}
}
