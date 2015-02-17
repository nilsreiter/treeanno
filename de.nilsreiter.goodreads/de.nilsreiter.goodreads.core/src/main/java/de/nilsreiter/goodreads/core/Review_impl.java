package de.nilsreiter.goodreads.core;

import de.nilsreiter.goodreads.api.Book;
import de.nilsreiter.goodreads.api.Review;
import de.nilsreiter.goodreads.api.User;

public class Review_impl implements Review {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	int goodReadsId;
	String body;
	String url;
	boolean spoilerFlag;
	int rating;
	User user;
	Book book;

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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getURL() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isSpoilerFlag() {
		return spoilerFlag;
	}

	public void setSpoilerFlag(boolean spoilerFlag) {
		this.spoilerFlag = spoilerFlag;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
