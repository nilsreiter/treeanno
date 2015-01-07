package de.nilsreiter.pipeline;

import static de.nilsreiter.pipeline.PipelineBuilder.array;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.tei.TeiReader;
import de.uniheidelberg.cl.a10.MainWithIODir;

public class TEI2TXT extends MainWithIODir {
	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		TEI2TXT t2t = new TEI2TXT();
		t2t.processArguments(args);
		t2t.run();
	}

	public CollectionReader getTextCollectionReader()
			throws ResourceInitializationException {
		return createReader(TeiReader.class, TeiReader.PARAM_SOURCE_LOCATION,
				this.input.getAbsolutePath() + File.separator + "*.xml");
	}

	public void run() throws ResourceInitializationException, UIMAException,
			IOException {
		if (!getOutputDirectory().exists()) {
			this.getOutputDirectory().mkdirs();
		}
		SimplePipeline.runPipeline(getTextCollectionReader(),
				array(PipelineBuilder.xmi(PipelineBuilder.txt(
						new ArrayList<AnalysisEngineDescription>(),
						this.getOutputDirectory()), getOutputDirectory())));
	};
}
