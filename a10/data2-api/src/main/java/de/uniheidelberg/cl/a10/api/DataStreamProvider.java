package de.uniheidelberg.cl.a10.api;

import java.io.IOException;
import java.io.InputStream;

public interface DataStreamProvider {
	public InputStream findStreamFor(String objectName) throws IOException;

	public InputStream findStreamFor(String objectName, String type)
			throws IOException;

}
