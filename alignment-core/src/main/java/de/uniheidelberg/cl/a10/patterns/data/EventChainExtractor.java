package de.uniheidelberg.cl.a10.patterns.data;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.TabFormat;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.data2.impl.FrameEvent_impl;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public abstract class EventChainExtractor {

	public enum Extraction {
		ALLFRAMES, ANNOTATEDFRAMES
	};

	protected EventChainExtractor() {
	};

	public static EventChainExtractor getEventChainExtractor(
			final Extraction extraction) throws IOException {
		switch (extraction) {
		case ANNOTATEDFRAMES:
			return new AnnotatedFrames(new File("data/FrameTargetPairs.csv"));

		case ALLFRAMES:
		default:
			return new AllFrames();
		}
	};

	public abstract List<Event> getEventChain(final Document rd);

	public abstract Alignment<Event> getAlignmentDocument(
			final Alignment<Frame> ad);
}

class AnnotatedFrames extends EventChainExtractor {

	Matrix<String, String, Boolean> matrix;

	public AnnotatedFrames(final File file) throws IOException {
		matrix = new MapMatrix<String, String, Boolean>(false);
		TabFormat tf = new TabFormat(file, ";");
		tf.process();

		for (TabFormat.Line line : tf.getLines()) {
			matrix.put(line.get(0), line.get(1), true);
		}
	}

	@Override
	public List<Event> getEventChain(final Document rd) {
		List<Event> ret = new LinkedList<Event>();
		for (Frame f : rd.getFrames()) {
			if (matrix.get(f.getFrameName(), f.getTarget().getLemma()))
				ret.add(new FrameEvent_impl(f));
		}
		return ret;
	}

	@Override
	public Alignment<Event> getAlignmentDocument(final Alignment<Frame> ad) {
		Alignment<Event> ret = new Alignment_impl<Event>(ad.getId());
		for (Link<Frame> aa : ad.getAlignments()) {
			HashSet<Event> events = new HashSet<Event>();
			for (Frame f : aa.getElements()) {
				if (matrix.get(f.getFrameName(), f.getTarget().getLemma()))
					events.add(new FrameEvent_impl(f));
			}
			if (!events.isEmpty())
				ret.addAlignment(aa.getId(), events);
		}
		return ret;
	}

}

class AllFrames extends EventChainExtractor {

	@Override
	public List<Event> getEventChain(final Document rd) {
		List<Event> ret = new LinkedList<Event>();
		for (Frame f : rd.getFrames()) {
			ret.add(new FrameEvent_impl(f));
		}
		return ret;
	}

	@Override
	public Alignment<Event> getAlignmentDocument(final Alignment<Frame> ad) {
		Alignment<Event> ret = new Alignment_impl<Event>(ad.getId());
		for (Link<Frame> aa : ad.getAlignments()) {
			HashSet<Event> events = new HashSet<Event>();
			for (Frame f : aa.getElements()) {
				events.add(new FrameEvent_impl(f));
			}
			ret.addAlignment(aa.getId(), events);
		}
		return ret;
	}

}
