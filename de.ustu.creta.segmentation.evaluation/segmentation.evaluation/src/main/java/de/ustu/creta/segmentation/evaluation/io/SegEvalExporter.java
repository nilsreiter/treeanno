package de.ustu.creta.segmentation.evaluation.io;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.json.JSONObject;

import de.nilsreiter.pipeline.segmentation.type.SegmentBoundary;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.ustu.creta.segmentation.evaluation.impl.AbstractSegEvalMetric;
import de.ustu.creta.segmentation.evaluation.impl.BoundarySimilarity_impl;

public class SegEvalExporter {

	int counter = 0;
	AbstractSegEvalMetric metric = new BoundarySimilarity_impl(
			SegmentBoundary.class);

	JSONObject items = new JSONObject();

	public void collect(JCas gold, JCas silver) {
		String did = null;
		if (JCasUtil.exists(gold, DocumentMetaData.class)) {
			DocumentMetaData dmd =
					JCasUtil.selectSingle(gold, DocumentMetaData.class);
			did = dmd.getDocumentId();
		} else {
			did = String.valueOf(counter++);
		}
		JSONObject obj = new JSONObject();
		obj.put("1", metric.getMassTuple(gold, SegmentBoundary.class));
		obj.put("2", metric.getMassTuple(gold, SegmentBoundary.class));
		items.put(did, obj);

	};

	public JSONObject exportToJSON() {
		JSONObject b = new JSONObject();
		b.put("items", items);
		b.put("segmentation_type", "linear");

		return b;
	}

}
