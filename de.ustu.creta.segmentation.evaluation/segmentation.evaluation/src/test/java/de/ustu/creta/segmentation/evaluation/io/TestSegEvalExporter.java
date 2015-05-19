package de.ustu.creta.segmentation.evaluation.io;

import static org.apache.uima.fit.factory.AnnotationFactory.createAnnotation;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;

@Deprecated
public class TestSegEvalExporter {
	JCas gold, silv;
	String text = "The dog barks. It is hungry.";

	@Before
	public void setUp() throws UIMAException {
		gold = JCasFactory.createJCas();
		gold.setDocumentText(text);

		silv = JCasFactory.createJCas();
		silv.setDocumentText(text);

		createAnnotation(gold, 14, 14, SegmentBoundary.class);

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

		// sentence 1
		createAnnotation(silv, 0, 3, SegmentationUnit.class);
		createAnnotation(silv, 4, 7, SegmentationUnit.class);
		createAnnotation(silv, 8, 13, SegmentationUnit.class);
		createAnnotation(silv, 13, 14, SegmentationUnit.class);

		// sentence 2
		createAnnotation(silv, 15, 17, SegmentationUnit.class);
		createAnnotation(silv, 18, 20, SegmentationUnit.class);
		createAnnotation(silv, 21, 27, SegmentationUnit.class);
		createAnnotation(silv, 27, 28, SegmentationUnit.class);
	}

	@Test
	public void testSegEvalExport() {
		SegEvalExporter exp = new SegEvalExporter();
		exp.collect(gold, silv);
		JSONObject obj = exp.exportToJSON();
		System.err.println(obj.toString());
	}

}
