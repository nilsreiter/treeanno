package de.uniheidelberg.cl.a10.patterns.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.TokenAlignmentReader;
import de.uniheidelberg.cl.a10.eval.AlignmentEvaluation;
import de.uniheidelberg.cl.a10.eval.AlignmentEvaluationSettings;
import de.uniheidelberg.cl.a10.eval.SingleResult;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.Operation;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;

public class MRSystemTuning extends MainWithInputSequences {
	@Option(name = "--output", usage = "Output file for XML output")
	File output = null;

	@Option(name = "--of", usage = "Output format.")
	Output.Style outputStyle = Output.Style.WIKI;

	AlignmentEvaluationSettings settings = new AlignmentEvaluationSettings();

	AlignmentEvaluation<Token> evaluation;

	public static void main(final String[] args) throws SecurityException,
	FrameNotFoundException, FrameElementNotFoundException, IOException,
	InstantiationException, IllegalAccessException,
	ClassNotFoundException, ParserConfigurationException, SAXException,
	IncompatibleException {
		Main.initProperties();
		MRSystemTuning asm = new MRSystemTuning();
		asm.processArguments(args, asm.settings);
		asm.run();
	}

	public void run() throws SecurityException, InstantiationException,
	IllegalAccessException, ClassNotFoundException,
	ParserConfigurationException, SAXException, IOException,
	IncompatibleException {
		evaluation =
				de.uniheidelberg.cl.a10.eval.Evaluation
						.getAlignmentEvaluation(this.settings.evaluationStyle);
		TokenAlignmentReader ar =
				new TokenAlignmentReader(settings.dataDirectory);
		Alignment<Token> goldDocument = ar.read(settings.gold);
		List<SimilarityConfiguration> confs = this.getConfigurationSpace();
		for (int i = 0; i < this.getArguments().size(); i++) {
			for (int j = i + 1; j < this.getArguments().size(); j++) {
				for (SimilarityConfiguration sc : confs) {
					try {
						MRSystemMain mrsm = new MRSystemMain();
						mrsm.similarityConf = sc;
						mrsm.setArguments(Arrays.asList(this.getArguments()
								.get(i), this.getArguments().get(j)));
						Alignment<Token> silver =
								new EventTokenConverter().convert(mrsm.align());
						SingleResult res =
								evaluation.evaluate(goldDocument, silver, sc);
						String pair =
								this.getArguments().get(i) + " "
										+ this.getArguments().get(j);
						Output op = Output.getOutput(outputStyle);
						op.setNumberFormatString("%1$.2f");
						op.setPrintHeader(false);
						res.setName(pair + " " + sc.getCommandLine());

						System.out.print(op.getString(res));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected List<SimilarityConfiguration> getConfigurationSpace() {
		List<SimilarityConfiguration> confs =
				new LinkedList<SimilarityConfiguration>();

		SimilarityConfiguration conf;

		for (double d = 0.0; d < 1.0; d += 0.1) {
			for (int w0 = 1; w0 < 3; w0++) {
				for (int w1 = 1; w1 < 3; w1++) {
					for (int w2 = 1; w2 < 3; w2++) {
						for (int w3 = 1; w3 < 3; w3++) {
							for (int w4 = 1; w4 < 3; w4++) {
								conf = new SimilarityConfiguration();
								conf.setThreshold(d);
								conf.similarityFunctions =
										Arrays.asList("WNS", "FNS", "VNS",
												"ATS", "GDS");
								conf.weights =
										Arrays.asList(String.valueOf(w0),
												String.valueOf(w1),
												String.valueOf(w2),
												String.valueOf(w3),
												String.valueOf(w4));
								conf.combination = Operation.GEO;
								confs.add(conf);
							}
						}
					}
				}
			}
		}

		return confs;

	}
}
