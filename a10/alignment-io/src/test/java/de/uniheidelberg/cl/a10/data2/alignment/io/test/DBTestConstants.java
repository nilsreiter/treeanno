package de.uniheidelberg.cl.a10.data2.alignment.io.test;

import de.nilsreiter.util.db.DatabaseConfiguration;

public class DBTestConstants {
	public static final DatabaseConfiguration dbConf =
			new DatabaseConfiguration("localhost", "reiterns", "password",
					"documents", 3306, "mysql", "test");
}
