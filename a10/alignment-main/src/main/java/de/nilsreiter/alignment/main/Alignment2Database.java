package de.nilsreiter.alignment.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.nilsreiter.util.db.DataSourceFactory;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.impl.DatabaseDataSource_impl;
import de.uniheidelberg.cl.a10.MainWithIO;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Link_impl;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentReader;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentWriter;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentWriter;
import de.uniheidelberg.cl.a10.io.DatabaseDocumentStreamProvider;

public class Alignment2Database extends MainWithIO {

	boolean pairwise = true;

	public static void main(String[] args) throws IOException {
		Alignment2Database ad = new Alignment2Database();
		ad.processArguments(args);
		ad.run();
	}

	private void run() throws IOException {
		Database database =
				new DatabaseDataSource_impl(
						DataSourceFactory.getDataSource(getConfiguration()));

		AlignmentReader<Token> alignmentReader =
				new AlignmentReader<Token>(new DatabaseDocumentStreamProvider(
						database));

		Alignment<Token> alignment =
				alignmentReader.read(new FileInputStream(input));

		Map<Token, Event> eventMap = new HashMap<Token, Event>();

		for (Document document : alignment.getDocuments()) {
			for (Event event : document.getEvents()) {
				eventMap.put(event.firstToken(), event);
			}
		}
		Alignment<Event> evAlignment =
				new Alignment_impl<Event>(alignment.getId());
		for (Link<Token> link : alignment.getAlignments()) {
			Link<Event> evlink = new Link_impl<Event>(link.getId());
			for (Token token : link.getElements()) {
				evlink.add(eventMap.get(token));
			}
			evAlignment.addAlignment(evlink.getId(), evlink);
		}
		AlignmentWriter aw = new DBAlignmentWriter(new DBAlignment(database));
		aw.write(evAlignment);
		aw.close();
	}

}
