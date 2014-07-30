package de.nilsreiter.util.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface Database {
	@Deprecated
	public Statement getStatement() throws SQLException;

	public Connection getConnection() throws SQLException;

	public String getTableName(String baseTblSimilarities);

	public void dropTable(String table_documents) throws SQLException;
}
