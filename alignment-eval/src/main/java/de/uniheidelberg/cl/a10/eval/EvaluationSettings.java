package de.uniheidelberg.cl.a10.eval;

import java.io.File;

import org.kohsuke.args4j.Option;

public class EvaluationSettings {
	@Option(name = "--gold", usage = "The gold standard", aliases = { "-g" })
	public File gold = new File("tasks/alignment/gold-ad.xml");

	@Option(name = "--data", usage = "Directory, in which ritual data files are stored. Default: null", aliases = { "-d" })
	public File dataDirectory = null;
}