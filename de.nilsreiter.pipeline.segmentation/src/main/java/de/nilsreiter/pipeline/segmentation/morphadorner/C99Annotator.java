package de.nilsreiter.pipeline.segmentation.morphadorner;

import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
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

	public static final String PARAM_MASK_SIZE = "Mask Size";
	public static final String PARAM_SEGMENTS_WANTED = "Segments Wanted";

	@ConfigurationParameter(name = PARAM_MASK_SIZE, mandatory = false,
			defaultValue = "11")
	int maskSize = 11;
	@ConfigurationParameter(name = PARAM_SEGMENTS_WANTED, mandatory = false,
			defaultValue = "-1")
	int segmentsWanted = -1;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		this.createTokenLists(aJCas);

		C99TextSegmenter c99 = new C99TextSegmenter();
		c99.setSegmentsWanted(segmentsWanted);
		c99.setMaskSize(maskSize);
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
