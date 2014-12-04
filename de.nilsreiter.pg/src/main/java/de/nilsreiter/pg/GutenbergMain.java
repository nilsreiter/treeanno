package de.nilsreiter.pg;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;

import javax.sql.DataSource;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.nilsreiter.util.db.DataSourceFactory;
import de.uniheidelberg.cl.a10.MainWithIO;

public class GutenbergMain extends MainWithIO {
	public static void main(String[] args) throws SQLException {
		GutenbergMain gm = new GutenbergMain();
		gm.processArguments(args);
		gm.run();
	}

	private void run() throws SQLException {
		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		// use the FileManager to find the input file
		InputStream in = this.getInputStream();
		if (in == null) {
			throw new IllegalArgumentException("File: " + this.input.getName()
					+ " not found");
		}

		// read the RDF/XML file
		model.read(in, null);
		// list the statements in the Model
		StmtIterator iter = model.listStatements();
		Resource mainResource = null;

		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); // get next statement
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

		DataSource dataSource =
				DataSourceFactory.getDataSource(getConfiguration());
		PreparedStatement stmt =
				dataSource
						.getConnection()
						.prepareStatement(
								"INSERT INTO Gutenberg_Meta VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		stmt.setString(1, mainResource.getURI());

		Property p = model.createProperty("http://purl.org/dc/terms/creator");
		Resource author = model.getProperty(mainResource, p).getResource();
		// System.err.println(author.toString());
		p = model.createProperty("http://www.gutenberg.org/2009/pgterms/name");

		stmt.setString(2, model.getProperty(author, p).getObject().toString());
		p =
				model.createProperty("http://www.gutenberg.org/2009/pgterms/birthdate");
		stmt.setInt(
				3,
				Integer.valueOf(model.getProperty(author, p).getObject()
						.toString().substring(0, 4)));
		p =
				model.createProperty("http://www.gutenberg.org/2009/pgterms/deathdate");
		stmt.setInt(
				4,
				Integer.valueOf(model.getProperty(author, p).getObject()
						.toString().substring(0, 4)));
		p = model.createProperty("http://purl.org/dc/terms/language");
		Resource language = model.getProperty(mainResource, p).getResource();
		p =
				model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");

		stmt.setString(5, model.getProperty(language, p).getObject().toString()
				.substring(0, 2));

		stmt.setString(
				6,
				model.getProperty(mainResource,
						model.getProperty("http://purl.org/dc/terms/title"))
						.getObject().toString());
		stmt.setString(
				7,
				model.getProperty(
						mainResource,
						model.getProperty("http://purl.org/dc/terms/description"))
						.getObject().toString());

		p = model.createProperty("http://purl.org/dc/terms/subject");
		NodeIterator iterator = model.listObjectsOfProperty(mainResource, p);
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
			} else if (node.hasProperty(
					model.createProperty("http://purl.org/dc/dcam/memberOf"),
					model.getResource("http://purl.org/dc/terms/LCC"))) {
				lcc.add(model.getProperty(node, valProp).getObject().toString());
			} else
				subjects.add(model.getProperty(node, valProp).getObject()
						.toString());
		}
		stmt.setString(8, lcsh.toString());
		stmt.setString(9, lcc.toString());
		stmt.setString(10, subjects.toString());

		stmt.execute();
		stmt.close();
	}
}
