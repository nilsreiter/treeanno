package de.ustu.ims.reiter.treeanno;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.xml.sax.SAXException;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;

/**
 * This interface describes the methods to retrieve data from cache or database.
 * All data access should go through one of these methods.
 * @author reiterns
 *
 */
public interface DataLayer {
	Project getProject(int i) throws SQLException;

	Collection<Project> getProjects() throws SQLException;

	User getUser(int i) throws SQLException;

	Document getDocument(int i) throws SQLException;

	int getAccessLevel(Project proj, User user) throws SQLException;

	Collection<Document> getDocuments(Project proj) throws SQLException;

	JCas getJCas(Document document) throws SQLException, UIMAException,
			SAXException, IOException;

	boolean deleteDocument(Document document) throws SQLException;

	int cloneDocument(Document document) throws SQLException;

	boolean updateJCas(Document document, JCas jcas) throws SQLException,
	SAXException;

	boolean updateDocument(Document document) throws SQLException;
}
