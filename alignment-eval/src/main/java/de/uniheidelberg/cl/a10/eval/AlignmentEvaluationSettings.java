package de.uniheidelberg.cl.a10.eval;

import org.kohsuke.args4j.Option;

public class AlignmentEvaluationSettings extends EvaluationSettings {

	@Option(name = "--style", usage = "Evaluation style to use")
	public Style evaluationStyle = Style.BLANC;

}
