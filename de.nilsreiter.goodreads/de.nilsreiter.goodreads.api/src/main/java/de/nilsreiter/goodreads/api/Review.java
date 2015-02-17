package de.nilsreiter.goodreads.api;

import java.io.Serializable;

public interface Review extends Serializable {
	int getId();

	int getGoodReadsId();

	String getBody();

	String getURL();

	boolean isSpoilerFlag();

	int getRating();

	User getUser();

	Book getBook();
}
