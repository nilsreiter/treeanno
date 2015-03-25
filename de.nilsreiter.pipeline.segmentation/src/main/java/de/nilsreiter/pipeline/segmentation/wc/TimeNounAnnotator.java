package de.nilsreiter.pipeline.segmentation.wc;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.wc.type.TimeNoun;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN;

public class TimeNounAnnotator extends JCasAnnotator_ImplBase {

	public final static String RESOURCE_LIST = "List";

	@ExternalResource(key = RESOURCE_LIST)
	private WordSet model;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		for (NN token : JCasUtil.select(aJCas, NN.class)) {
			if (model.contains(token.getCoveredText()))
				AnnotationFactory.createAnnotation(aJCas, token.getBegin(),
						token.getEnd(), TimeNoun.class);
		}
	}

}
