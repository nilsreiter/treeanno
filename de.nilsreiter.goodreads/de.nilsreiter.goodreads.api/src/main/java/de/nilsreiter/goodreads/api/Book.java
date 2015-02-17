package de.nilsreiter.goodreads.api;

import java.io.Serializable;
import java.util.List;

public interface Book extends Serializable {
	int getId();

	int getGoodReadsId();

	String getISBN();

	String getISBN13();

	String getTitle();

	List<Author> getAuthors();

	String getLanguage();
}
