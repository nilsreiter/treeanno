package de.uniheidelberg.cl.a10.patterns.main;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.main.MainWithInputDocuments;
import de.uniheidelberg.cl.a10.patterns.data.EventChainExtractor;

public abstract class MainWithInputSequences extends MainWithInputDocuments {

	@Option(
			name = "--extraction",
			usage = "Controls the pre-filtering of frames into events. Deprecated. ")
	protected EventChainExtractor.Extraction extraction =
			EventChainExtractor.Extraction.ANNOTATEDFRAMES;

	List<List<FrameTokenEvent>> sequences = null;

	@Deprecated
	EventChainExtractor eventChainExtractor;

	public List<List<FrameTokenEvent>> getSequences()
			throws ParserConfigurationException, SAXException, IOException {
		if (sequences == null) {
			sequences = new LinkedList<List<FrameTokenEvent>>();
			eventChainExtractor =
					EventChainExtractor.getEventChainExtractor(extraction);
			for (Document rd : this.getDocuments()) {
				sequences.add(eventChainExtractor.getEventChain(rd));
			}
		}
		return sequences;
	}

	public EventChainExtractor getEventChainExtractor() {
		return eventChainExtractor;
	}
}