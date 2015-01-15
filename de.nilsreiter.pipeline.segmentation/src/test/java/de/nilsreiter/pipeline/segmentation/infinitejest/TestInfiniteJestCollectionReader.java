package de.nilsreiter.pipeline.segmentation.infinitejest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class TestInfiniteJestCollectionReader {

	@Test
	public void testText() throws ResourceInitializationException {
		String filePath =
				"/Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/test/resources/infinite_jest_annotated.txt";

		CollectionReaderDescription cr =
				CollectionReaderFactory
						.createReaderDescription(
								IJReader.class,
								IJReader.PARAM_SOURCE_LOCATION,
								filePath,
								IJReader.PARAM_CREATE_BOUNDARY_ANNOTATION,
								false,
						IJReader.PARAM_CREATE_SEGMENT_ANNOTATION,
						false);

		JCasIterable jcasIter = SimplePipeline.iteratePipeline(cr);
		JCas jcas = jcasIter.iterator().next();
		// System.err.println(jcas.getDocumentText());

		assertEquals("INFINITE J", jcas.getDocumentText().substring(0, 10));
		assertEquals("es in it. ",
				jcas.getDocumentText().substring(10000, 10010));
		assertFalse(jcas.getDocumentText().contains("<"));
		assertFalse(jcas.getDocumentText().contains(">"));
		assertFalse(JCasUtil.exists(jcas, Segment.class));
		assertFalse(JCasUtil.exists(jcas, SegmentBoundary.class));
	}

	@Test
	public void testSegmentAnnotation() throws Exception {
		String filePath =
				"/Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/test/resources/infinite_jest_annotated.txt";

		CollectionReaderDescription cr =
				CollectionReaderFactory
						.createReaderDescription(
								IJReader.class,
								IJReader.PARAM_SOURCE_LOCATION,
								filePath,
								IJReader.PARAM_CREATE_BOUNDARY_ANNOTATION,
								false,
								IJReader.PARAM_CREATE_SEGMENT_ANNOTATION,
								true);

		JCasIterable jcasIter = SimplePipeline.iteratePipeline(cr);
		JCas jcas = jcasIter.iterator().next();

		assertTrue(JCasUtil.exists(jcas, Segment.class));
		Segment segment;

		segment = JCasUtil.selectByIndex(jcas, Segment.class, 0);
		assertEquals(3262, segment.getBegin());
		assertEquals("ETA", segment.getValue());
		assertEquals(24147, segment.getEnd());

		segment = JCasUtil.selectByIndex(jcas, Segment.class, 1);
		assertEquals(28207, segment.getBegin());
		assertEquals("ETA", segment.getValue());
	}

	@Test
	public void testConversion() throws Exception {
		String filePath =
				"/Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/test/resources/*.txt";
		String tFilePath =
				"/Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/test/resources/xmi/";
		CollectionReader cr;
		cr =
				CollectionReaderFactory.createReader(
						IJReader.class,
						IJReader.PARAM_SOURCE_LOCATION,
						filePath);
		AnalysisEngine writer =
				AnalysisEngineFactory.createEngine(XmiWriter.class,
						XmiWriter.PARAM_TARGET_LOCATION,
						new File(tFilePath).getAbsolutePath());
		SimplePipeline.runPipeline(cr, writer);

		assertTrue(true);

	}

	@Test
	public void testBoundaryAnnotation() throws Exception {
		String filePath =
				"/Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/test/resources/infinite_jest_annotated.txt";

		CollectionReaderDescription cr =
				CollectionReaderFactory
						.createReaderDescription(
								IJReader.class,
								IJReader.PARAM_SOURCE_LOCATION,
								filePath,
								IJReader.PARAM_CREATE_BOUNDARY_ANNOTATION,
								true);

		JCasIterable jcasIter = SimplePipeline.iteratePipeline(cr);
		JCas jcas = jcasIter.iterator().next();
		JCasUtil.exists(jcas, SegmentBoundary.class);

	}
}
