package de.nilsreiter.goodreads.api;

import java.util.List;

public interface Author {
	int getId();

	int getGoodReadsId();

	String getName();

	List<Book> getBooks();
}
