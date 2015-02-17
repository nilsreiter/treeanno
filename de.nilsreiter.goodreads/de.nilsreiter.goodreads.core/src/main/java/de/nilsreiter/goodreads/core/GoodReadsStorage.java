package de.nilsreiter.goodreads.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.sql.DataSource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import de.nilsreiter.goodreads.api.Author;
import de.nilsreiter.goodreads.api.Book;
import de.nilsreiter.goodreads.api.Review;
import de.nilsreiter.goodreads.api.User;

public class GoodReadsStorage {
	DataSource dataSource;
	CacheManager cacheManager;

	private static final String CACHE_USERS = "gr_users";
	private static final String CACHE_REVIEWS = "gr_reviews";
	private static final String CACHE_BOOKS = "gr_books";

	public GoodReadsStorage(DataSource ds) {
		dataSource = ds;

		cacheManager = CacheManager.create();

		// Creating user cache
		Cache userCache = new Cache(CACHE_USERS, 5000, false, false, 5, 2);
		cacheManager.addCache(userCache);

		// Creating review cache
		cacheManager
		.addCache(new Cache(CACHE_REVIEWS, 5000, false, false, 5, 2));
		// Creating book cache
		cacheManager.addCache(new Cache(CACHE_BOOKS, 5000, false, false, 5, 2));
	}

	public User getUser(int id) throws SQLException {

		Cache cache = cacheManager.getCache(CACHE_USERS);
		Element element = cache.get(id);
		if (element != null) return (User) element.getValue();

		User user = getUserFromDatabase(id);
		cache.put(new Element(id, user));
		return user;
	}

	public Review getReview(int id) throws SQLException {
		Cache cache = cacheManager.getCache(CACHE_REVIEWS);
		Element element = cache.get(id);
		if (element != null) return (Review) element.getValue();

		Review rev = getReviewFromDatabase(id);
		cache.put(new Element(id, rev));
		return rev;
	}

	protected Review getReviewFromDatabase(int id) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs =
				stmt.executeQuery("SELECT * FROM gr_reviews WHERE id='" + id
						+ "'");
		Review_impl review = null;
		if (rs.next()) {
			review = new Review_impl();
			review.setId(rs.getInt(1));
			review.setGoodReadsId(rs.getInt(2));
			review.setBook(getBook(rs.getInt(3)));
			review.setUser(getUser(rs.getInt(4)));
			review.setBody(rs.getString(5));
			review.setRating(rs.getInt(6));
			review.setUrl(rs.getString(7));
			review.setSpoilerFlag(rs.getBoolean(8));
		}
		rs.close();
		stmt.close();
		conn.close();
		return review;
	}

	public Book getBook(int id) throws SQLException {
		Cache cache = cacheManager.getCache(CACHE_BOOKS);
		Element element = cache.get(id);
		if (element != null) return (Book) element.getValue();

		Book rev = getBookFromDatabase(id);
		cache.put(new Element(id, rev));
		return rev;

	}

	protected Book getBookFromDatabase(int id) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs =
				stmt.executeQuery("SELECT * FROM gr_books,gr_books2authors WHERE gr_books.id='"
						+ id + "' AND gr_books2authors.book='" + id + "'");
		Book_impl book = null;
		while (rs.next()) {
			if (book == null) {
				book = new Book_impl();
				book.setId(rs.getInt(1));
				book.setGoodReadsId(rs.getInt(2));
				book.setIsbn(rs.getString(3));
				book.setIsbn13(rs.getString(4));
				book.setTitle(rs.getString(5));
				book.setLanguage(rs.getString(6));
				book.setAuthors(new LinkedList<Author>());
			}
			book.getAuthors().add(getAuthor(rs.getInt(9)));
		}
		rs.close();
		stmt.close();
		conn.close();
		return book;
	}

	public Author getAuthor(int int1) {
		return null;
	}

	protected Author getAuthorFromDatabase(int id) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs =
				stmt.executeQuery("SELECT * FROM gr_authors,gr_books2authors WHERE gr_authors.id='"
						+ id + "' AND gr_books2authors.author='" + id + "';");
		Author_impl author = null;
		while (rs.next()) {
			if (author == null) {
				author = new Author_impl();
				author.setId(rs.getInt(1));
				author.setGoodReadsId(rs.getInt(2));
				author.setName(rs.getString(3));
				author.setBooks(new LinkedList<Book>());
			}
		}
		rs.close();
		stmt.close();
		conn.close();
		return author;
	}

	protected User getUserFromDatabase(int id) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs =
				stmt.executeQuery("SELECT * FROM gr_users WHERE id='" + id
						+ "'");
		User_impl user = null;
		if (rs.next()) {
			user = new User_impl();
			user.setId(rs.getInt(1));
			user.setGoodReadsId(rs.getInt(2));
			user.setName(rs.getString(3));
			user.setLocation(rs.getString(4));
		}

		rs.close();
		stmt.close();
		conn.close();
		return user;
	}
}
