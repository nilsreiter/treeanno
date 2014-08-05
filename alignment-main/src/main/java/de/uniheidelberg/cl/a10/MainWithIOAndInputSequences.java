package de.uniheidelberg.cl.a10;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.main.MainWithInputDocuments;
import de.uniheidelberg.cl.a10.patterns.data.EventChainExtractor;

/**
 * Main class with predefined input, output and file arguments.
 * 
 * @author reiter
 * 
 */
@Deprecated
public abstract class MainWithIOAndInputSequences extends
MainWithInputDocuments {
	@Option(name = "--extraction",
			usage = "Controls the pre-filtering of frames into events")
	protected EventChainExtractor.Extraction extraction =
			EventChainExtractor.Extraction.ANNOTATEDFRAMES;

	List<List<FrameTokenEvent>> sequences = null;

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

	@Option(name = "--input", aliases = { "-i" },
			usage = "The input file. If not set, System.in is used.")
	File input = null;

	@Option(name = "--output",
			// aliases = { "-o" },
			usage = "The output file. If not set, System.out is used.")
	File output = null;

	public InputStream getInputStream() {
		try {
			return this.getInputStreamForFileOption(input);
		} catch (FileNotFoundException e) {
			logger.error("File {} not found.", input.getName());
			return System.in;
		}
	}

	public OutputStream getOutputStream() {
		return this.getOutputStreamForFileOption(output, System.out);
	}
}
