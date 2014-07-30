package de.uniheidelberg.cl.a10.data2.io;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import de.nilsreiter.util.db.DBUtils;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.SQLBuilder;
import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.data2.impl.DocumentSet_impl;
import de.uniheidelberg.cl.a10.io.AbstractLinkedXMLReader;
import de.uniheidelberg.cl.a10.io.DatabaseDocumentStreamProvider;

public class DBDocumentSetReader extends AbstractLinkedXMLReader<DocumentSet> {
	Database database;

	String tableName = "documentSets";

	public DBDocumentSetReader(Database db) throws SQLException {
		super(new DatabaseDocumentStreamProvider(db));
		database = db;
	}

	public DBDocumentSetReader(Database db, DataStreamProvider dsp)
			throws SQLException {
		super(dsp);
		database = db;
	}

	public DocumentSet read(String id) throws SQLException, ValidityException,
			IOException, ParsingException {
		SQLBuilder b = new SQLBuilder();
		b.select("*").from(database.getTableName(tableName))
				.where("id = '" + id + "'");
		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(b.toString());
		DocumentSet ds = new DocumentSet_impl(id);
		if (rs.first()) {
			for (String s : rs.getString(2).split(",")) {
				ds.add(getRitualDocument(s));
			}
		}
		ds.setTitle(rs.getString(3));
		DBUtils.closeAll(rs);
		return ds;
	}

	@Override
	protected DocumentSet read(Element rootElement) throws IOException {
		// empty, because not used
		throw new UnsupportedOperationException();
	}

}
