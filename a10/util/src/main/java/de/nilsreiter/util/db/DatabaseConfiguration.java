package de.nilsreiter.util.db;

import org.apache.commons.configuration.Configuration;

public class DatabaseConfiguration {

	String host;
	String username;
	String password;
	String database;
	int port = 3306;
	String protocol = "mysql";
	String prefix = "";

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public DatabaseConfiguration(String host, String username, String password,
			String database, String port, String protocol) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.database = database;
		this.port = Integer.valueOf(port);
		this.protocol = protocol;
	}

	public DatabaseConfiguration(String host, String username, String password,
			String database, int port, String protocol, String prefix) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.database = database;
		this.port = port;
		this.protocol = protocol;
		this.prefix = prefix;
	}

	public DatabaseConfiguration(String host, String username, String password,
			String database) {
		super();
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public String getURL() {
		StringBuilder b = new StringBuilder();
		b.append("jdbc:").append(protocol).append("://");
		b.append(host).append(':').append(port);
		b.append('/');
		b.append(database);

		return b.toString();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public static DatabaseConfiguration getDefaultConfiguration() {
		return new DatabaseConfiguration("waitahapinguin", "reiterns",
				"bybNoaKni", "reiter", "3306", "mysql");
	}

	public static DatabaseConfiguration getLocalConfiguration() {
		return new DatabaseConfiguration("localhost", "reiterns", "bybNoaKni",
				"reiter", "3306", "mysql");
	}

	public static DatabaseConfiguration getDatabaseConfiguration(
			Configuration config) {
		DatabaseConfiguration dbConf =
				new DatabaseConfiguration(config.getString("database.host"),
						config.getString("database.username"),
						config.getString("database.password"),
						config.getString("database.name"));
		dbConf.setPort(config.getInt("database.port"));
		return dbConf;
	}
}