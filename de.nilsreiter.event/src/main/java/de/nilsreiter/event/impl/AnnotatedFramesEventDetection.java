package de.nilsreiter.event.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.nilsreiter.event.LocalEventDetector;
import de.uniheidelberg.cl.a10.TabFormat;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public class AnnotatedFramesEventDetection implements LocalEventDetector {
	Matrix<String, String, Boolean> matrix;

	public AnnotatedFramesEventDetection(File file) throws IOException {
		this.initFromFile(new FileInputStream(file));
	}

	public AnnotatedFramesEventDetection() throws IOException {
		this.initFromFile(AnnotatedFramesEventDetection.class
				.getResourceAsStream("FrameTargetPairs.csv"));
	}

	private void initFromFile(InputStream file) throws IOException {
		matrix = new MapMatrix<String, String, Boolean>(false);
		TabFormat tf = new TabFormat(file, ";");
		tf.process();

		for (TabFormat.Line line : tf.getLines()) {
			matrix.put(line.get(0), line.get(1), true);
		}
	}

	@Override
	public boolean isEvent(AnnotationObjectInDocument anchor) {
		Frame f = (Frame) anchor;
		return matrix.get(f.getFrameName(), f.getTarget().getLemma());
	}

	@Override
	public Class<? extends AnnotationObjectInDocument> typeRestrictor() {
		return Frame.class;
	}
}