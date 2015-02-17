package de.nilsreiter.goodreads.api;

import java.io.Serializable;

public interface User extends Serializable {
	int getId();

	int getGoodReadsId();

	String getName();

	String getLocation();
}
