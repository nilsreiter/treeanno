package de.nilsreiter.pipeline.segmentation.morphadorner;

import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import edu.northwestern.at.morphadorner.corpuslinguistics.stemmer.PorterStemmer;
import edu.northwestern.at.morphadorner.corpuslinguistics.stopwords.BaseStopWords;
import edu.northwestern.at.morphadorner.corpuslinguistics.textsegmenter.C99TextSegmenter;

@TypeCapability(
		inputs = { "de.nilsreiter.pipeline.segmentation.type.SegmentationUnit",
				"de.nilsreiter.pipeline.segmentation.type.SegmentationSubUnit" },
		outputs = { "de.nilsreiter.pipeline.segmentation.type.SegmentBoundary" })
public class C99Annotator extends MASegmenter {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		this.createTokenLists(aJCas);

		C99TextSegmenter c99 = new C99TextSegmenter();
		c99.setSegmentsWanted(10);
		c99.setMaskSize(5);
		c99.setStopWords(new BaseStopWords());
		c99.setStemmer(new PorterStemmer());

		List<Integer> segments = c99.getSegmentPositions(this.tokenSurfaces);

		for (Integer i : segments) {
			int b = annotationList.get(i).getBegin();
			AnnotationFactory.createAnnotation(aJCas, b, b + 1,
					SegmentBoundary.class);
		}
	}
}
