package de.nilsreiter.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.nilsreiter.util.db.DBUtils;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.SQLBuilder;

public class SVGDatabase {

	Database database;

	public SVGDatabase(Database db) {
		database = db;
	}

	public String getSVGString(String docId, String eventId) {
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = database.getConnection();
			SQLBuilder b = new SQLBuilder();
			b.select("svg")
			.from(database.getTableName("eventsSVG"))
			.where("documentId='" + docId + "' AND eventId='" + eventId
					+ "'");
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(b.toString());
			rs.first();
			String svgString = rs.getString(1);
			rs.close();
			rs = null;
			stmt.close();
			conn.close();
			return svgString;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) DBUtils.closeAll(rs);
			if (conn != null) DBUtils.close(conn);
		}
	}

	public void putSVGString(String docId, String eventId, String svgString) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = database.getConnection();
			SQLBuilder b = new SQLBuilder();
			b.insert(database.getTableName("eventsSVG"), "documentId",
					"eventId", "svg").values("?", "?", "?");
			stmt = conn.prepareStatement(b.toString());
			stmt.setString(1, docId);
			stmt.setString(2, eventId);
			stmt.setString(3, svgString);
			int res = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) DBUtils.close(stmt);
			if (conn != null) DBUtils.close(conn);
		}
	}
}
