package de.nilsreiter.pipeline.segmentation.infinitejest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.Segment;

public class TestInfiniteJestCollectionReader {
	@Test
	public void testCollectionReader() throws ResourceInitializationException {
		String filePath =
				"/Users/reiterns/Documents/Java/de.nilsreiter.pipeline.segmentation/src/test/resources/infinite_jest_annotated.txt";

		CollectionReaderDescription cr =
				CollectionReaderFactory
						.createReaderDescription(
								InfiniteJestCollectionReader.class,
								InfiniteJestCollectionReader.PARAM_INPUT_FILE,
								filePath);

		JCasIterable jcasIter = SimplePipeline.iteratePipeline(cr);
		JCas jcas = jcasIter.iterator().next();
		// System.err.println(jcas.getDocumentText());

		assertEquals("INFINITE J", jcas.getDocumentText().substring(0, 10));
		assertEquals("es in it. ",
				jcas.getDocumentText().substring(10000, 10010));
		assertFalse(jcas.getDocumentText().contains("<"));
		assertFalse(jcas.getDocumentText().contains(">"));

		assertTrue(JCasUtil.exists(jcas, Segment.class));
		Segment segment;

		segment = JCasUtil.selectByIndex(jcas, Segment.class, 0);
		assertEquals(3262, segment.getBegin());
		assertEquals(42807, segment.getEnd());
		assertEquals("HAL", segment.getValue());

		segment = JCasUtil.selectByIndex(jcas, Segment.class, 1);
		assertEquals(3262, segment.getBegin());
		assertEquals("INC", segment.getValue());
	}
}
