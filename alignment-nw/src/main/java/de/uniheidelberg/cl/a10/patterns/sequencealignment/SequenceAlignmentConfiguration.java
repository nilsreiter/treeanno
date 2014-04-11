package de.uniheidelberg.cl.a10.patterns.sequencealignment;

import org.kohsuke.args4j.Option;

import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;

public class SequenceAlignmentConfiguration extends SimilarityConfiguration {
	public enum Algorithm {
		SmithWaterman, NeedlemanWunsch, RecursiveSmithWaterman, DoubleNeedlemanWunsch
	};

	@Option(name = "--algorithm",
			usage = "The sequence alignment algorithm to use")
	public Algorithm algorithm = Algorithm.DoubleNeedlemanWunsch;

	@Override
	public String toString() {
		return this.getCommandLine();
	}
}
