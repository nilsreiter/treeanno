package de.uniheidelberg.cl.a10.data2.io;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.nilsreiter.util.db.DBUtils;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.SQLBuilder;
import de.uniheidelberg.cl.a10.io.DocumentNotFoundException;

public class DBDocument {
	Database database;

	public static String table_documents = "documents";

	private static String TBL_STRUCT_DOCUMENTS =
			"databaseId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
					+ "id VARCHAR(20) NOT NULL,corpus VARCHAR(20) DEFAULT NULL,"
					+ "text TEXT, xml LONGTEXT";

	protected String getInsertSQL() {
		SQLBuilder b = new SQLBuilder();
		b.insert(database.getTableName(table_documents));
		b.values("default", "?", "?", "?", "?");
		return b.toString();
	}

	public PreparedStatement getInsertStatement() throws SQLException {
		return database.getConnection().prepareStatement(getInsertSQL());
	}

	public InputStream getXMLStreamForId(String id) throws IOException {
		SQLBuilder sb = new SQLBuilder();
		sb.select("xml").from(database.getTableName(table_documents))
				.where("id='" + id + "'");
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = database.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sb.toString());
			if (rs.first()) {
				return rs.getSQLXML(1).getBinaryStream();
			}
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
			DBUtils.close(conn);
		}
		throw new DocumentNotFoundException(id);
	}

	public DBDocument(Database database) {
		super();
		this.database = database;
	}

	public void initDatabase() throws SQLException {

		SQLBuilder b = new SQLBuilder();
		b.create(database.getTableName(table_documents)).struct(
				TBL_STRUCT_DOCUMENTS);

		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute(b.toString());

		DBUtils.close(stmt);
		DBUtils.close(conn);
	}

	public void deleteTable() throws SQLException {
		SQLBuilder b = new SQLBuilder();
		b.drop(database.getTableName(table_documents));
		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute(b.toString());
		stmt.close();
		DBUtils.close(stmt);
		DBUtils.close(conn);

	}

	protected Connection getConnection() throws SQLException {
		return database.getConnection();
	}
}
