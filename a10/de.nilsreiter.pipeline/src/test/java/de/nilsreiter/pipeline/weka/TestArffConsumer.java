package de.nilsreiter.pipeline.weka;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import de.nilsreiter.pipeline.weka.type.Weka;

public class TestArffConsumer {
	JCas jcas;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("The dog barks. The cat eats.");
		jcas.setDocumentLanguage("en");

		Weka weka;
		weka = AnnotationFactory.createAnnotation(jcas, 0, 3, Weka.class);
		weka.setFeature1("F1");
		weka.setFeature2(15);
		weka.setFeature3(3.3);
		weka.setFeature4("F7");
		weka = AnnotationFactory.createAnnotation(jcas, 4, 7, Weka.class);
		weka.setFeature4("F42");
		weka.setFeature1("F12");
		weka.setFeature3(2.2);
		weka.setFeature2(2);
	}

	@Test
	public void testArffConsumer() throws AnalysisEngineProcessException,
	ResourceInitializationException, IOException {
		File tempFile = File.createTempFile("weka", ".arff");
		SimplePipeline.runPipeline(
				jcas,
				createEngineDescription(ArffConsumer.class,
						ArffConsumer.PARAM_ANNOTATION_TYPE,
						Weka.class.getCanonicalName(),
						ArffConsumer.PARAM_OUTPUT_FILE,
						tempFile.getAbsolutePath()));

		ArffLoader al = new ArffLoader();
		al.setFile(tempFile);
		Instances instances = al.getDataSet();
		assertEquals(2, instances.numInstances());
		assertEquals(4, instances.numAttributes());
		Instance inst;
		inst = instances.instance(0);
		assertEquals("F1", inst.toString(0));
		assertEquals(15f, inst.value(1), 1e-5);
		assertEquals(3.3, inst.value(2), 1e-5);
		tempFile.delete();
	}

	@Test(expected = ResourceInitializationException.class)
	public void testArffConsumerCastError()
			throws AnalysisEngineProcessException,
			ResourceInitializationException, IOException {
		File tempFile = File.createTempFile("weka", ".arff");
		SimplePipeline.runPipeline(
				jcas,
				createEngineDescription(ArffConsumer.class,
						ArffConsumer.PARAM_ANNOTATION_TYPE,
						String.class.getCanonicalName(),
						ArffConsumer.PARAM_OUTPUT_FILE,
						tempFile.getAbsolutePath()));

	}
}
