package de.ustu.ims.reiter.treeanno.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;

public class DatabaseIO implements DataLayer {

	DataSource dataSource;

	public DatabaseIO() throws ClassNotFoundException, NamingException {
		Context initContext;
		Class.forName("com.mysql.jdbc.Driver");

		initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		dataSource = (DataSource) envContext.lookup("jdbc/treeanno");

	}

	public DatabaseIO(DataSource ds) throws ClassNotFoundException,
			NamingException {
		dataSource = ds;
	}

	public boolean isHidden(int documentId) throws SQLException {
		Connection conn = dataSource.getConnection();
		PreparedStatement stmt =
				conn.prepareStatement("SELECT hidden FROM treeanno_documents WHERE id=?");
		stmt.setInt(1, documentId);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			boolean b = rs.getBoolean(1);
			rs.close();
			stmt.close();
			conn.close();
			return b;
		}
		rs.close();
		stmt.close();
		conn.close();
		return false;
	}

	public int getAccessLevel(int documentId, User user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			stmt =
					conn.prepareStatement("SELECT level FROM (SELECT treeanno_projects.id AS pid, treeanno_documents.id AS did FROM treeanno_documents, treeanno_projects WHERE treeanno_documents.project = treeanno_projects.id) proj, treeanno_users_permissions WHERE pid = projectId AND userId=? AND did=?");
			stmt.setInt(1, user.getDatabaseId());
			stmt.setInt(2, documentId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				int r = rs.getInt(1);
				rs.close();
				stmt.close();
				conn.close();
				return r;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(conn);
		}
		return 0;
	}

	@Override
	public int getAccessLevel(Project project, User user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			stmt =
					conn.prepareStatement("SELECT level FROM treeanno_users_permissions WHERE projectId=? AND userId=?");
			stmt.setInt(1, project.getDatabaseId());
			stmt.setInt(2, user.getDatabaseId());
			rs = stmt.executeQuery();
			if (rs.next()) {
				int r = rs.getInt(1);
				rs.close();
				stmt.close();
				conn.close();
				return r;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(conn);
		}
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
						.prepareStatement("UPDATE treeanno_documents SET xmi=? WHERE id=?");
		stmt.setString(1, s);
		stmt.setInt(2, documentId);
		int r = stmt.executeUpdate();
		stmt.close();
		return r == 1;
	}

	@Override
	public Document getDocument(int documentId) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Document document = null;
		try {
			connection = dataSource.getConnection();

			stmt =
					connection
							.prepareStatement("SELECT name,modificationDate, project, hidden FROM treeanno_documents WHERE id=?");
			stmt.setInt(1, documentId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				document = new Document();
				document.setDatabaseId(documentId);
				document.setName(rs.getString(1));
				document.setModificationDate(rs.getDate(2));
				document.setHidden(rs.getBoolean(4));
				document.setProject(getProject(rs.getInt(3)));
				return document;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(connection);
		}
		return document;

	}

	public JCas getJCas(int documentId) throws SQLException, IOException,
			UIMAException {
		JCas jcas = null;

		Connection connection = dataSource.getConnection();

		PreparedStatement stmt =
				connection
						.prepareStatement("SELECT * FROM treeanno_documents WHERE id=?");
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
				IOUtils.closeQuietly(is);
			}
		}
		rs.close();
		stmt.close();
		connection.close();
		return jcas;

	}

	public boolean deleteDocument(int documentId) throws SQLException {
		Connection connection = dataSource.getConnection();

		PreparedStatement stmt =
				connection
						.prepareStatement("UPDATE treeanno_documents SET hidden=1 WHERE id=?");
		stmt.setInt(1, documentId);
		int r = stmt.executeUpdate();
		stmt.close();
		connection.close();

		return r == 1;
	}

	public boolean cloneDocument(int documentId) throws SQLException {
		Connection connection = dataSource.getConnection();

		PreparedStatement stmt =
				connection
						.prepareStatement("INSERT INTO treeanno_documents(xmi,typesystemId,project,name) SELECT xmi,typesystemId,project,name FROM treeanno_documents WHERE id=?");
		stmt.setInt(1, documentId);
		int r = stmt.executeUpdate();
		stmt.close();
		connection.close();

		return r == 1;
	}

	@Override
	public List<Project> getProjects() {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Project> projects = new LinkedList<Project>();
		try {
			connection = dataSource.getConnection();
			stmt =
					connection
							.prepareStatement("SELECT * FROM treeanno_projects");
			rs = stmt.executeQuery();
			while (rs.next()) {
				Project p = new Project();
				p.setDatabaseId(rs.getInt(1));
				p.setName(rs.getString(2));
				projects.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(connection);
		}

		return projects;

	}

	public List<Document> getDocuments(int projectId) {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Document> documents = new LinkedList<Document>();
		try {
			connection = dataSource.getConnection();
			stmt =
					connection
							.prepareStatement("SELECT id,modificationDate,name,hidden,project FROM treeanno_documents WHERE project=? AND hidden=0");
			stmt.setInt(1, projectId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Document doc = new Document();
				doc.setDatabaseId(rs.getInt(1));
				doc.setModificationDate(rs.getDate(2));
				doc.setName(rs.getString(3));
				doc.setHidden(rs.getBoolean(4));
				doc.setProject(getProject(rs.getInt(5)));
				documents.add(doc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(connection);
		}

		return documents;

	}

	private void closeQuietly(Connection connection) {
		try {
			if (connection != null) connection.close();
		} catch (Exception e) {};
	}

	private void closeQuietly(Statement statement) {
		try {
			if (statement != null) statement.close();
		} catch (Exception e) {};
	}

	private void closeQuietly(ResultSet resultSet) {
		try {
			if (resultSet != null) resultSet.close();
		} catch (Exception e) {};
	}

	@Override
	public Project getProject(int i) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Project proj = null;
		try {
			conn = dataSource.getConnection();
			stmt =
					conn.prepareStatement("SELECT * FROM treeanno_projects WHERE id=?");
			stmt.setInt(1, i);
			rs = stmt.executeQuery();
			if (rs.next()) {
				proj = new Project();
				proj.setDatabaseId(rs.getInt(1));
				proj.setName(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(conn);
		}

		return proj;
	}

	@Override
	public User getUser(int i) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = dataSource.getConnection();
			stmt =
					conn.prepareStatement("SELECT id, username,email,language FROM treeanno_users WHERE id=?");
			stmt.setInt(1, i);
			rs = stmt.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setDatabaseId(rs.getInt(1));
				user.setName(rs.getString(2));
				if (rs.getString(3) != null) user.setEmail(rs.getString(3));
				if (rs.getString(4) != null) user.setLanguage(rs.getString(5));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(conn);
		}

		return user;
	}

	@Override
	public Collection<Document> getDocuments(Project proj) {
		return getDocuments(proj.getDatabaseId());
	}

	@Override
	public JCas getJCas(Document document) {
		try {
			return this.getJCas(document.getDatabaseId());
		} catch (UIMAException | SQLException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean deleteDocument(Document document) {
		try {
			return this.deleteDocument(document.getDatabaseId());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int cloneDocument(Document document) {
		try {
			if (!cloneDocument(document.getDatabaseId())) return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(conn);
		}
		return -1;
	}

	@Override
	public boolean updateJCas(Document document, JCas jcas) {
		try {
			return this.updateJCas(document.getDatabaseId(), jcas);
		} catch (SQLException | SAXException | IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
