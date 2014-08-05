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

	@Option(
			name = "--input",
			aliases = { "-i" },
			usage = "The input file or directory. If not set, System.in is used.")
	protected File input = null;

	@Option(
			name = "--output",
			usage = "The output file or directory. If not set, System.out is used.")
	protected File output = null;

	public InputStream getInputStream() {
		try {
			return this.getInputStreamForFileOption(input);
		} catch (FileNotFoundException e) {
			logger.error("File {} not found. Falling back on standard input.",
					input.getName());
			return System.in;
		}
	}

	public OutputStream getOutputStream() {
		return this.getOutputStreamForFileOption(output, System.out);
	}
}
