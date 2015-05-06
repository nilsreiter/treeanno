package de.ustu.creta.segmentation.evaluation;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.ustu.creta.segmentation.evaluation.impl.SegmentationUtil;

public class TestSegmentationUtil {

	JCas gold;
	String text = "The dog barks. It is hungry.";

	@Before
	public void setUp() throws UIMAException {

		gold = JCasFactory.createJCas();
		gold.setDocumentText(text);

		// sentence 1
		createAnnotation(gold, 0, 3, SegmentationUnit.class);
		createAnnotation(gold, 4, 7, SegmentationUnit.class);
		createAnnotation(gold, 8, 13, SegmentationUnit.class);
		createAnnotation(gold, 13, 14, SegmentationUnit.class);

		// sentence 2
		createAnnotation(gold, 15, 17, SegmentationUnit.class);
		createAnnotation(gold, 18, 20, SegmentationUnit.class);
		createAnnotation(gold, 21, 27, SegmentationUnit.class);
		createAnnotation(gold, 27, 28, SegmentationUnit.class);
	}

	@Test
	public void testGetMassTuple1() {
		createAnnotation(gold, 0, 0, SegmentBoundary.class);
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		createAnnotation(gold, 28, 28, SegmentBoundary.class);
		int[] tuple =
				SegmentationUtil.getMassTuple(gold, SegmentBoundary.class);
		// System.err.println(tuple);

		assertEquals(4, tuple.length);
		assertEquals(0, tuple[0]);
		assertEquals(4, tuple[1]);
		assertEquals(4, tuple[2]);
		assertEquals(0, tuple[3]);
	}

	@Test
	public void testGetMassTuple2() {
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		int[] tuple =
				SegmentationUtil.getMassTuple(gold, SegmentBoundary.class);
		// System.err.println(tuple);
		assertEquals(4, tuple[0]);
		assertEquals(4, tuple[1]);
	}

	@Test
	public void testGetMassTuple3() {

		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		createAnnotation(gold, 17, 17, SegmentBoundary.class);
		int[] tuple =
				SegmentationUtil.getMassTuple(gold, SegmentBoundary.class);
		System.err.println(tuple);
		assertEquals(4, tuple[0]);
		assertEquals(1, tuple[1]);
		assertEquals(3, tuple[2]);
	}

	@Test
	public void testGetBoundaryString1() {
		int[] array = new int[] { 1, 2, 2, 2, 4, 2, 1 };

		boolean[] b = SegmentationUtil.getBoundaryString(array);
		assertFalse(b[0]);
		assertTrue(b[1]);
		assertFalse(b[2]);
		assertTrue(b[3]);
		assertFalse(b[4]);
		assertTrue(b[5]);
		assertFalse(b[6]);
		assertTrue(b[7]);
		assertFalse(b[8]);
		assertFalse(b[9]);
		assertFalse(b[10]);
		assertTrue(b[11]);
		assertFalse(b[12]);
		assertTrue(b[13]);
	}

	@Test
	public void testGetBoundaryString2() {
		int[] array = new int[] { 1, 2, 0, 1, 0, 0, 0 };

		boolean[] b = SegmentationUtil.getBoundaryString(array);
		assertFalse(b[0]);
		assertTrue(b[1]);
		assertFalse(b[2]);
		assertTrue(b[3]);
	}

	@Test
	public void testGetBoundaryString3() {
		int[] array = new int[] { 1, 2, 0 };

		boolean[] b = SegmentationUtil.getBoundaryString(array);
		assertFalse(b[0]);
		assertTrue(b[1]);
		assertFalse(b[2]);
	}
}
