package de.uniheidelberg.cl.a10.semafortraining.uima;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileWriterHandling {
	static Map<File, FileWriter> fileWriters = new HashMap<File, FileWriter>();

	public static FileWriter get(final File file) throws IOException {
		if (!fileWriters.containsKey(file)) {
			fileWriters.put(file, new FileWriter(file, false));
			if (!file.exists()) {
				file.createNewFile();
			}
		}
		return fileWriters.get(file);
	}
}
