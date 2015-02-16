package de.nilsreiter.goodreads.core;

import java.util.List;

public interface Book {
	int getId();

	int getGoodReadsId();

	String getISBN();

	String getISBN13();

	String getTitle();

	List<Author> getAuthors();
}
