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

	JCas gold, gold2;
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

		gold2 = JCasFactory.createJCas();
		gold2.setDocumentText("12345678901234567890");
		for (int i = 0; i < 20; i++) {
			createAnnotation(gold2, i, i + 1, SegmentationUnit.class);
		}
	}

	@Test
	public void testGetMassTuple1() {
		createAnnotation(gold, 0, 0, SegmentBoundary.class);
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		createAnnotation(gold, 28, 28, SegmentBoundary.class);
		int[] tuple =
				SegmentationUtil.getMassTuple(gold, SegmentBoundary.class);
		// System.err.println(tuple);

		assertEquals(2, tuple.length);
		// assertEquals(0, tuple[0]);
		assertEquals(4, tuple[0]);
		assertEquals(4, tuple[1]);
		// assertEquals(0, tuple[3]);
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
	public void testGetMassTuple4() {

		createAnnotation(gold, 4, 4, SegmentBoundary.class);
		createAnnotation(gold, 13, 13, SegmentBoundary.class);
		createAnnotation(gold, 18, 18, SegmentBoundary.class);
		int[] tuple =
				SegmentationUtil.getMassTuple(gold, SegmentBoundary.class);
		System.err.println(tuple);
		assertEquals(1, tuple[0]);
		assertEquals(2, tuple[1]);
		assertEquals(2, tuple[2]);
	}

	@Test
	public void testGetMassTuple5() {

		createAnnotation(gold, 8, 8, SegmentBoundary.class);
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		createAnnotation(gold, 21, 21, SegmentBoundary.class);
		int[] tuple =
				SegmentationUtil.getMassTuple(gold, SegmentBoundary.class);
		System.err.println(tuple);
		assertEquals(2, tuple[0]);
		assertEquals(2, tuple[1]);
		assertEquals(2, tuple[2]);
	}

	@Test
	public void testGetBoundaryString1() {
		int[] array = new int[] { 1, 2, 2, 2, 4, 2, 1 };

		boolean[] b = SegmentationUtil.getBoundaryString(array);
		assertEquals(SegmentationUtil.sum(array), b.length);
		assertTrue(b[0]);
		assertFalse(b[1]);
		assertTrue(b[2]);
		assertFalse(b[3]);
		assertTrue(b[4]);
		assertFalse(b[5]);
		assertTrue(b[6]);
		assertFalse(b[7]);
		assertFalse(b[8]);
		assertFalse(b[9]);
		assertTrue(b[10]);
		assertFalse(b[11]);
		assertTrue(b[12]);
		assertFalse(b[13]);
	}

	@Test
	public void testGetBoundaryString2() {
		int[] array = new int[] { 1, 2, 0, 1, 0, 0, 0 };

		boolean[] b = SegmentationUtil.getBoundaryString(array);
		assertTrue(b[0]);
		assertFalse(b[1]);
		assertTrue(b[2]);
		assertFalse(b[3]);
	}

	@Test
	public void testGetBoundaryString3() {
		int[] array = new int[] { 1, 2, 0 };

		boolean[] b = SegmentationUtil.getBoundaryString(array);
		assertTrue(b[0]);
		assertFalse(b[1]);
		assertFalse(b[2]);
	}

	@Test
	public void testGetMassTuple6() {
		for (int i = 0; i < gold2.getDocumentText().length() + 1; i++) {
			createAnnotation(gold2, i, i, SegmentBoundary.class);
		}
		int[] ms = SegmentationUtil.getMassTuple(gold2, SegmentBoundary.class);
		assertEquals(20, ms.length);
	}
}
