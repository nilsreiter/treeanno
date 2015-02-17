package de.nilsreiter.goodreads.api;

import java.io.Serializable;
import java.util.List;

public interface Author extends Serializable {
	int getId();

	int getGoodReadsId();

	String getName();

	List<Book> getBooks();
}
