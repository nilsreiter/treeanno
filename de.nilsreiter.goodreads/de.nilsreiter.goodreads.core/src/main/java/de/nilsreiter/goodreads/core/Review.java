package de.nilsreiter.goodreads.core;

public interface Review {
	int getId();

	int getGoodReadsId();

	String getBody();

	String getURL();

	boolean isSpoilerFlag();

	int getRating();

	User getUser();

	Book getBook();
}
