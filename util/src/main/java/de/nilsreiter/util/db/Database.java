package de.nilsreiter.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private Connection connection = null;

	public Database(DatabaseConfiguration dbc) throws SQLException,
			ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");

		connection = DriverManager.getConnection(dbc.getURL(),
				dbc.getUsername(), dbc.getPassword());
	}

	protected Statement getStatement() throws SQLException {
		return connection.createStatement();
	}

	public Connection getConnection() {
		return connection;
	}
}