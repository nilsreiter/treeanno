package de.nilsreiter.pipeline.segmentation;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;

public class TestCSVExport {
	JCas jcas;

	@Before
	public void setUp() throws Exception {
		jcas = JCasFactory.createJCas();
		InputStreamReader isr =
				new InputStreamReader(getClass().getResourceAsStream(
						"/segmentation_test.txt"));
		StringBuilder b = new StringBuilder();
		while (isr.ready()) {
			b.append((char) isr.read());
		}

		jcas.setDocumentText(b.toString());
		jcas.setDocumentLanguage("en");
		AnnotationFactory.createAnnotation(jcas, 0, b.length(),
				DocumentMetaData.class).setDocumentId("segmentation_test");;
		SimplePipeline.runPipeline(jcas,
				AnalysisEngineFactory.createEngine(StanfordSegmenter.class));
	};

	// TODO: Make more meaningful test
	@Test
	public void testCSVExport() throws AnalysisEngineProcessException,
	ResourceInitializationException, IOException {
		File tempdir =
				Files.createTempDirectory("test", new FileAttribute[0])
						.toFile();
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(CSVExport.class,
						CSVExport.PARAM_OUTPUT_DIRECTORY,
						tempdir.getAbsolutePath(), CSVExport.PARAM_PREFIX_SIZE,
						3));
		tempdir.deleteOnExit();
	}
}
