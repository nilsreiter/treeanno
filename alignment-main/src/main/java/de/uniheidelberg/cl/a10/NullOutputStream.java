package de.uniheidelberg.cl.a10;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {
	@Override
	public void write(final int b) throws IOException {
	}
}
