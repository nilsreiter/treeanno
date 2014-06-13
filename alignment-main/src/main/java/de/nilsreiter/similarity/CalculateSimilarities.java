package de.nilsreiter.similarity;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.kohsuke.args4j.Option;

import de.nilsreiter.event.similarity.FrameNet;
import de.nilsreiter.event.similarity.Null;
import de.nilsreiter.event.similarity.SimilarityDatabase;
import de.nilsreiter.event.similarity.WordNet;
import de.nilsreiter.util.db.DatabaseConfiguration;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.main.MainWithInputDocuments;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

/**
 * This class is used to calculate similarities for all pairs of events across
 * two documents. We assume documents to be eventized by some preprocessing
 * step.
 * 
 * @author reiterns
 * 
 */
public class CalculateSimilarities extends MainWithInputDocuments {

	static enum Measure {
		WN, FN, GD, AT, VN
	}

	int threadCounter = 0;

	@Option(name = "--measure", usage = "The similarity measure to use")
	Measure measure = Measure.WN;

	@Option(name = "--init", usage = "Re-initialize the database, deleting everything")
	boolean rinit = false;

	@Option(name = "--identifier", usage = "Sets an identifier for the database")
	String identifier = "";

	SimilarityFunction<Event> function = null;
	SimilarityDatabase<Event> database = null;

	public static void main(String[] args) throws IOException, SQLException {
		CalculateSimilarities cs = new CalculateSimilarities();
		cs.processArguments(args);
		cs.init();
		cs.run();
	}

	protected void init() throws SQLException, IOException {
		try {
			database = new SimilarityDatabase<Event>(
					DatabaseConfiguration.getDefaultConfiguration(), identifier);
			if (rinit)
				database.rebuild();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		switch (measure) {
		case WN:
			WordNet wns = new WordNet(new File(getConfiguration().getString(
					"paths.wnhome")), new File(getConfiguration().getString(
					"paths.nombank")));

			function = wns;
			break;
		case FN:
			FrameNet fns = new FrameNet(new File(getConfiguration().getString(
					"paths.fnhome")));
			function = fns;
			break;
		default:
			function = new Null();
		}
	}

	protected void runDocuments(Document d1, Document d2) throws SQLException {
		for (Event event1 : d1.getEvents()) {
			for (Event event2 : d2.getEvents()) {
				// while (threadCounter > 100) {
				// try {
				// Thread.sleep(10);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				new SimilarityThread(event1, event2, database).run();
			}
		}
	}

	private void run() throws IOException, SQLException {
		List<Document> docs = this.getDocuments();
		for (int i = 0; i < docs.size(); i++) {
			for (int j = i; j < docs.size(); j++) {
				this.runDocuments(docs.get(i), docs.get(j));
			}
		}
	}

	class SimilarityThread implements Runnable {

		public SimilarityThread(Event event1, Event event2,
				SimilarityDatabase<Event> database) {
			super();
			this.event1 = event1;
			this.event2 = event2;
			this.database = database;
		}

		Event event1, event2;
		SimilarityDatabase<Event> database;

		@Override
		public void run() {
			threadCounter++;
			Probability p;
			try {
				p = function.sim(event1, event2);
				database.putSimilarity(function.getClass(), event1, event2,
						p.getProbability());
			} catch (IncompatibleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				threadCounter--;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				threadCounter--;
			}
			threadCounter--;
		}

	}
}