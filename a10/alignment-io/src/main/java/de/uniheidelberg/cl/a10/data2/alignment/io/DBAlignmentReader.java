package de.uniheidelberg.cl.a10.data2.alignment.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import nu.xom.ParsingException;
import nu.xom.ValidityException;
import de.nilsreiter.util.db.DBUtils;
import de.nilsreiter.util.db.Database;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.io.DatabaseDocumentStreamProvider;
import de.uniheidelberg.cl.a10.io.DocumentNotFoundException;

public class DBAlignmentReader<T extends HasDocument> extends
AlignmentReader<T> {
	DBAlignment database;

	public DBAlignmentReader(Database db) throws SQLException {
		super(new DatabaseDocumentStreamProvider(db));
		database = new DBAlignment(db);
	}

	public Alignment<T> read(String id) throws SQLException, ValidityException,
			IOException, ParsingException {

		// System.err.println(b.toString());
		Connection connection = database.getDatabase().getConnection();

		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(database.getSelectStatement(id));
		if (rs.first()) {
			byte[] arr = rs.getString(2).getBytes();
			// SQLXML sx = rs.getSQLXML(2);
			Alignment_impl<T> al =
					(Alignment_impl<T>) super
					.read(new ByteArrayInputStream(arr));
			al.setId(id);
			al.setTitle(rs.getString(1));
			DBUtils.closeAll(rs);
			return al;
		}
		throw new DocumentNotFoundException(id);
	}

}
