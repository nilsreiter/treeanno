package de.ustu.ims.reiter.tense.experiments;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.ustu.ims.reiter.tense.annotator.TenseAnnotator;

public class SilverAnnotator {

	public static void main(String[] args)
			throws ResourceInitializationException, UIMAException, IOException {
		SimplePipeline.runPipeline(CollectionReaderFactory
				.createReaderDescription(XmiReader.class,
						XmiReader.PARAM_SOURCE_LOCATION,
						"target/main/resources/plain/*.xmi"),
						createEngineDescription(LanguageAnnotator.class),
				createEngineDescription(StanfordPosTagger.class),
				// createEngineDescription(AspectAnnotator.class),
				createEngineDescription(TenseAnnotator.class),
						createEngineDescription(XmiWriter.class,
								XmiWriter.PARAM_TARGET_LOCATION,
						"target/main/resources/silver/"));

	}

	public static class LanguageAnnotator extends JCasAnnotator_ImplBase {

		@Override
		public void process(JCas aJCas) throws AnalysisEngineProcessException {
			aJCas.setDocumentLanguage("en");
		}

	}
}
