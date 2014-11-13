package de.uniheidelberg.cl.a10.data2.io;

import java.io.IOException;

public interface Writer<T> {
	void write(T object) throws IOException;
}
