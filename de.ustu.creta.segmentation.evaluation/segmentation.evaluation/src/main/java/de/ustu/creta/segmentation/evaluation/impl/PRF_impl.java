package de.ustu.creta.segmentation.evaluation.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.Feature;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniheidelberg.cl.reiter.util.Counter;
import de.ustu.creta.segmentation.evaluation.PRF;
import de.ustu.creta.segmentation.evaluation.Strings;

public class PRF_impl implements PRF {

	Logger logger = LoggerFactory.getLogger(getClass());
	boolean classWise = false;
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
			logger.debug(e.getLocalizedMessage());
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
		Set<String> categories = new HashSet<String>();
		for (Annotation goldAnno : JCasUtil.select(gold, annotationClass)) {
			Annotation silverAnno =
					JCasUtil.selectCovered(silver, annotationClass,
							goldAnno.getBegin(), goldAnno.getEnd()).get(0);

			String fs_gold = null, fs_silver = null;
			fs_gold = goldAnno.getStringValue(feature);
			fs_silver = silverAnno.getStringValue(feature);
			categories.add(String.valueOf(fs_gold));
			if ((fs_gold == null && fs_silver == null)
					|| (fs_gold != null && fs_gold.equals(fs_silver))) {
				tp.add(String.valueOf(fs_gold));
			} else {
				fp.add(String.valueOf(fs_silver));
				fn.add(String.valueOf(fs_gold));
			}
		}
		Map<String, Double> res;
		switch (average) {
		case Macro:
			// TODO: Implement
			return null;
		default:
			res = getMicroAverage(tp, fp, fn);
		}
		if (isClassWise()) for (String c : categories) {
				res.putAll(getPRF(tp.get(c), fp.get(c), fn.get(c), c + "_"));
			}
		return res;
	}

	Map<String, Double> getPRF(int tp, int fp, int fn, String prefix) {
		Map<String, Double> result = new HashMap<String, Double>();
		double prec = (double) tp / ((double) (tp + fp));
		double rec = (double) tp / ((double) (tp + fn));
		double f = (2 * prec * rec) / (prec + rec);
		result.put(prefix + Strings.PRECISION, prec);
		result.put(prefix + Strings.RECALL, rec);
		result.put(prefix + Strings.FSCORE, f);
		return result;
	}

	Map<String, Double> getMicroAverage(Counter<String> tpc,
			Counter<String> fpc, Counter<String> fnc) {

		int tp = sum(tpc), fp = sum(fpc), fn = sum(fnc);
		return getPRF(tp, fp, fn, "_");

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

	public boolean isClassWise() {
		return classWise;
	}

	public void setClassWise(boolean classWise) {
		this.classWise = classWise;
	}

}
