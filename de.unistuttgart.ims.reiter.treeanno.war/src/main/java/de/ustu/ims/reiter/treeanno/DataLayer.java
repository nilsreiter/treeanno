package de.ustu.ims.reiter.treeanno;

import java.sql.SQLException;
import java.util.Collection;

import org.apache.uima.jcas.JCas;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;

/**
 * This interface describes the methods to retrieve data from cache or database.
 * All data access should go through one of these methods.
 * 
 * @author reiterns
 *
 */
public interface DataLayer {
	Project getProject(int i) throws SQLException;

	Collection<Project> getProjects() throws SQLException;

	User getUser(int i) throws SQLException;

	Document getDocument(int i) throws SQLException;

	UserDocument getUserDocument(int id) throws SQLException;

	UserDocument getUserDocument(int userId, int documentId)
			throws SQLException;

	UserDocument createUserDocument(User user, Document document)
			throws SQLException;

	int getAccessLevel(Project proj, User user) throws SQLException;

	boolean deleteDocument(Document document) throws SQLException;

	boolean setJCas(Document document, JCas jcas) throws SQLException,
			SAXException;

	boolean updateDocument(Document document) throws SQLException;

	Document createNewDocument(Document d) throws SQLException;

	boolean updateUserDocument(UserDocument document) throws SQLException;

	boolean deleteUserDocument(int id) throws SQLException;

	Collection<User> getUsers() throws SQLException;

	UserDocument getUserDocument(User user, Document document)
			throws SQLException;
}
