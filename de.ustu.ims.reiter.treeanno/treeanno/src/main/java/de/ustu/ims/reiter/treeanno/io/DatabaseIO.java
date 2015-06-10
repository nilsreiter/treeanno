package de.ustu.ims.reiter.treeanno.io;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.beans.User;

public class DatabaseIO {

	DataSource dataSource;

	public DatabaseIO() throws ClassNotFoundException, NamingException {
		Context initContext;
		Class.forName("com.mysql.jdbc.Driver");

		initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		dataSource = (DataSource) envContext.lookup("jdbc/treeanno");

	}

	public int getAccessLevel(int documentId, User user) throws SQLException {
		Connection conn = dataSource.getConnection();
		PreparedStatement stmt =
				conn.prepareStatement("SELECT level FROM (SELECT projects.id AS pid, documents.id AS did FROM documents, projects WHERE documents.project = projects.id) proj, users_permissions WHERE pid = projectId AND userId=? AND did=?");
		stmt.setInt(1, user.getDatabaseId());
		stmt.setInt(2, documentId);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			int r = rs.getInt(1);
			rs.close();
			stmt.close();
			conn.close();
			return r;
		}
		rs.close();
		stmt.close();
		conn.close();
		return 0;
	}

	public boolean updateJCas(int documentId, JCas jcas) throws SQLException,
			SAXException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmiCasSerializer.serialize(jcas.getCas(), baos);
		String s = new String(baos.toByteArray(), "UTF-8");

		Connection connection = dataSource.getConnection();

		PreparedStatement stmt =
				connection
				.prepareStatement("UPDATE documents SET xmi=? WHERE id=?");
		stmt.setString(1, s);
		stmt.setInt(2, documentId);
		int r = stmt.executeUpdate();
		stmt.close();
		return r == 1;
	}

	public JCas getJCas(int documentId) throws SQLException, IOException,
			UIMAException {
		JCas jcas = null;

		Connection connection = dataSource.getConnection();

		PreparedStatement stmt =
				connection
						.prepareStatement("SELECT * FROM documents WHERE id=?");
		stmt.setInt(1, documentId);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {

			String textXML = rs.getString(2);
			TypeSystemDescription tsd =
					TypeSystemDescriptionFactory.createTypeSystemDescription();
			jcas = JCasFactory.createJCas(tsd);
			InputStream is = null;
			try {
				is = new ByteArrayInputStream(textXML.getBytes());
				XmiCasDeserializer.deserialize(is, jcas.getCas(), true);
			} catch (SAXException e) {
				IOException ioe = new IOException(e.getMessage());
				ioe.initCause(e);
				throw ioe; // NOPMD
				// If we were using Java 1.6 and add the wrapped exception
				// to
				// the IOException
				// constructor, we would not get a warning here
			} finally {
				closeQuietly(is);
			}
		}
		rs.close();
		stmt.close();
		connection.close();
		return jcas;

	}

	public void closeConnections() {}
}
