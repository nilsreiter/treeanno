package de.uniheidelberg.cl.a10.data2.alignment.io;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.SQLBuilder;

public class DBAlignment {

	Database database;

	public DBAlignment(Database database) {
		super();
		this.database = database;
	}

	public static String table = "alignments";

	private static String TBL_STRUCT =
			"databaseId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
					+ "id VARCHAR(20) NOT NULL,"
					+ "documentIds MEDIUMTEXT, xml LONGTEXT";

	public PreparedStatement getInsertStatement() throws SQLException {
		SQLBuilder b = new SQLBuilder();
		b.insert(database.getTableName(table));
		b.values("default", "?", "?", "?", "?");
		return database.getConnection().prepareStatement(b.toString());
	}

	public String getSelectStatement(String id) throws SQLException {
		SQLBuilder b = new SQLBuilder();
		b.select("id, xml").from(database.getTableName(table))
				.where("databaseId = '" + id + "'");
		return b.toString();
	}

	public void deleteTable() throws SQLException {

		database.dropTable(database.getTableName(table));

	}

	public void initTable() throws SQLException {

		SQLBuilder b = new SQLBuilder();
		b.create(database.getTableName(table)).struct(TBL_STRUCT);

		Statement stmt = database.getConnection().createStatement();
		stmt.execute(b.toString());

		stmt.close();

	}

	public Database getDatabase() {
		return database;
	}

}
