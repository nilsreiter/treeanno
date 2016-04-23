package de.ustu.ims.reiter.treeanno.uima;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Before;
import org.junit.Test;

import de.ustu.ims.reiter.treeanno.api.type.TreeSegment;

public class TestGraphExporter {
	JCas jcas;
	TreeSegment[] ts;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("Bla Bla Bla Bla");
		ts =
				new TreeSegment[] {
				AnnotationFactory.createAnnotation(jcas, 0, 3,
						TreeSegment.class),
						AnnotationFactory.createAnnotation(jcas, 4, 7,
								TreeSegment.class),
								AnnotationFactory.createAnnotation(jcas, 8, 11,
										TreeSegment.class),
										AnnotationFactory.createAnnotation(jcas, 12, 14,
												TreeSegment.class) };
	}

	@Test
	public void testGraphExporter() throws AnalysisEngineProcessException,
			ResourceInitializationException, IOException {
		ts[1].setParent(ts[0]);
		File temp = File.createTempFile("test", "");
		temp.delete();
		temp.mkdir();
		SimplePipeline.runPipeline(jcas, AnalysisEngineFactory
				.createEngineDescription(GraphExporter.class,
						GraphExporter.PARAM_OUTPUT_DIRECTORY,
						temp.getAbsolutePath()));
	}
}
