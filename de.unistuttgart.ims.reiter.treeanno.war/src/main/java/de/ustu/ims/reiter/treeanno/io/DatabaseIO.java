package de.ustu.ims.reiter.treeanno.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.H2DatabaseType;
import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.ProjectType;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.beans.UserPermission;

public class DatabaseIO implements DataLayer {

	DataSource dataSource;
	Dao<User, Integer> userDao;
	Dao<Project, Integer> projectDao;
	Dao<Document, Integer> documentDao;
	Dao<UserDocument, Integer> userDocumentDao;
	Dao<UserPermission, Integer> userPermissionDao;

	public DatabaseIO(DataSource dataSource, int dsType) throws ClassNotFoundException, NamingException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Class.forName("org.h2.Driver");

		DataSourceConnectionSource connectionSource = new DataSourceConnectionSource(dataSource,
				(dsType == 0 ? new MysqlDatabaseType() : new H2DatabaseType()));

		userDao = DaoManager.createDao(connectionSource, User.class);
		projectDao = DaoManager.createDao(connectionSource, Project.class);
		documentDao = DaoManager.createDao(connectionSource, Document.class);
		userDocumentDao = DaoManager.createDao(connectionSource, UserDocument.class);
		userPermissionDao = DaoManager.createDao(connectionSource, UserPermission.class);

		userDao.setObjectCache(true);
		projectDao.setObjectCache(true);
		documentDao.setObjectCache(true);
		userDocumentDao.setObjectCache(true);
		userPermissionDao.setObjectCache(false);

		TableUtils.createTableIfNotExists(connectionSource, User.class);
		TableUtils.createTableIfNotExists(connectionSource, Project.class);
		TableUtils.createTableIfNotExists(connectionSource, Document.class);
		TableUtils.createTableIfNotExists(connectionSource, UserDocument.class);
		TableUtils.createTableIfNotExists(connectionSource, UserPermission.class);

		Project p = null;
		if (projectDao.countOf() == 0) {
			p = new Project();
			p.setName("Project 1");
			p.setType(ProjectType.DEFAULT);
			projectDao.create(p);
		}

