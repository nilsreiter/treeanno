package de.ustu.ims.reiter.treeanno.io;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.ByteArrayInputStream;
import java.io.File;
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
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.xml.sax.SAXException;

public class DatabaseReader {

	DataSource dataSource;

	public DatabaseReader() throws ClassNotFoundException, NamingException {
		Context initContext;
		Class.forName("com.mysql.jdbc.Driver");

		initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		dataSource = (DataSource) envContext.lookup("jdbc/treeanno");

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

	public JCas loadJCas(File jcasFile, File tsFile) throws UIMAException,
			IOException {
		TypeSystemDescription tsd =
				TypeSystemDescriptionFactory
				.createTypeSystemDescriptionFromPath(tsFile
						.getAbsolutePath());

		return JCasFactory.createJCas(jcasFile.getAbsolutePath(), tsd);
	}

	public void closeConnections() {}
}
