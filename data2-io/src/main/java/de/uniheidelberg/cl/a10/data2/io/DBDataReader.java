package de.uniheidelberg.cl.a10.data2.io;

import java.io.IOException;
import java.sql.SQLException;

import nu.xom.ParsingException;
import nu.xom.ValidityException;
import de.nilsreiter.util.db.Database;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.io.DatabaseDocumentStreamProvider;

public class DBDataReader {
	DatabaseDocumentStreamProvider ddsp;
	DataReader dataReader;

	public DBDataReader(Database db) {
		dataReader = new DataReader();
		ddsp = new DatabaseDocumentStreamProvider(db);
	}

	public Document read(String id) throws SQLException, IOException,
	ValidityException, ParsingException {
		return dataReader.read(ddsp.findStreamFor(id));
	}
}
