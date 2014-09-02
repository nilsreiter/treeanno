///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2003 Thomas Morton
// 
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
// 
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
// 
//You should have received a copy of the GNU Lesser General Public
//License along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////
package de.uniheidelberg.cl.a10.opennlp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import opennlp.maxent.MaxentModel;
import opennlp.maxent.io.SuffixSensitiveGISModelReader;

/**
 * This is the chunker modified by A10 in order to work with the augmented
 * models.
 * 
 * @author reiter
 */
public class A10TreebankChunker extends A10ChunkerME {

	/**
	 * Creates an English Treebank Chunker which uses the specified model file.
	 * 
	 * @param modelFile
	 *            The name of the maxent model to be used.
	 * @throws IOException
	 *             When the model file can't be open or read.
	 */
	public A10TreebankChunker(final String modelFile) throws IOException {
		this(new SuffixSensitiveGISModelReader(new File(modelFile)).getModel());
	}

	/**
	 * Creates an English Treebank Chunker which uses the specified model.
	 * 
	 * @param mod
	 *            The maxent model to be used.
	 */
	public A10TreebankChunker(final MaxentModel mod) {
		super(mod, new A10DefaultChunkerContextGenerator());
	}

	/**
	 * Creates an English Treebank Chunker which uses the specified model and
	 * context generator.
	 * 
	 * @param mod
	 *            The maxent model to be used.
	 * @param cg
	 *            The context generator to be used.
	 */
	public A10TreebankChunker(final MaxentModel mod,
			final A10ChunkerContextGenerator cg) {
		super(mod, cg);
	}

	/**
	 * Creates an English Treebank Chunker which uses the specified model and
	 * context generator which will be decoded using the specified beamSize.
	 * 
	 * @param mod
	 *            The maxent model to be used.
	 * @param cg
	 *            The context generator to be used.
	 * @param beamSize
	 *            The size of the beam used for decoding.
	 */
	public A10TreebankChunker(final MaxentModel mod,
			final A10ChunkerContextGenerator cg, final int beamSize) {
		super(mod, cg, beamSize);
	}

	private boolean validOutcome(final String outcome, final String prevOutcome) {

		if (outcome.startsWith("I-")) {
			if (prevOutcome == null) {
				return (false);
			} else {
				if (prevOutcome.equals("O")) {
					return (false);
				}
				try {
					if (!prevOutcome.substring(2).equals(outcome.substring(2))) {
						return (false);
					}
				} catch (java.lang.StringIndexOutOfBoundsException e) {
					// System.err.println(e.getMessage());

					return false;
				}
			}
		}
		return (true);
	}

	@Override
	protected boolean validOutcome(final String outcome, final String[] sequence) {
		String prevOutcome = null;
		if (sequence.length > 0) {
			prevOutcome = sequence[sequence.length - 1];
		}
		return validOutcome(outcome, prevOutcome);
	}

	/**
	 * Chunks tokenized input from stdin. <br>
	 * Usage: java opennlp.tools.chunker.EnglishTreebankChunker model <
	 * tokenized_sentences <br>
	 * 
	 * The tokenized sentences must have the following format:
	 * 
	 * <pre>
	 * ...
	 * TOKEN *TOKEN *POS %TOKEN %POS
	 * ...
	 * </pre>
	 * 
	 * % stands for a number, representing the domain. Each sentence should be
	 * followed by an empty line.
	 */
	public static void main(final String[] args) throws IOException {
		if (args.length < 1 || args.length > 2) {
			System.err
					.println("Usage: java de.uniheidelberg.cl.a10.opennlp.A10TreebankChunker model < tokenized_sentences");
			System.err
					.println("Usage: java de.uniheidelberg.cl.a10.opennlp.A10TreebankChunker model tokenized_sentences");
			System.exit(1);
		}
		A10TreebankChunker chunker = new A10TreebankChunker(args[0]);
		java.io.BufferedReader inReader = null;
		if (args.length == 1) {
			System.err.println("Reading from System.in");
			inReader = new java.io.BufferedReader(
					new java.io.InputStreamReader(System.in));
		} else if (args.length == 2) {
			System.err.println("Reading from file " + args[1]);
			inReader = new java.io.BufferedReader(new java.io.FileReader(
					new File(args[1])));

		} else {
			System.exit(1);
		}
		ArrayList<String> tokens = new ArrayList<String>();
		ArrayList<ArrayList<String>> context = new ArrayList<ArrayList<String>>();

		for (String line = inReader.readLine(); line != null; line = inReader
				.readLine()) {
			if (line.matches("^\\p{Space}*$")) {

				// The sentence has ended

				// Collecting the sentence context
				String[][] con = new String[context.size()][];
				for (int i = 0; i < context.size(); i++) {
					con[i] = new String[context.get(i).size()];
					for (int j = 0; j < context.get(i).size(); j++) {
						con[i][j] = context.get(i).get(j);
					}
				}

				// Letting the chunker chunk
				String[] chunks = chunker.chunk(tokens.toArray(), con);

				// Printing out the results
				for (int i = 0; i < chunks.length; i++) {
					System.out.print(chunks[i]);
					System.out.print(" ");
					System.out.print(tokens.get(i).substring(1) + " ");
					for (int j = 0; j < con.length; j++) {
						System.out.print(con[j][i] + " ");
					}
					System.out.println();
				}
				tokens.clear();
				context.clear();
				System.out.println();
			} else {

				// Splitting the line.
				// We expect that tts.length == 5
				String[] tts = line.split(" ");
				if (tts.length == 5) {
					tokens.add(tts[0]);

					for (int i = 1; i < tts.length; i++) {
						if (context.size() <= i - 1)
							context.add(new ArrayList<String>());
						context.get(i - 1).add(tts[i]);
					}
				} else {
					System.err
							.println("The line is non-empty, but seems not to be correctly formatted: "
									+ line);
				}
			}
		}
	}
}
