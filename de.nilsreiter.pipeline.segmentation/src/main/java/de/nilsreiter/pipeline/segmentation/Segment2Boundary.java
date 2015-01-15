package de.nilsreiter.pipeline.segmentation;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.type.Segment;
import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;

@TypeCapability(
		inputs = { "de.nilsreiter.pipeline.segmentation.type.Segment" },
		outputs = { "de.nilsreiter.pipeline.segmentation.type.SegmentBoundary" })
public class Segment2Boundary extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Segment segment : JCasUtil.select(jcas, Segment.class)) {
			AnnotationFactory.createAnnotation(jcas, segment.getBegin(),
					segment.getBegin() + 1, SegmentBoundary.class);
		}

	}

}
