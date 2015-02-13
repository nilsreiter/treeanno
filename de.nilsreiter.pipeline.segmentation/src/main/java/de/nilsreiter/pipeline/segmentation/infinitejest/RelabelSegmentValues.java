package de.nilsreiter.pipeline.segmentation.infinitejest;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.nilsreiter.pipeline.segmentation.type.Segment;

public class RelabelSegmentValues extends JCasAnnotator_ImplBase {

	Map<String, Integer> mapping = new HashMap<String, Integer>();

	int nextNumber = 0;

	@Override
	public void initialize(final UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
		mapping.put("AFR", 2);
		mapping.put("EHDRH", 0);
		mapping.put("ETA", 1);
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		for (Segment segment : JCasUtil.select(aJCas, Segment.class)) {
			String v = segment.getValue();
			if (v != null) {
				if (mapping.containsKey(v)) {
					segment.setValue(String.valueOf(mapping.get(v)));
				} else {
					mapping.put(v, nextNumber++);
					segment.setValue(String.valueOf(mapping.get(v)));
				}
			}
		}
	}

}
