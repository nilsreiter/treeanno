package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.InputStream;

import de.nilsreiter.util.db.Database;
import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.io.DBDocument;

public class DatabaseDocumentStreamProvider implements DataStreamProvider {

	DBDocument database;

	public DatabaseDocumentStreamProvider(Database database) {
		super();
		this.database = new DBDocument(database);
	}

	@Override
	public InputStream findStreamFor(String id) throws IOException {
		return database.getXMLStreamForId(id);
	}

	@Override
	public InputStream findStreamFor(String objectName, String type)
			throws IOException {
		return database.getXMLStreamForId(objectName);
	}

}
