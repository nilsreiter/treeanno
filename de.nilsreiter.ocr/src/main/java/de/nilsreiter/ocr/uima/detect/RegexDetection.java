package de.nilsreiter.ocr.uima.detect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public abstract class RegexDetection extends JCasAnnotator_ImplBase {

	Pattern pattern;

	public void markRegex(JCas jcas, Class<? extends Annotation> annoClass) {
		String text = jcas.getDocumentText();

		Matcher m = pattern.matcher(text);
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			AnnotationFactory.createAnnotation(jcas, start, end, annoClass);
		}
	}

}
