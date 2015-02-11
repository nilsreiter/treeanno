package de.nilsreiter.segmentation.evaluation.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.Feature;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.segmentation.evaluation.PRF;
import de.nilsreiter.segmentation.evaluation.Strings;
import de.uniheidelberg.cl.reiter.util.Counter;

public class PRF_impl implements PRF {

	Logger logger = LoggerFactory.getLogger(getClass());

	String featureName = "Value";
	Feature feature;
	Class<? extends Annotation> annotationClass;
	Average average = Average.Micro;

	public PRF_impl(Class<? extends Annotation> annoClass) {
		annotationClass = annoClass;
	}

	public boolean init(JCas gold) {
		try {
			feature =
					gold.getRequiredFeature(
							gold.getTypeSystem().getType(
									annotationClass.getCanonicalName()),
							featureName);
		} catch (CASRuntimeException e) {
			e.printStackTrace();
			return false;
		} catch (CASException e) {
			e.printStackTrace();
			return false;
		}
		return JCasUtil.exists(gold, annotationClass);
	}

	public Map<String, Double> score(JCas gold, JCas silver) {
		int goldNumber = JCasUtil.select(gold, annotationClass).size();
		int silverNumber = JCasUtil.select(silver, annotationClass).size();
		if (goldNumber != silverNumber) {
			logger.error("Number of annotations not matching.");
			throw new RuntimeException(
					"Number of annotations not matching (gold: " + goldNumber
					+ ", silver: " + silverNumber + ").");
		}
		// int fp = 0, fn = 0;
		Counter<String> tp = new Counter<String>();
		Counter<String> fp = new Counter<String>();
		Counter<String> fn = new Counter<String>();
		for (Annotation goldAnno : JCasUtil.select(gold, annotationClass)) {
			Annotation silverAnno =
					JCasUtil.selectCovered(silver, annotationClass,
							goldAnno.getBegin(), goldAnno.getEnd()).get(0);
			String fs_gold = goldAnno.getStringValue(feature);
			String fs_silver = silverAnno.getStringValue(feature);
			if (fs_gold.equals(fs_silver)) {
				tp.add(fs_gold.toString());
			} else {
				fp.add(fs_silver.toString());
				fn.add(fs_gold.toString());
			}
		}
		switch (average) {
		case Macro:
			// TODO: Implement
			return null;
		default:
			return getMicroAverage(tp, fp, fn);
		}
	}

	Map<String, Double> getMicroAverage(Counter<String> tpc,
			Counter<String> fpc, Counter<String> fnc) {
		Map<String, Double> result = new HashMap<String, Double>();

		int tp = sum(tpc), fp = sum(fpc), fn = sum(fnc);

		double prec = (double) tp / ((double) (tp + fp));
		double rec = (double) tp / ((double) (tp + fn));
		double f = (2 * prec * rec) / (prec + rec);
		result.put(Strings.PRECISION, prec);
		result.put(Strings.RECALL, rec);
		result.put(Strings.FSCORE, f);
		return result;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public Class<? extends Annotation> getAnnotationClass() {
		return annotationClass;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public Average getAverage() {
		return average;
	}

	public void setAverage(Average average) {
		this.average = average;
	}

	public int sum(Counter<?> c) {
		int i = 0;
		for (Integer o : c.values()) {
			i += o;
		}
		return i;
	}

}
