package de.nilsreiter.util.db.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.configuration.Configuration;

public class DatabaseFromConfiguration_impl extends AbstractDatabase {
	Connection connection;

	public DatabaseFromConfiguration_impl(Configuration configuration)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		StringBuilder b = new StringBuilder();
		b.append("jdbc:mysql://").append(
				configuration.getString("database.host"));
		b.append(":").append(configuration.getString("database.port"));
		b.append("/").append(configuration.getString("database.name"));
		connection =
				DriverManager.getConnection(b.toString(),
						configuration.getString("database.username"),
						configuration.getString("database.password"));
	}

	@Override
	@Deprecated
	public Statement getStatement() throws SQLException {
		return null;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}
}