		if (userDao.countOf() == 0) {
			User user = new User();
			user.setName("admin");
			user.setEmail("example@example.org");
			user.setAdmin(true);
			userDao.create(user);

			UserPermission up = new UserPermission();
			up.setUserId(user);
			up.setLevel(Perm.ADMIN_ACCESS);
			up.setProjectId(p);
			userPermissionDao.create(up);
		}

	}

	public int getAccessLevel(int documentId, User user) throws SQLException {
		if (user == null)
			return Perm.NO_ACCESS;
		return getAccessLevel(getDocument(documentId).getProject(), user);
	}

	@Override
	public int getAccessLevel(Project project, User user) throws SQLException {
		if (user.isAdmin())
			return Perm.ADMIN_ACCESS;
		List<UserPermission> list = userPermissionDao.queryBuilder().where().eq(UserPermission.FIELD_USER, user).and()
				.eq(UserPermission.FIELD_PROJECT, project).query();

		if (!list.isEmpty())
			return list.get(0).getLevel();
		return Perm.NO_ACCESS;

	}

	@Override
	public void setAccessLevel(Project project, User user, int level) throws SQLException {
		List<UserPermission> list = userPermissionDao.queryBuilder().where().eq(UserPermission.FIELD_USER, user).and()
				.eq(UserPermission.FIELD_PROJECT, project).query();
		if (list.isEmpty()) {
			UserPermission up = new UserPermission();
			up.setUserId(user);
			up.setProjectId(project);
			up.setLevel(level);
			userPermissionDao.create(up);
		} else if (list.size() == 1) {
			UserPermission up = list.get(0);
			up.setLevel(level);
			userPermissionDao.update(up);
		} else {
			// this should not happen
			throw new SQLException();
		}

	}

	public boolean updateJCas(int documentId, JCas jcas) throws SQLException, SAXException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmiCasSerializer.serialize(jcas.getCas(), baos);

		String s = null;
		try {
			s = new String(baos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// This should not happen.
			e.printStackTrace();
		}

		Connection connection = dataSource.getConnection();

		PreparedStatement stmt = connection.prepareStatement("UPDATE treeanno_documents SET xmi=? WHERE id=?");
		stmt.setString(1, s);
		stmt.setInt(2, documentId);
		int r = stmt.executeUpdate();
		stmt.close();
		return r == 1;
	}

	@Override
	public Document getDocument(int documentId) throws SQLException {
		// TODO: prevent immediate retrieval of xmi column
		Document d = documentDao.queryForId(documentId);
		return d;
	}

	@Override
	public UserDocument getUserDocument(User user, Document document) throws SQLException {
		// TODO: prevent immediate retrieval of xmi column

		QueryBuilder<UserDocument, Integer> queryBuilder = userDocumentDao.queryBuilder();
		PreparedQuery<UserDocument> pq = queryBuilder.where().eq(UserDocument.FIELD_SRC_DOCUMENT, document).and()
				.eq(UserDocument.FIELD_USER, user).prepare();
		List<UserDocument> ret = userDocumentDao.query(pq);
		if (ret.isEmpty()) {
			UserDocument ud = new UserDocument();
			ud.setUser(user);
			ud.setDocument(document);
			ud.setXmi(document.getXmi());
			userDocumentDao.create(ud);
			return ud;
		} else {
			return ret.get(0);
		}
	}

	@Override
	public UserDocument getUserDocument(int user, int document) throws SQLException {
		// TODO: prevent immediate retrieval of xmi column
		// TODO: also, make more efficient

		return getUserDocument(getUser(user), getDocument(document));
	}

	public JCas getJCas(int documentId) throws SQLException, UIMAException, SAXException, IOException {
		JCas jcas = null;

		Connection connection = dataSource.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT xmi FROM treeanno_documents WHERE id=?");
			stmt.setInt(1, documentId);
			rs = stmt.executeQuery();

			if (rs.next()) {

				String textXML = rs.getString(1);
				TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription();
				jcas = JCasFactory.createJCas(tsd);
				InputStream is = null;
				try {
					is = new ByteArrayInputStream(textXML.getBytes());
					XmiCasDeserializer.deserialize(is, jcas.getCas(), true);
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(connection);
		}
		return jcas;

	}

	public boolean deleteDocument(int documentId) throws SQLException {
		return (documentDao.deleteById(documentId) == 1);
	}

	@Override
	public List<Project> getProjects() throws SQLException {
		return projectDao.queryForAll();
	}

	public List<Document> getDocuments(int projectId) throws SQLException {
		Map<String, Object> fv = new HashMap<String, Object>();
		fv.put("project", projectId);
		fv.put("hidden", 0);
		return documentDao.queryForFieldValues(fv);

	}

	private void closeQuietly(Connection connection) {
		try {
			if (connection != null)
				connection.close();
		} catch (Exception e) {
		}
		;
	}

	private void closeQuietly(Statement statement) {
		try {
			if (statement != null)
				statement.close();
		} catch (Exception e) {
		}
		;
	}

	private void closeQuietly(ResultSet resultSet) {
		try {
			if (resultSet != null)
				resultSet.close();
		} catch (Exception e) {
		}
		;
	}

	@Override
	public Project getProject(int i) throws SQLException {
		return projectDao.queryForId(i);
	}

	@Override
	public User getUser(int i) throws SQLException {
		return userDao.queryForId(i);
	}

	@Override
	public boolean deleteDocument(Document document) throws SQLException {
		return this.deleteDocument(document.getDatabaseId());

	}

	@Override
	public boolean setJCas(Document document, JCas jcas) throws SQLException, SAXException {
		return this.updateJCas(document.getDatabaseId(), jcas);
	}

	@Override
	public boolean updateDocument(Document document) throws SQLException {
		return (documentDao.update(document) == 1);
	}

	@Override
	public boolean updateUserDocument(UserDocument document) throws SQLException {
		return (userDocumentDao.update(document) == 1);
	}

	public Dao<Document, Integer> getDocumentDao() {
		return documentDao;
	}

	@Override
	public Document createNewDocument(Document d) throws SQLException {
		int r = documentDao.create(d);
		if (r == 1)
			return d;
		else
			return null;
	}

	@Override
	public User createNewUser(User d) throws SQLException {
		int r = userDao.create(d);
		if (r == 1)
			return d;
		else
			return null;
	}

	@Override
	public UserDocument getUserDocument(int id) throws SQLException {
		// TODO: prevent immediate retrieval of xmi column
		return userDocumentDao.queryForId(id);
	}

	@Override
	public boolean deleteUserDocument(int id) throws SQLException {
		return (userDocumentDao.deleteIds(Arrays.asList(id)) == 1);
	}

	@Override
	public List<User> getUserList() throws SQLException {
		CloseableIterator<User> iter = userDao.closeableIterator();
		List<User> list = new ArrayList<User>();
		while (iter.hasNext())
			list.add(iter.next());
		return list;
	}

	@Override
	public boolean updateUser(User user) throws SQLException {
		return (userDao.update(user) == 1);
	}

	@Override
	public Project createNewProject(Project p) throws SQLException {
		int r = projectDao.create(p);
		if (r == 1)
			return p;
		else
			return null;
	}
}
