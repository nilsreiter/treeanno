package de.uniheidelberg.cl.a10.data2.io;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.nilsreiter.util.StringUtil;
import de.nilsreiter.util.db.DBUtils;
import de.nilsreiter.util.db.Database;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;

public class DBDocumentSetWriter implements Writer<DocumentSet> {
	DBDocumentSet database;

	String tableName = "documentSets";

	int lastCreatedId;

	public DBDocumentSetWriter(Database db) throws SQLException {
		database = new DBDocumentSet(db);
	}

	@Override
	public void write(DocumentSet object) throws IOException {
		PreparedStatement stmt = null;
		try {
			stmt = database.getInsertStatement();
			stmt.setString(1, StringUtil.join(object.getSet(),
					new StringUtil.ToString<Document>() {
				@Override
				public String toString(Document obj) {
					return obj.getId();
				}
			}, ","));
			stmt.setString(2, object.getId());
			stmt.executeUpdate();

			ResultSet generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				lastCreatedId = generatedKeys.getInt(1);
			}
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			DBUtils.close(stmt);
		}
	}

	public int getLastCreatedId() {
		return lastCreatedId;
	}

}
