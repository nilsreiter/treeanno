package de.uniheidelberg.cl.a10.main;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.eval.ClusterEvaluationStyle;
import de.uniheidelberg.cl.a10.eval.EvaluationSettings;
import de.uniheidelberg.cl.a10.patterns.main.Output;

public class ClusterEvaluationConf extends EvaluationSettings {
	@Argument
	List<File> arguments = new LinkedList<File>();

	@Option(name = "--silver", usage = "The system output. If not set, the system output will be"
			+ " read from STDIN. ", aliases = { "-sf" })
	public File silver = null;

	@Option(name = "--of", usage = "Output format")
	public Output.Style outputStyle = Output.Style.CSV;

	@Option(name = "--restrict", usage = "Restricts the evaluation to specified gold cluster")
	String clusterId = null;

	@Option(name = "--style")
	ClusterEvaluationStyle style = ClusterEvaluationStyle.Rand2;

}
