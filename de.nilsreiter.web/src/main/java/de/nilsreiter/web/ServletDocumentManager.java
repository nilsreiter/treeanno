package de.nilsreiter.web;

import java.io.IOException;
import java.io.InputStream;

import de.uniheidelberg.cl.a10.api.DataStreamProvider;

public class ServletDocumentManager implements DataStreamProvider {

	@Override
	public InputStream findStreamFor(String objectName) throws IOException {
		return Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(objectName + ".xml");

	}

}
