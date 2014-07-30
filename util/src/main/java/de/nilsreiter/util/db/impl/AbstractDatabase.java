package de.nilsreiter.util.db.impl;

import java.sql.SQLException;
import java.sql.Statement;

import de.nilsreiter.util.db.Database;

public abstract class AbstractDatabase implements Database {
	String prefix = "";
	private static String DROP_TABLE = "DROP TABLE IF EXISTS";

	@Override
	public String getTableName(String s) {
		return prefix + s;
	};

	@Override
	public void dropTable(String tbl) throws SQLException {
		StringBuilder b = new StringBuilder();
		b.append(DROP_TABLE).append(" ");
		b.append(tbl).append(';');

		Statement stmt = getConnection().createStatement();
		stmt.execute(b.toString());
		stmt.close();
	}
}
