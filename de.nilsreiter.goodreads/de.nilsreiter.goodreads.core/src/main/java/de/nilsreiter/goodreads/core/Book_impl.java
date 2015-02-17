package de.nilsreiter.goodreads.core;

import java.util.List;

import de.nilsreiter.goodreads.api.Author;
import de.nilsreiter.goodreads.api.Book;

public class Book_impl implements Book {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	int goodReadsId;
	String isbn;
	String isbn13;
	String title;
	String language;
	List<Author> authors;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGoodReadsId() {
		return goodReadsId;
	}

	public void setGoodReadsId(int goodReadsId) {
		this.goodReadsId = goodReadsId;
	}

	public String getISBN() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getISBN13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
}
