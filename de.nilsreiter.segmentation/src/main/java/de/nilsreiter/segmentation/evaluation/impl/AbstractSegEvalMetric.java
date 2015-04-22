package de.nilsreiter.segmentation.evaluation.impl;

import java.util.Properties;

import org.apache.commons.io.IOUtils;
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

}
