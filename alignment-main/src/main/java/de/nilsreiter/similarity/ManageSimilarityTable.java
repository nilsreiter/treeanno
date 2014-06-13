package de.nilsreiter.similarity;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import de.nilsreiter.event.similarity.SimilarityDatabase;
import de.nilsreiter.util.db.DatabaseConfiguration;
import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.data2.Event;

public class ManageSimilarityTable extends Main {

	enum Action {
		INIT, NOOP, REMOVE_TYPE
	}

	@Option(name = "--action")
	Action action = Action.NOOP;

	@Argument
	List<String> arguments = new LinkedList<String>();

	SimilarityDatabase<Event> database = null;

	@Option(name = "--identifier", usage = "Sets an identifier for the database")
	String identifier;

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		ManageSimilarityTable mst = new ManageSimilarityTable();
		mst.processArguments(args);
		mst.run();
	}

	private void run() throws ClassNotFoundException, SQLException {
		database = new SimilarityDatabase<Event>(
				DatabaseConfiguration.getDefaultConfiguration(), identifier);
		switch (action) {
		case INIT:
			database.rebuild();
			break;
		case REMOVE_TYPE:
			for (String s : arguments) {
				database.dropType(s);
			}
			break;
		case NOOP:
		default:
			System.exit(0);
		}

	}

}
