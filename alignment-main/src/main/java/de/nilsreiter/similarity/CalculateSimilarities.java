package de.nilsreiter.similarity;

import java.io.IOException;

import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.main.MainWithInputDocuments;

/**
 * This class is used to calculate similarities for all pairs of events across
 * two documents. We assume documents to be eventized by some preprocessing
 * step.
 * 
 * @author reiterns
 * 
 */
public class CalculateSimilarities extends MainWithInputDocuments {

	@Option(name = "--measure")
	String measure = "";

	public static void main(String[] args) throws IOException {
		CalculateSimilarities cs = new CalculateSimilarities();
		cs.processArguments(args);
		cs.run();
	}

	private void run() throws IOException {

		for (Event event1 : getDocuments().get(0).getEvents()) {
			for (Event event2 : getDocuments().get(1).getEvents()) {

			}
		}
	}
}