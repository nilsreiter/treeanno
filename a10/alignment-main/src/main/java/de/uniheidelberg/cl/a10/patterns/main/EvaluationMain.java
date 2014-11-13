package de.uniheidelberg.cl.a10.patterns.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentReader;
import de.uniheidelberg.cl.a10.eval.AlignmentEvaluation;
import de.uniheidelberg.cl.a10.eval.AlignmentEvaluationSettings;
import de.uniheidelberg.cl.a10.eval.SingleResult;

public class EvaluationMain extends Main {
	static final String BMM_ERROR_MESSAGE =
			"We can no longer evaluate this data" + " format. Exiting.";

	AlignmentEvaluationSettings settings = new AlignmentEvaluationSettings();

	@Option(name = "--silver",
			usage = "The system output. If not set, the system output will be"
					+ " read from STDIN. If the filename ends with .bmm, we"
					+ " assume the file to be a hidden markov model.",
					aliases = { "-s" })
	File silver = null;

	@Option(name = "--of", usage = "Output format.")
	Output.Style outputStyle = Output.Style.CSV;

	@Option(name = "--rowhead",
			usage = "If --of TABLE is given, this value is printed as row head")
	String rowhead = null;

	@Option(name = "--printheader")
	boolean printHeader = false;

	@Option(name = "--html", usage = "Produce an HTML page", aliases = {})
	File htmlOutput = null;

	@Option(
			name = "--ratioThreshold",
			usage = "If set to a double value, the HTML output includes highlighting of interesting areas. Only applicable if --html is specified.")
	double alignmentRatioThreshold = Double.NaN;

	boolean highlightInteresting = false;

	AlignmentEvaluation<Token> evaluation;

	public void run() throws IOException, ValidityException, ParsingException {
		SingleResult res = null;
		AlignmentReader<Token> ar =
				new AlignmentReader<Token>(settings.dataDirectory);
		Alignment<Token> goldDocument = ar.read(settings.gold);
		evaluation =
				de.uniheidelberg.cl.a10.eval.Evaluation
				.getAlignmentEvaluation(this.settings.evaluationStyle);

		Builder xBuilder = new Builder();

		Document silverDoc =
				xBuilder.build(this.getInputStreamForFileOption(silver));

		Alignment<Token> silver = ar.read(silverDoc);
		res = this.evaluateAlignment(goldDocument, silver, null);
		Output output = Output.getOutput(outputStyle);
		// output.setNumberFormatString("%1$.4f");
		output.setPercentage(false);
		output.setPrintHeader(printHeader);
		StringBuilder b = new StringBuilder();
		res.setName(rowhead);
		b.append(output.getString(res));
		System.out.print(b.toString());

	}

	public SingleResult evaluateAlignment(final Alignment<Token> goldDocument,
			final Alignment<Token> silverDocument, final String name)
			throws FileNotFoundException, IOException {
		SingleResult res;
		res = this.evaluation.evaluate(goldDocument, silverDocument, name);

		return res;
	}

	public static void main(final String[] args) throws IOException,
			ValidityException, ParsingException {
		EvaluationMain eval = new EvaluationMain();
		eval.processArguments(args, eval.settings);
		eval.run();
	}

}
