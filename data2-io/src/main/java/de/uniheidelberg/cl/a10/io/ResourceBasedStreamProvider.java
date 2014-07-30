package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.InputStream;

import de.uniheidelberg.cl.a10.api.DataStreamProvider;

public class ResourceBasedStreamProvider implements DataStreamProvider {

	String suffix = ".xml";

	@Override
	public InputStream findStreamFor(String objectName) throws IOException {
		if (!objectName.endsWith(suffix)) {
			objectName = objectName + suffix;
		}
		return Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(objectName);

	}

	@Override
	public InputStream findStreamFor(String objectName, String type)
			throws IOException {
		return this.findStreamFor(objectName);
	}

}
