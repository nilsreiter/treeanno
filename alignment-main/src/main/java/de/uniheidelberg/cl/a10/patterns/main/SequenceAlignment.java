package de.uniheidelberg.cl.a10.patterns.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import neobio.alignment.IncompatibleScoringSchemeException;

import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.uniheidelberg.cl.a10.Main;
import de.uniheidelberg.cl.a10.NullOutputStream;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.RitualDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.FrameAlignmentDocument;
import de.uniheidelberg.cl.a10.data2.io.AlignmentWriter;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.AdvancedScoringScheme;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.DoubleNeedlemanWunsch;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.NeedlemanWunsch;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.PairwiseAlignment;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.PairwiseAlignmentAlgorithm;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.RecursiveSmithWaterman;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.ScoringScheme;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.SequenceAlignmentConfiguration;
import de.uniheidelberg.cl.a10.patterns.sequencealignment.SmithWaterman;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunctionFactory;

public class SequenceAlignment extends MainWithInputSequences {

	@Option(name = "--output", usage = "Output file for XML output")
	File output = null;

	@Option(name = "--noout", usage = "Suppress all output")
	boolean noout = false;

	@Option(name = "--dotoutput", usage = "Output file for dot output")
	File dotOutput = null;

	Alignment<Event> doc = null;

	SimilarityFunction<Event> similarityFunction = null;

	SequenceAlignmentConfiguration saConfig = new SequenceAlignmentConfiguration();

	public static void main(final String[] args) throws SecurityException,
			FrameNotFoundException, FrameElementNotFoundException, IOException,
			IncompatibleScoringSchemeException, ParserConfigurationException,
			SAXException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		// System.out.println(java.net.InetAddress.getLocalHost().getHostName());
		Main.initProperties();
		SequenceAlignment sa = new SequenceAlignment();
		sa.processArguments(args, sa.saConfig);
		sa.run();
	}

	public PairwiseAlignment<Event> getAlignmentFromAlgorithm(
			final RitualDocument doc1, final RitualDocument doc2)
			throws IncompatibleScoringSchemeException, SecurityException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, ParserConfigurationException, SAXException,
			IOException {

		PairwiseAlignmentAlgorithm<Event> algo;
		ScoringScheme<Event> scoringScheme = new AdvancedScoringScheme<Event>(
				Probability.fromProbability(this.saConfig.threshold),
				this.getSimilarityFunction());
		switch (this.saConfig.algorithm) {
		case SmithWaterman:
			algo = new SmithWaterman<Event>();
			algo.setScoring(scoringScheme);
			break;
		case RecursiveSmithWaterman:
			algo = new RecursiveSmithWaterman<Event>();
			algo.setScoring(scoringScheme);
			break;
		case NeedlemanWunsch:
			algo = new NeedlemanWunsch<Event>(scoringScheme);
			break;
		default:
		case DoubleNeedlemanWunsch:
			algo = new DoubleNeedlemanWunsch<Event>(scoringScheme);
			break;
		}

		Iterator<List<Event>> seqIter = getSequences().iterator();
		List<Event> seq1 = seqIter.next();
		List<Event> seq2 = seqIter.next();

		algo.setSequences(seq1, seq2);
		PairwiseAlignment<Event> alignment = algo.computePairwiseAlignment();
		return alignment;
	}

	public void run() throws SecurityException, FrameNotFoundException,
			FrameElementNotFoundException, IncompatibleScoringSchemeException,
			ParserConfigurationException, SAXException, IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		if (getArguments().size() != 2) {
			System.err.println("You need to specify exactly two input files.");
			System.exit(1);
		}

		PrintStream stdout = System.out;
		if (noout) {
			stdout = new PrintStream(new NullOutputStream());
		}

		RitualDocument d1, d2;
		DataReader dr = new DataReader();
		d1 = dr.read(getArguments().get(0));
		d2 = dr.read(getArguments().get(1));

		PairwiseAlignment<Event> alignment = this.getAlignmentFromAlgorithm(d1,
				d2);
		doc = FrameAlignmentDocument.fromPairwiseAlignment(alignment, d1, d2);

		AlignmentWriter dw = new AlignmentWriter(
				this.getOutputStreamForFileOption(output, stdout));
		dw.write(new EventTokenConverter().convert(doc));
		dw.close();

	}

	protected String getVerticalAlignmentTable(
			final PairwiseAlignment<Frame> alignment, final int cellwidth) {
		StringBuilder b = new StringBuilder();
		String formatString = "%1$" + cellwidth + "." + cellwidth
				+ "s %2$-4.4s %3$-" + cellwidth + "." + cellwidth + "s\n";
		Formatter formatter = new Formatter(b, Locale.US);
		formatter.format(formatString, "Sequence 1", "  ", "Sequence 2");
		for (int i = 0; i < alignment.getScoreTagLine().size(); i++) {
			Frame f1 = alignment.getGappedSequence1().get(i);
			Frame f2 = alignment.getGappedSequence2().get(i);

			formatter.format(formatString, getString(f1), alignment
					.getScoreTagLine().get(i), getString(f2));
		}
		formatter.close();
		return b.toString();
	}

	protected String getString(final Frame f) {
		if (f == null)
			return null;
		StringBuilder b = new StringBuilder();
		b.append(f.toExtendedString());
		return b.toString();
	}

	public SimilarityFunction<Event> getSimilarityFunction()
			throws FileNotFoundException, SecurityException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		if (this.similarityFunction == null) {
			SimilarityFunctionFactory<Event> factory = new SimilarityFunctionFactory<Event>();
			SimilarityFunction<Event> sf = factory
					.getSimilarityFunction(saConfig);

			this.similarityFunction = sf;
		}
		return this.similarityFunction;
	}
}
