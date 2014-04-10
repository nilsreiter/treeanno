package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractWriter<T> {
	OutputStream outputStream;

	public AbstractWriter(final OutputStream os) {
		this.outputStream = os;
	}

	public void close() throws IOException {
		this.outputStream.close();
	}

	public abstract void write(final T object) throws IOException;
}
