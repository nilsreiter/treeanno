package de.nilsreiter.pipeline.segmentation.morphadorner;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import de.nilsreiter.pipeline.segmentation.type.SegmentationSubUnit;
import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;

public abstract class MASegmenter extends JCasAnnotator_ImplBase {

	protected List<List<String>> tokenSurfaces = new LinkedList<List<String>>();
	protected List<Annotation> annotationList = new LinkedList<Annotation>();

	protected synchronized void createTokenLists(JCas aJCas) {
		for (SegmentationUnit sentence : JCasUtil.select(aJCas,
				SegmentationUnit.class)) {
			List<String> sentenceList = new LinkedList<String>();
			for (SegmentationSubUnit token : JCasUtil.selectCovered(aJCas,
					SegmentationSubUnit.class, sentence)) {
				sentenceList.add(token.getCoveredText());
				annotationList.add(token);
			}
			tokenSurfaces.add(sentenceList);
	
		}
	}

}
