package de.uniheidelberg.cl.a10.data2.io;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import de.nilsreiter.util.db.DBUtils;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.SQLBuilder;

public class DBDocumentSet {
	Database database;

	public static String tableName = "documentSets";

	public PreparedStatement getInsertStatement() throws SQLException {
		SQLBuilder b = new SQLBuilder();
		b.insert(database.getTableName(tableName)).values("default", "?", "?");

		return database.getConnection().prepareStatement(b.toString(),
				Statement.RETURN_GENERATED_KEYS);
	}

	public DBDocumentSet(Database database) {
		super();
		this.database = database;
	}

	protected void initTable() throws SQLException {

		SQLBuilder sb = new SQLBuilder();
		sb.create(database.getTableName(tableName))
		.struct("id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, document TEXT, documentset TEXT");

		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute(sb.toString());
		DBUtils.close(stmt);
		DBUtils.close(conn);

	}

	public void deleteDocumentSet(String id) throws SQLException {
		SQLBuilder sb = new SQLBuilder();
		sb.delete(database.getTableName(tableName)).where(
				"documentset=\"" + id + "\"");

		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute(sb.toString());
		DBUtils.close(stmt);
		DBUtils.close(conn);

	}

	public void initIfTableNotExists() throws SQLException {
		SQLBuilder sb = new SQLBuilder();
		sb.createIfNotExists(database.getTableName(tableName))
		.struct("id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, document TEXT, documentset TEXT");

		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute(sb.toString());
		DBUtils.close(stmt);
		DBUtils.close(conn);

	}

	public void deleteTable() throws SQLException {
		SQLBuilder b = new SQLBuilder();
		b.drop(database.getTableName(tableName));
		Connection conn = database.getConnection();
		Statement stmt = conn.createStatement();
		stmt.execute(b.toString());
		DBUtils.close(stmt);
		DBUtils.close(conn);

	}

	protected Connection getConnection() throws SQLException {
		return database.getConnection();
	}
}
