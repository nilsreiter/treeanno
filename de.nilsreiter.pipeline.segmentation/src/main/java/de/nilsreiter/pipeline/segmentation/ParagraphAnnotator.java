package de.nilsreiter.pipeline.segmentation;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Paragraph;

public class ParagraphAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String text = jcas.getDocumentText();

		int index = 0, oldindex = 0;
		do {
			index = text.indexOf("\n\n", oldindex);
			if (index >= 0) {
				AnnotationFactory.createAnnotation(jcas, oldindex, index,
						Paragraph.class);
				oldindex = index + 2;
			}
		} while (index >= 0);
		AnnotationFactory.createAnnotation(jcas, oldindex, text.length(),
				Paragraph.class);
	}
}
