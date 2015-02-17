package de.nilsreiter.goodreads.io.xml2db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.DataSource;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Xml2DB {

	Logger logger = Logger.getLogger(getClass().getName());

	DataSource dataSource;

	int pages = 100;

	boolean skip_review_exists = false;

	public Xml2DB(DataSource ds) {
		dataSource = ds;
	}

	public synchronized boolean readBook(int bookId)
			throws InterruptedException, ValidityException, ParsingException,
			IOException, SQLException {
		boolean r = true;
		logger.info("Processing book id " + bookId);
		for (int page = 1; page < this.getPages(); page++) {
			// long timestamp = System.currentTimeMillis();
			URL url =
					new URL(
							"https",
							"www.goodreads.com",
							443,
							"/book/show/"
									+ bookId
									+ "?format=xml&key=XGb9Cp3QPbvL4eRz3WfyA&text_only=true&page="
									+ page);
			logger.fine("Accessing URL " + url.toString());

			r &= this.readStream(url.openStream());

			logger.fine("Stored url info in database.");
			Thread.sleep(1000l);
		}
		return r;
	}

	public boolean readStream(InputStream is) throws ValidityException,
			IOException, SQLException {
		try {
			Builder xBuilder = new Builder();
			Document doc = xBuilder.build(is);

			Element rootElement = doc.getRootElement();
			// 1. Detect response type (by checking what method was asked
			String method =
					rootElement.getChildElements("Request").get(0)
							.getChildElements("method").get(0).getValue();
			if (method.equals("book_show"))
				return this.parseResponseBookShow(rootElement);
		} catch (ParsingException e) {
			logger.severe(e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	public boolean readFile(File xmlFile) throws ValidityException,
	FileNotFoundException, ParsingException, IOException, SQLException {
		return readStream(new FileInputStream(xmlFile));
	}

	protected boolean parseResponseBookShow(Element rElement)
			throws SQLException, MalformedURLException {
		Connection conn = dataSource.getConnection();

		boolean r = true;

		// 1. parse book information
		Element bookElement = rElement.getChildElements().get(1);
		Elements bookChildElements = bookElement.getChildElements();
		String id = null, title = null, isbn = null, isbn13 = null, language =
				null;
		Element authorElement = null;
		Element reviewsElement = null;
		for (int i = 0; i < bookChildElements.size(); i++) {
			Element elm = bookChildElements.get(i);
			String qname = elm.getQualifiedName();
			if (qname.equals("id"))
				id = elm.getValue();
			else if (qname.equals("title"))
				title = elm.getValue();
			else if (qname.equals("isbn"))
				isbn = elm.getValue();
			else if (qname.equals("isbn13"))
				isbn13 = elm.getValue();
			else if (qname.equals("language_code"))
				language = elm.getValue();
			else if (qname.equals("authors"))
				authorElement = elm;
			else if (qname.equals("reviews")) reviewsElement = elm;
		}
		if (!exists("gr_books", id)) {
			PreparedStatement stmt =
					conn.prepareStatement("INSERT INTO gr_books VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setString(1, id);
			stmt.setString(2, id);
			stmt.setString(3, isbn);
			stmt.setString(4, isbn13);
			stmt.setString(5, title);
			stmt.setString(6, language);
			stmt.execute();
			stmt.close();
		}
		if (authorElement != null) {
			Elements authorElements = authorElement.getChildElements();
			for (int i = 0; i < authorElements.size(); i++) {
				Element author = authorElements.get(i);
				String auth_id =
						author.getChildElements("id").get(0).getValue();
				String auth_name =
						author.getChildElements("name").get(0).getValue();
				if (!exists("gr_authors", auth_id)) {
					PreparedStatement stmt =
							conn.prepareStatement("INSERT INTO gr_authors VALUES (?,?,?)");
					stmt.setString(1, auth_id);
					stmt.setString(2, auth_id);
					stmt.setString(3, auth_name);
					stmt.execute();
					stmt.close();
				}
				if (!existsW("gr_books2authors", "book='" + id
						+ "' AND author='" + auth_id + "'")) {
					PreparedStatement stmt =
							conn.prepareStatement("INSERT INTO gr_books2authors VALUES (DEFAULT,?,?)");
					stmt.setString(1, id);
					stmt.setString(2, auth_id);
					stmt.execute();
					stmt.close();
				}
			}
		}

		if (reviewsElement != null) {
			Elements reviewElements = reviewsElement.getChildElements();
			for (int i = 0; i < reviewElements.size(); i++) {
				Element reviewElement = reviewElements.get(i);
				processReview(id, reviewElement);
			}
		}

		conn.close();
		return r;
	}

	protected void processReview(String bookId, Element reviewElement)
			throws SQLException, MalformedURLException {
		Connection conn = dataSource.getConnection();
		Elements reviewChildren = reviewElement.getChildElements();
		String id = null, body = null, userId = null, userLocation = null, userName =
				null;
		boolean spoiler_flag = false;
		int rating = 0;
		URL url = null;
		Element userElement = null;
		for (int i = 0; i < reviewChildren.size(); i++) {
			Element child = reviewChildren.get(i);
			String qname = child.getQualifiedName();
			if (qname.equals("id"))
				id = child.getValue();
			else if (qname.equals("body"))
				body = child.getValue();
			else if (qname.equals("rating"))
				rating = Integer.parseInt(child.getValue());
			else if (qname.equals("spoiler_flag"))
				spoiler_flag = Boolean.parseBoolean(child.getValue());
			else if (qname.equals("user"))
				userElement = child;
			else if (qname.equals("url")) url = new URL(child.getValue());
		}
		if (userElement != null) {
			userId = userElement.getChildElements("id").get(0).getValue();
			userLocation =
					userElement.getChildElements("location").get(0).getValue();
			userName = userElement.getChildElements("name").get(0).getValue();
		}

		if (!exists("gr_users", userId)) {
			PreparedStatement stmt =
					conn.prepareStatement("INSERT INTO gr_users VALUES (?,?,?,?)");
			stmt.setString(1, userId);
			stmt.setString(2, userId);
			stmt.setString(3, userName);
			stmt.setString(4, userLocation);
			stmt.execute();
			stmt.close();
		}

		if (skip_review_exists || !exists("gr_reviews", id)) {
			PreparedStatement stmt =
					conn.prepareStatement("INSERT INTO gr_reviews VALUES (?,?,?,?,?,?,?,?)");
			stmt.setString(1, id);
			stmt.setString(2, id);
			stmt.setString(3, bookId);
			stmt.setString(4, userId);
			stmt.setString(5, body);
			stmt.setInt(6, rating);
			stmt.setURL(7, url);
			stmt.setBoolean(8, spoiler_flag);
			stmt.execute();
			stmt.close();
		}
		conn.close();
	}

	boolean existsW(String tableName, String where) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs =
				stmt.executeQuery("SELECT id FROM " + tableName + " WHERE "
						+ where);
		boolean r = rs.next();
		rs.close();
		stmt.close();
		conn.close();
		return r;
	}

	boolean exists(String tableName, String gr_id) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs =
				stmt.executeQuery("SELECT id FROM " + tableName
						+ " WHERE gr_id = '" + gr_id + "'");
		boolean r = rs.next();
		rs.close();
		stmt.close();
		conn.close();
		return r;
	}

	public static void main(String[] args) throws ValidityException,
	FileNotFoundException, ParsingException, IOException,
	CmdLineException, InterruptedException, SQLException {

		OptionBeans op = new Xml2DB.OptionBeans();
		CmdLineParser cmd = new CmdLineParser(op);
		cmd.parseArgument(args);

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername(op.dbUsername);
		dataSource.setPassword(op.dbPassword);
		dataSource.setUrl("jdbc:mysql://" + op.dbHost + ":" + op.dbPort + "/"
				+ op.dbName);

		Xml2DB xml2db = new Xml2DB(dataSource);
		xml2db.setPages(op.pages);
		xml2db.setSkip_review_exists(false);
		for (int book = op.bookStart; book < op.bookStart + op.books; book++) {
			xml2db.readBook(book);
		}
	}

	public static class OptionBeans {
		@Option(name = "--host")
		String dbHost = "waitahapinguin";

		@Option(name = "--username")
		String dbUsername = "reiterns";

		@Option(name = "--password")
		String dbPassword = "bybNoaKni";

		@Option(name = "--port")
		int dbPort = 3306;

		@Option(name = "--database")
		String dbName = "reiter";

		@Option(name = "--start")
		int bookStart = 1;

		@Option(name = "--pages")
		int pages = 100;

		@Option(name = "--books")
		int books = 10;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public boolean isSkip_review_exists() {
		return skip_review_exists;
	}

	public void setSkip_review_exists(boolean skip_review_exists) {
		this.skip_review_exists = skip_review_exists;
	}
}
