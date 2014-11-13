package de.uniheidelberg.cl.a10.patterns.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.xml.sax.SAXException;

import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.data2.impl.FrameTokenEvent_impl;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityCalculationException;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunctionFactory;

@Deprecated
public class Inspector extends Main {

	@Argument(usage = "Two files in data2-XML format", required = true)
	List<File> arguments = new ArrayList<File>();

	Document[] rdoc = null;

	SimilarityConfiguration sConf = new SimilarityConfiguration();

	SimilarityFunction<FrameTokenEvent> sf_wordnet;

	SimilarityFunction<FrameTokenEvent> sf_verbnet;

	SimilarityFunction<FrameTokenEvent> sf_framenet;

	SimilarityFunction<FrameTokenEvent> sf_arg;

	SimilarityFunction<FrameTokenEvent> sf_distance;

	SimilarityFunction<FrameTokenEvent> sf_combined;

	enum Action {
		sim, exit, help, info, settings, set
	};

	public static void main(final String[] args) throws FileNotFoundException,
			ParserConfigurationException, SAXException, IOException,
			SecurityException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, CmdLineException {
		Main.initProperties();
		Inspector insp = new Inspector();
		insp.processArguments(args, insp.sConf);
		insp.init();
		insp.run();
	}

	protected String getSimilarityString(
			final SimilarityFunction<FrameTokenEvent> sf,
			final FrameTokenEvent f1, final FrameTokenEvent f2) {
		try {
			return sf.toString() + "(" + f1.getId() + "," + f2.getId() + ") = "
					+ sf.sim(f1, f2) + "\n";
		} catch (SimilarityCalculationException e) {
			return e.getMessage();
		}
	}

	protected void initSimilarityFunctions() throws InstantiationException,
			IllegalAccessException, FileNotFoundException, SecurityException,
			ClassNotFoundException {
		SimilarityFunctionFactory<FrameTokenEvent> factory =
				new SimilarityFunctionFactory<FrameTokenEvent>();
		System.out.println("Initialising similarity functions ... ");

		System.out.println("   WordNetSimilarity");
		sf_wordnet = factory.getSimilarityFunction("WordNetSimilarity", sConf);
		System.out.println("   VerbNetSimilarity");
		sf_verbnet = factory.getSimilarityFunction("VerbNetSimilarity", sConf);
		System.out.println("   FrameNetSimilarity");
		sf_framenet =
				factory.getSimilarityFunction("FrameNetSimilarity", sConf);
		System.out.println("   ArgumentTextSimilarity");
		sf_arg = factory.getSimilarityFunction("ArgumentTextSimilarity", sConf);
		System.out.println("   GaussianDistanceSimilarity");
		sf_distance =
				factory.getSimilarityFunction("GaussianDistanceSimilarity",
						sConf);
		System.out.println("   CombinedSimilarity");
		sf_combined = factory.getSimilarityFunction(sConf);

		sf_wordnet.readConfiguration(sConf);
		sf_verbnet.readConfiguration(sConf);
		sf_framenet.readConfiguration(sConf);
		sf_arg.readConfiguration(sConf);
		sf_distance.readConfiguration(sConf);
		sf_combined.readConfiguration(sConf);
	}

	protected void init() throws SecurityException,
			ParserConfigurationException, SAXException, IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		DataReader dr = new DataReader();
		System.out.println("Reading ritual documents ... ");
		rdoc =
				new Document[] { dr.read(arguments.get(0)),
				dr.read(arguments.get(1)) };

		this.initSimilarityFunctions();

	}

	protected void reset(final String[] args) throws CmdLineException,
			FileNotFoundException, SecurityException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		CmdLineParser parser = new CmdLineParser(this.sConf);
		parser.parseArgument(args);
		this.initSimilarityFunctions();
	}

	protected void run() throws FileNotFoundException,
			ParserConfigurationException, SAXException, IOException,
			CmdLineException, SecurityException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		// sf_combined_avg.addFeature(sf_arg);

		Scanner scanner = new Scanner(System.in);
		System.out.print("inspector> ");
		while (scanner.hasNext()) {

			String actionString = scanner.next();
			Action action;
			try {
				action = Action.valueOf(actionString);
			} catch (java.lang.IllegalArgumentException e) {
				System.out.println("Action " + actionString + " unknown.");
				action = Action.help;
			}
			switch (action) {
			case sim:
				this.sim(scanner.next(), scanner.next());
				break;
			case info:
				this.info(scanner.next(), scanner.next());
				break;
			case settings:
				System.out.println(this.sConf.getCommandLine());
				break;
			case set:
				this.reset(scanner.nextLine().trim().split("\\p{Space}+"));
				break;
			case exit:
				scanner.close();
				System.exit(0);
			default:
				this.help();
			}

			System.out.print("inspector> ");
		}
		scanner.close();

	}

	protected void info(final String rdDesc, final String next2) {
		int rd = 0;
		if (rdDesc.equalsIgnoreCase("r")) {
			rd = 1;
		}
		Document ritDoc = this.rdoc[rd];
		Frame f = (Frame) ritDoc.getById(next2);
		System.out.println(ritDoc.getId() + " " + f.getId());
		System.out.println("Target: " + f.getTarget().getId() + " ("
				+ f.getTarget().getSurface() + ")");
		for (FrameElement fe : f.getFrameElms()) {
			System.out.println(fe.getName() + ": " + fe.getTokens());
		}
	}

	protected void help() {
		System.out.println("Possible actions:");
		for (Action action : Action.values()) {
			System.out.println("  " + action);
		}

	}

	protected void sim(final String input0, final String input1) {
		FrameTokenEvent f1 = this.getFrameForInput(rdoc[0], input0);
		FrameTokenEvent f2 = this.getFrameForInput(rdoc[1], input1);
		if (f1 == null) {
			System.out.println("Frame with input " + input0 + " not found.");
		} else if (f2 == null) {
			System.out.println("Frame with input " + input1 + " not found.");
		} else {
			StringBuilder b = new StringBuilder();
			b.append(f1.getId()).append(": ").append(f1.getTarget().getLemma())
					.append("\n");
			b.append(f2.getId()).append(": ").append(f2.getTarget().getLemma())
					.append("\n");
			b.append(this.getSimilarityString(sf_wordnet, f1, f2));
			b.append(this.getSimilarityString(sf_verbnet, f1, f2));
			b.append(this.getSimilarityString(sf_framenet, f1, f2));
			b.append(this.getSimilarityString(sf_distance, f1, f2));
			b.append(this.getSimilarityString(sf_arg, f1, f2));
			b.append(this.getSimilarityString(sf_combined, f1, f2));
			System.out.println(b.toString());
		}
	}

	protected FrameTokenEvent getFrameForInput(final Document rd,
			final String input) {
		if (input.startsWith("f")) {
			return FrameTokenEvent_impl.getEvent((rd.getFrameById(input)));
		} else if (input.matches("[0-9]+")) {
			return FrameTokenEvent_impl.getEvent(rd.getFrames().get(
					Integer.valueOf(input)));
		}
		return null;
	}
}
