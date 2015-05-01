package de.ustu.creta.segmentation.evaluation;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;
import static org.junit.Assert.assertEquals;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;
import org.python.core.PyTuple;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;
import de.ustu.creta.segmentation.evaluation.impl.AbstractSegEvalMetric;
import de.ustu.creta.segmentation.evaluation.impl.BoundarySimilarity_impl;

public class TestAbstractSegEvalMetric {
	AbstractSegEvalMetric metric;
	JCas gold;
	String text = "The dog barks. It is hungry.";

	@Before
	public void setUp() throws UIMAException {
		metric = new BoundarySimilarity_impl(SegmentBoundary.class);

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
		PyTuple tuple = metric.getPyMassTuple(gold, SegmentBoundary.class);
		System.err.println(tuple);

		assertEquals(4, tuple.__len__());
		assertEquals(0, tuple.__getitem__(0).asInt());
		assertEquals(4, tuple.__getitem__(1).asInt());
		assertEquals(4, tuple.__getitem__(2).asInt());
		assertEquals(0, tuple.__getitem__(3).asInt());
	}

	@Test
	public void testGetMassTuple2() {
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		PyTuple tuple = metric.getPyMassTuple(gold, SegmentBoundary.class);
		System.err.println(tuple);
		assertEquals(4, tuple.__getitem__(0).asInt());
		assertEquals(4, tuple.__getitem__(1).asInt());
	}

	@Test
	public void testGetMassTuple3() {
		createAnnotation(gold, 15, 15, SegmentBoundary.class);
		createAnnotation(gold, 17, 17, SegmentBoundary.class);
		PyTuple tuple = metric.getPyMassTuple(gold, SegmentBoundary.class);
		System.err.println(tuple);
		assertEquals(4, tuple.__getitem__(0).asInt());
		assertEquals(1, tuple.__getitem__(1).asInt());
		assertEquals(3, tuple.__getitem__(2).asInt());
	}
}
