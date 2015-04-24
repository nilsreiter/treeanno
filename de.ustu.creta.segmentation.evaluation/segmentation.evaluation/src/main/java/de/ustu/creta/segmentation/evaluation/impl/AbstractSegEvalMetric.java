package de.ustu.creta.segmentation.evaluation.impl;

import java.util.Collection;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.python.core.PyDictionary;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

import de.nilsreiter.pipeline.segmentation.type.SegmentationUnit;

public abstract class AbstractSegEvalMetric {
	PythonInterpreter interpreter = null;

	/**
	 * Maximum distance (in potential boundary positions) that a transposition
	 * may span.
	 * 
	 * See {@link http://segeval.readthedocs.org/en/latest/api/#segeval.boundary_edit_distance}
	 */
	int maxNearMiss = 2;

	protected boolean ensureInterpreter() {
		try {
			if (interpreter == null) {
				PythonInterpreter.initialize(System.getProperties(),
						System.getProperties(), new String[0]);
				interpreter = new PythonInterpreter();
				interpreter.exec("import segeval");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {}
	}

	public PyTuple getMassTuple(JCas jcas,
			Class<? extends Annotation> boundaryType) {
		Collection<? extends Annotation> boundaries =
				JCasUtil.select(jcas, boundaryType);

		PyInteger[] masses = new PyInteger[boundaries.size() + 1];
		int i = 0, end = jcas.getDocumentText().length();
		Annotation prevAnno = null;
		Collection<? extends Annotation> coll;
		for (Annotation anno : boundaries) {
			if (prevAnno == null) {
				coll =
						JCasUtil.selectPreceding(SegmentationUnit.class, anno,
								Integer.MAX_VALUE);
			} else {
				coll =
						JCasUtil.selectBetween(SegmentationUnit.class,
								prevAnno, anno);
			}
			// System.err.println(i + ": " + coll.toString());
			masses[i++] = new PyInteger(coll.size());
			prevAnno = anno;
		}
		coll = null;
		if (prevAnno == null) {
			coll = JCasUtil.select(jcas, SegmentationUnit.class);
		} else
			coll =
					JCasUtil.selectBetween(SegmentationUnit.class, prevAnno,
							new Annotation(jcas, end, end));
		if (coll != null) {
			// System.err.println(i + ": " + coll.toString());
			masses[i] = new PyInteger(coll.size());
		}

		return new PyTuple(masses);
	}

	PyTuple getMassTuple(Collection<? extends Annotation> annotations,
			int length) {
		PyInteger[] silverMasses = new PyInteger[annotations.size() + 1];
		int index = 0;
		int i = 0;
		for (Annotation anno : annotations) {
			silverMasses[i++] = new PyInteger(anno.getBegin() - index);
			index = anno.getBegin();
		}
		silverMasses[i++] = new PyInteger(length - index);
		return new PyTuple(silverMasses);
	}

	double getPyFunctionValue(PyTuple seg1, PyTuple seg2, String function) {
		interpreter.set("seg1", seg1);
		interpreter.set("seg2", seg2);

		interpreter.exec("result = " + function + "(seg1, seg2,n_t="
				+ maxNearMiss + ")");
		PyObject obj = interpreter.get("result");
		return obj.asDouble();
	}

	double getPyFunctionValueFromDictionary(PyTuple seg1, PyTuple seg2,
			String function) {
		PyDictionary dict =
				new PyDictionary(new PyObject[] {
						new PyString("data"),
						new PyDictionary(new PyObject[] { new PyInteger(1),
								seg1, new PyInteger(2), seg2 }) });
		interpreter.set("dataset", dict);
		interpreter.exec("result = " + function + "(dataset,n_t=" + maxNearMiss
				+ ")");
		PyObject obj = interpreter.get("result");
		return obj.asDouble();
	}

	public int getMaxNearMiss() {
		return maxNearMiss;
	}

	public void setMaxNearMiss(int maxNearMiss) {
		this.maxNearMiss = maxNearMiss;
	}

}
