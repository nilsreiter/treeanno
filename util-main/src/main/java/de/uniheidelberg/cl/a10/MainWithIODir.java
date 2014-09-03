package de.uniheidelberg.cl.a10;

import java.io.File;

import org.kohsuke.args4j.Option;

/**
 * Main class with predefined input and output options.
 * 
 * @author reiter
 * 
 */
public abstract class MainWithIODir extends Main {

	@Option(name = "--input", aliases = { "-i" },
			usage = "The input directory.", required = true)
	protected File input = null;

	@Option(name = "--output", aliases = { "-o" },
			usage = "The output directory.", required = true)
	protected File output = null;

	public File getOutputDirectory() {
		if (!output.exists()) {
			output.mkdirs();
		}
		return output;
	}

	public File getInputDirectory() {
		return input;
	}

}
