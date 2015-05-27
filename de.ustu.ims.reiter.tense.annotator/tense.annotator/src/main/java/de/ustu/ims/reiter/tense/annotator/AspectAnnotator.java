package de.ustu.ims.reiter.tense.annotator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.TypeCapability;
import org.apache.uima.jcas.JCas;

@TypeCapability(inputs = {
		"de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS",
"de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence" },
outputs = { "de.nilsreiter.pipeline.tense.type.Aspect" })
@Deprecated
public class AspectAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		// Intentionally empty
	}

}
