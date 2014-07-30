package de.uniheidelberg.cl.a10.io;

import java.io.IOException;
import java.io.OutputStream;

import de.uniheidelberg.cl.a10.data2.io.Writer;

public abstract class AbstractWriter<T> implements Writer<T> {
	protected OutputStream outputStream;

	public AbstractWriter(final OutputStream os) {
		this.outputStream = os;
	}

	public void close() throws IOException {
		this.outputStream.close();
	}

	@Override
	public abstract void write(final T object) throws IOException;
}
