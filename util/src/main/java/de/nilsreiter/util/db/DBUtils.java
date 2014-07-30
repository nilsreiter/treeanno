package de.nilsreiter.util.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtils {
	public static void close(ResultSet obj) {
		try {
			if (obj != null) obj.close();
		} catch (Exception e) {};
	}

	public static void close(Connection obj) {
		try {
			if (obj != null) obj.close();
		} catch (Exception e) {};
	}

	public static void close(Statement obj) {
		try {
			if (obj != null) obj.close();
		} catch (Exception e) {};
	}

	public static void closeAll(ResultSet obj) {
		Statement stmt = null;
		Connection conn = null;
		try {
			stmt = obj.getStatement();
			conn = stmt.getConnection();
		} catch (Exception e) {} finally {
			close(obj);
			close(stmt);
			close(conn);
		}
	}
}
