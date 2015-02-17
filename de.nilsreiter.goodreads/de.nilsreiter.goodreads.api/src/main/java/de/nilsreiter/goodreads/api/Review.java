package de.nilsreiter.goodreads.api;

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
