package de.nilsreiter.goodreads.core;

import java.util.List;

import de.nilsreiter.goodreads.api.Author;
import de.nilsreiter.goodreads.api.Book;

public class Author_impl implements Author {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	int goodReadsId;
	String name;
	List<Book> books;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

}
