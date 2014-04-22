package de.uniheidelberg.cl.a10.eval;

import java.io.File;

import org.kohsuke.args4j.Option;

public class EvaluationSettings {

	@Option(name = "--style", usage = "Evaluation style to use")
	public Style evaluationStyle = Style.BLANC;

	@Option(name = "--gold", usage = "The gold standard, an n-ary alignment document", aliases = { "-g" })
	public File gold = new File("tasks/alignment/gold-ad.xml");

	@Option(name = "--data", usage = "Directory, in which ritual data files are stored. Default: null", aliases = { "-d" })
	public File dataDirectory = null;
}
