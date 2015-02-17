package de.nilsreiter.goodreads.core;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.nilsreiter.goodreads.api.Book;
import de.nilsreiter.goodreads.api.Review;
import de.nilsreiter.goodreads.api.User;

public class TestGoodReadsStorage {
	static DataSource ds;

	@BeforeClass
	public static void setUpClass() {
		String host = "localhost";
		String port = "3309";
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername("reiterns");
		dataSource.setPassword("bybNoaKni");
		dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/reiter");
		ds = dataSource;
	}

	GoodReadsStorage grs;

	@Before
	public void setUp() {
		grs = new GoodReadsStorage(ds);
	}

	@Test
	public void testGetUser() throws SQLException {
		User user = grs.getUser(1319754);
		assertEquals(1319754, user.getId());
		assertEquals(1319754, user.getGoodReadsId());
		assertEquals("Natascha", user.getName());
		assertEquals("Büdingen, Germany", user.getLocation());
	}

	@Test
	public void testGetReview() throws SQLException {
		Review review = grs.getReview(221997);
		assertEquals(221997, review.getId());
		assertEquals(221997, review.getGoodReadsId());
		assertEquals("\n      These are really fun!\n  ", review.getBody());
		assertEquals("Arezou", review.getUser().getName());
		assertEquals(false, review.isSpoilerFlag());
		assertEquals(2, review.getRating());
		assertEquals("https://www.goodreads.com/review/show/221997",
				review.getURL());
	}

	@Test
	public void testGetBook() throws SQLException {
		Book book = grs.getBook(1);
		assertEquals(1, book.getId());
		assertEquals(1, book.getGoodReadsId());
		assertEquals(
				"Harry Potter and the Half-Blood Prince (Harry Potter, #6)",
				book.getTitle());
		assertEquals("0439785960", book.getISBN());
		assertEquals("9780439785969", book.getISBN13());
		assertEquals("eng", book.getLanguage());
		assertEquals(2, book.getAuthors().size());
	}
}
