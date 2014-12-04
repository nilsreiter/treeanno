package de.nilsreiter.pg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;

import javax.sql.DataSource;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.nilsreiter.util.db.DBUtils;
import de.nilsreiter.util.db.DataSourceFactory;
import de.uniheidelberg.cl.a10.MainWithIODir;

public class GutenbergMain extends MainWithIODir {
	DataSource dataSource;

	public static void main(String[] args) throws SQLException,
			FileNotFoundException {
		GutenbergMain gm = new GutenbergMain();
		gm.processArguments(args);
		gm.run();
	}

	private void run() throws SQLException, FileNotFoundException {

		dataSource = DataSourceFactory.getDataSource(getConfiguration());

		processDirectory(this.getInputDirectory());
	}

	private void processDirectory(File file) throws FileNotFoundException,
			SQLException {
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				processDirectory(files[i]);
			} else if (files[i].getName().endsWith(".rdf")) {
				processFile(files[i]);
			}
		}
	}

	private void processFile(File file) throws FileNotFoundException,
			SQLException {
		Model model = ModelFactory.createDefaultModel();

		InputStream in = new FileInputStream(file);

		// read the RDF/XML file
		model.read(in, null);
		// list the statements in the Model
		StmtIterator iter = model.listStatements();
		Resource mainResource = null;

		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement

			// System.out.println(stmt);
			Resource subject = stmt.getSubject(); // get the subject
			Property typeProperty =
					model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			if (subject
					.hasProperty(
							typeProperty,
							model.createResource("http://www.gutenberg.org/2009/pgterms/ebook"))) {
				mainResource = subject;

			}

		}

		if (mainResource != null) {

			Connection connection = null;
			PreparedStatement stmt = null;
			try {
				connection = dataSource.getConnection();
				stmt =
						connection
								.prepareStatement("INSERT INTO Gutenberg_Meta VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

				stmt.setString(1, mainResource.getURI());

				Property p =
						model.createProperty("http://purl.org/dc/terms/creator");
				Resource author =
						model.getProperty(mainResource, p).getResource();
				// System.err.println(author.toString());
				p =
						model.createProperty("http://www.gutenberg.org/2009/pgterms/name");

				stmt.setString(2, model.getProperty(author, p).getObject()
						.toString());
				p =
						model.createProperty("http://www.gutenberg.org/2009/pgterms/birthdate");
				if (author.hasProperty(p))
					stmt.setInt(
							3,
							Integer.valueOf(model.getProperty(author, p)
									.getObject().toString().substring(0, 4)
									.replaceAll("[^0-9]", "")));
				else
					stmt.setNull(3, java.sql.Types.INTEGER);
				p =
						model.createProperty("http://www.gutenberg.org/2009/pgterms/deathdate");
				if (author.hasProperty(p))
					stmt.setInt(
							4,
							Integer.valueOf(model.getProperty(author, p)
									.getObject().toString().substring(0, 4)
									.replaceAll("[^0-9]", "")));
				else
					stmt.setNull(4, java.sql.Types.INTEGER);

				p = model.createProperty("http://purl.org/dc/terms/language");
				RDFNode languageNode =
						model.getProperty(mainResource, p).getObject();
				if (languageNode.isResource()) {
					p =
							model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");

					stmt.setString(5,
							model.getProperty(languageNode.asResource(), p)
							.getObject().toString().substring(0, 2));
				} else if (languageNode.isLiteral()) {
					String langString = languageNode.toString();
					langString =
							langString.substring(0, langString.indexOf('^'));
					stmt.setString(5, langString);
				} else {
					stmt.setNull(5, java.sql.Types.OTHER);
				}

				String title =
						model.getProperty(
								mainResource,
								model.getProperty("http://purl.org/dc/terms/title"))
								.getObject().toString();
				stmt.setString(6, title);

				p = model.getProperty("http://purl.org/dc/terms/description");
				if (mainResource.hasProperty(p))
					stmt.setString(7, model.getProperty(mainResource, p)
							.getObject().toString());
				else
					stmt.setNull(7, java.sql.Types.OTHER);

				p = model.createProperty("http://purl.org/dc/terms/subject");
				NodeIterator iterator =
						model.listObjectsOfProperty(mainResource, p);
				HashSet<String> subjects = new HashSet<String>();
				HashSet<String> lcsh = new HashSet<String>();
				HashSet<String> lcc = new HashSet<String>();

				Property valProp =
						model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");
				while (iterator.hasNext()) {
					Resource node = iterator.next().asResource();
					if (node.hasProperty(
							model.createProperty("http://purl.org/dc/dcam/memberOf"),
							model.getResource("http://purl.org/dc/terms/LCSH"))) {
						lcsh.add(model.getProperty(node, valProp).getObject()
								.toString());
					} else if (node
							.hasProperty(
									model.createProperty("http://purl.org/dc/dcam/memberOf"),
									model.getResource("http://purl.org/dc/terms/LCC"))) {
						lcc.add(model.getProperty(node, valProp).getObject()
								.toString());
					} else
						subjects.add(model.getProperty(node, valProp)
								.getObject().toString());
				}
				stmt.setString(8, lcsh.toString());
				stmt.setString(9, lcc.toString());
				stmt.setString(10, subjects.toString());

				p = model.createProperty("http://purl.org/dc/terms/hasFormat");
				iterator = model.listObjectsOfProperty(mainResource, p);
				Property valueProperty =
						model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");
				Property formatProperty =
						model.createProperty("http://purl.org/dc/terms/format");
				Property extentProperty =
						model.createProperty("http://purl.org/dc/terms/extent");
				Resource textVersion = null;
				String format = null;
				while (iterator.hasNext()) {
					Resource hasFormatnode = iterator.next().asResource();
					Resource formatNode =
							model.getProperty(hasFormatnode, formatProperty)
									.getResource();
					if (formatNode.hasProperty(valueProperty)) {
						RDFNode valueNode =
								formatNode.getProperty(valueProperty)
								.getObject();
						if (valueNode.toString().startsWith("text/plain")) {
							textVersion = hasFormatnode;
							format = valueNode.toString();
						}
					}
				}

				if (textVersion != null) {
					String extentString =
							textVersion.getProperty(extentProperty).getObject()
							.toString();
					stmt.setInt(11, Integer.valueOf(extentString.substring(0,
							extentString.indexOf('^'))));
				} else
					stmt.setNull(11, java.sql.Types.INTEGER);

				if (format != null)
					stmt.setString(12, format);
				else
					stmt.setNull(12, java.sql.Types.OTHER);
				// System.out.println(stmt.toString());
				stmt.execute();
				stmt.close();

				logger.info("Added {}: {}", mainResource.getURI(), title);
			} catch (com.hp.hpl.jena.rdf.model.ResourceRequiredException e) {
				e.printStackTrace();
			} catch (Exception we) {
				we.printStackTrace();
				logger.warn("Exception in {}.", file);
			} finally {
				DBUtils.close(stmt);
				DBUtils.close(connection);
			}
		}
	}
}
