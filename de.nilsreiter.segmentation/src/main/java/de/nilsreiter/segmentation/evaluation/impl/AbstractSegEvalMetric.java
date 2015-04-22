package de.nilsreiter.segmentation.evaluation.impl;

import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.uima.jcas.tcas.Annotation;
import org.python.core.PyInteger;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

public abstract class AbstractSegEvalMetric {
	PythonInterpreter interpreter = null;

	protected boolean ensureInterpreter() {
		try {
			if (interpreter == null) {
				Properties props = new Properties();
				props.setProperty(
						"python.path",
						"/Users/reiterns/Documents/Java/de.nilsreiter.segmentation/src/main/resources/python/segeval-2.0.11");
				PythonInterpreter.initialize(props, props, new String[0]);
				interpreter = new PythonInterpreter();
				// interpreter.exec("import sys");
				// interpreter.exec("print sys.path");
				interpreter.exec("import segeval");
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			IOUtils.closeQuietly(interpreter);
		}
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

}
