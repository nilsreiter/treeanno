package de.uniheidelberg.cl.a10;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import org.kohsuke.args4j.Option;

/**
 * Main class with predefined input and output options.
 * 
 * @author reiter
 * 
 */
public abstract class MainWithIO extends Main {

	@Option(name = "--input",
			aliases = { "-i" },
			usage = "The input file. If not set, System.in is used.")
	File input = null;

	@Option(name = "--output",
	// aliases = { "-o" },
			usage = "The output file. If not set, System.out is used.")
	File output = null;

	public InputStream getInputStream() {
		try {
			return this.getInputStreamForFileOption(input);
		} catch (FileNotFoundException e) {
			logger.warning("File " + input.getName() + " not found.");
			return System.in;
		}
	}

	public OutputStream getOutputStream() {
		return this.getOutputStreamForFileOption(output, System.out);
	}
}
