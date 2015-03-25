package de.nilsreiter.pipeline.segmentation.wc;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.nilsreiter.pipeline.segmentation.wc.type.TimeAdverb;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADV;

@TypeCapability(
		inputs = { "de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADV" },
		outputs = { "de.nilsreiter.pipeline.segmentation.wc.type.TimeAdverb" })
public class TimeAdverbAnnotator extends JCasAnnotator_ImplBase {

	String[] timeAdverbs = new String[] { "after", "always", "before",
			"during", "lately", "never", "often", "rarely", "recently",
			"sometimes", "soon", "today" };

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		for (ADV token : JCasUtil.select(aJCas, ADV.class)) {
			if (ArrayUtils.contains(timeAdverbs, token.getCoveredText()))
				AnnotationFactory.createAnnotation(aJCas, token.getBegin(),
						token.getEnd(), TimeAdverb.class);
		}
	}
}
