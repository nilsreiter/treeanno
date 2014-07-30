package de.uniheidelberg.cl.a10.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public abstract class AbstractReader<T> {
	Map<File, T> documents = new HashMap<File, T>();

	public abstract T read(InputStream is) throws IOException;

	public T read(final File f) throws IOException {
		if (documents.containsKey(f)) return documents.get(f);
		InputStream is;
		if (f.getName().endsWith(".gz")) {
			is = new GZIPInputStream(new FileInputStream(f));
		} else {
			is = new FileInputStream(f);
		}
		T t = this.read(is);
		documents.put(f, t);
		is.close();
		return t;
	};
}
