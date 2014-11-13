package de.nilsreiter.ocr.uima;

import static org.junit.Assert.assertEquals;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.pipeline.uima.ocr.type.OCRCorrection;

public class TestOCRCorrectedExport {

	JCas jcas;
	OCRCorrectedExport export;

	@Before
	public void setUp() throws UIMAException {
		jcas = JCasFactory.createJCas();
		jcas.setDocumentText("The dog barks, because it is hungry.");

		export = new OCRCorrectedExport();
	}

	@Test
	public void test1() {
		AnnotationFactory.createAnnotation(jcas, 4, 7, OCRCorrection.class)
		.setCorrection("cat");
		String extext = export.getExportText(jcas);
		assertEquals("The cat barks, because it is hungry.", extext);
	}

	@Test
	public void test2() {
		AnnotationFactory.createAnnotation(jcas, 4, 7, OCRCorrection.class)
		.setCorrection("mouse");
		String extext = export.getExportText(jcas);
		assertEquals("The mouse barks, because it is hungry.", extext);
	}

	@Test
	public void test3() {
		AnnotationFactory.createAnnotation(jcas, 4, 7, OCRCorrection.class)
				.setCorrection("mouses");
		AnnotationFactory.createAnnotation(jcas, 12, 13, OCRCorrection.class)
				.setCorrection("");
		String extext = export.getExportText(jcas);
		assertEquals("The mouses bark, because it is hungry.", extext);
	}

	@Test
	public void test4() {
		AnnotationFactory.createAnnotation(jcas, 4, 7, OCRCorrection.class)
		.setCorrection("mouse");
		AnnotationFactory.createAnnotation(jcas, 4, 7, OCRCorrection.class)
		.setCorrection("cat");

		assertEquals("The mouse barks, because it is hungry.",
				export.getExportText(jcas));
	}

	@Test
	public void test5() {
		AnnotationFactory.createAnnotation(jcas, 4, 7, OCRCorrection.class)
		.setCorrection("mouse");
		AnnotationFactory.createAnnotation(jcas, 4, 13, OCRCorrection.class)
		.setCorrection("cats meaow");

		assertEquals("The cats meaow, because it is hungry.",
				export.getExportText2(jcas));
	}

}
