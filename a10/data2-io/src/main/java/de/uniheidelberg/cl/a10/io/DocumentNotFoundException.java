package de.uniheidelberg.cl.a10.io;

import java.io.IOException;

public class DocumentNotFoundException extends IOException {

	private static final long serialVersionUID = 1L;

	String id;

	public DocumentNotFoundException(String id) {
		super();
		this.id = id;
	}

}
