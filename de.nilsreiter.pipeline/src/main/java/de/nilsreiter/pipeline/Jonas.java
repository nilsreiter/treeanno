package de.nilsreiter.pipeline;

import static de.nilsreiter.pipeline.PipelineBuilder.array;
import static de.nilsreiter.pipeline.PipelineBuilder.conll;
import static de.nilsreiter.pipeline.PipelineBuilder.xmi;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordCoreferenceResolver;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.uniheidelberg.cl.a10.MainWithIODir;

public class Jonas extends MainWithIODir {
	public static void main(String[] args) throws UIMAException, IOException {
		Jonas j = new Jonas();
		j.processArguments(args);
		j.run();
	}

	public CollectionReader getTextCollectionReader()
			throws ResourceInitializationException {
		return createReader(TextReader.class, TextReader.PARAM_SOURCE_LOCATION,
				this.input.getAbsolutePath() + File.separator + "*.txt",
				TextReader.PARAM_LANGUAGE, "en");
	}

	private void run() throws UIMAException, IOException {
		List<AnalysisEngineDescription> l =
				new ArrayList<AnalysisEngineDescription>();
		l.add(createEngineDescription(StanfordSegmenter.class));
		l.add(createEngineDescription(StanfordLemmatizer.class));
		l.add(createEngineDescription(StanfordPosTagger.class));
		l.add(createEngineDescription(StanfordParser.class));
		l.add(createEngineDescription(StanfordCoreferenceResolver.class));

		l = conll(xmi(l, this.getOutputDirectory()), this.getOutputDirectory());
		SimplePipeline.runPipeline(getTextCollectionReader(), array(l));
	}
}
