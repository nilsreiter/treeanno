package de.uniheidelberg.cl.a10.data2.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;
import de.uniheidelberg.cl.a10.data2.Document;

/**
 *
 * 
 * @author reiterns
 *
 */
public class DBDataWriter extends DataWriter {

	DBDocument database;

	public DBDataWriter(DatabaseDBConfiguration_impl db) {
		super(new ByteArrayOutputStream());
		database = new DBDocument(db);
	}

	@Override
	public void write(Document doc) throws IOException {
		try {
			super.write(doc);
			super.close();

			PreparedStatement statement =
					database.getConnection().prepareStatement(
							database.getInsertSQL());
			int fC = 1;
			statement.setString(fC++, doc.getId());
			statement.setString(fC++, doc.getCorpusName());
			statement.setString(fC++, doc.getOriginalText());
			statement.setString(fC++, outputStream.toString());
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new IOException(e);
		}

	}

}
