///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2002 Jason Baldridge and Gann Bierner
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////

package de.uniheidelberg.cl.a10.opennlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import opennlp.maxent.DataStream;
import opennlp.maxent.Evalable;
import opennlp.maxent.EventCollector;
import opennlp.maxent.EventStream;
import opennlp.maxent.GISModel;
import opennlp.maxent.MaxentModel;
import opennlp.maxent.PlainTextByLineDataStream;
import opennlp.maxent.TwoPassDataIndexer;
import opennlp.maxent.io.SuffixSensitiveGISModelWriter;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.ngram.NGramModel;
import opennlp.tools.ngram.Token;
import opennlp.tools.ngram.TokenList;
import opennlp.tools.postag.POSContextGenerator;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.TagDictionary;
import opennlp.tools.util.BeamSearch;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Pair;
import opennlp.tools.util.Sequence;

/**
 * <p>
 * A part-of-speech tagger that uses maximum entropy. Trys to predict whether
 * words are nouns, verbs, or any of 70 other POS tags depending on their
 * surrounding context.
 * </p>
 * <p>
 * A10 changes: swapped build of dictionary out of main into own method
 * buildDict(File,String,int); buildDict treats inverse order of POS and word
 * (Hal Daumé's format) changed default cutoff in main method
 * </p>
 * 
 * @author Gann Bierner, zeller
 * @version $Revision: 1.28 $, $Date: 2007/05/17 13:27:34 $
 */
public class A10POSTaggerME implements opennlp.maxent.Evalable, POSTagger {

	/**
	 * The maximum entropy model to use to evaluate contexts.
	 */
	protected MaxentModel posModel;

	/**
	 * The feature context generator.
	 */
	protected A10DefaultPOSContextGenerator contextGen;

	/**
	 * Tag dictionary used for restricting words to a fixed set of tags.
	 */
	protected TagDictionary tagDictionary;

	protected Dictionary ngramDictionary;

	/**
	 * Says whether a filter should be used to check whether a tag assignment is
	 * to a word outside of a closed class.
	 */
	protected boolean useClosedClassTagsFilter = false;

	private static final int DEFAULT_BEAM_SIZE = 3;

	/**
	 * The size of the beam to be used in determining the best sequence of pos
	 * tags.
	 */
	protected int size;

	private Sequence bestSequence;

	/** The search object used for search multiple sequences of tags. */
	protected BeamSearch beam;

	/**
	 * Creates a new tagger with the specified model and tag dictionary.
	 * 
	 * @param model
	 *            The model used for tagging.
	 * @param tagdict
	 *            The tag dictionary used for specifing a set of valid tags.
	 */
	public A10POSTaggerME(final MaxentModel model, final TagDictionary tagdict) {
		this(model, new A10DefaultPOSContextGenerator(null), tagdict);
	}

	/**
	 * Creates a new tagger with the specified model and n-gram dictionary.
	 * 
	 * @param model
	 *            The model used for tagging.
	 * @param dict
	 *            The n-gram dictionary used for feature generation.
	 */
	public A10POSTaggerME(final MaxentModel model, final Dictionary dict) {
		this(model, new A10DefaultPOSContextGenerator(dict));
	}

	/**
	 * Creates a new tagger with the specified model, n-gram dictionary, and tag
	 * dictionary.
	 * 
	 * @param model
	 *            The model used for tagging.
	 * @param dict
	 *            The n-gram dictionary used for feature generation.
	 * @param tagdict
	 *            The dictionary which specifies the valid set of tags for some
	 *            words.
	 */
	public A10POSTaggerME(final MaxentModel model, final Dictionary dict,
			final TagDictionary tagdict) {
		this(DEFAULT_BEAM_SIZE, model, new A10DefaultPOSContextGenerator(dict),
				tagdict);
	}

	/**
	 * Creates a new tagger with the specified model and context generator.
	 * 
	 * @param model
	 *            The model used for tagging.
	 * @param cg
	 *            The context generator used for feature creation.
	 */
	public A10POSTaggerME(final MaxentModel model,
			final A10DefaultPOSContextGenerator cg) {
		this(DEFAULT_BEAM_SIZE, model, cg, null);
	}

	/**
	 * Creates a new tagger with the specified model, context generator, and tag
	 * dictionary.
	 * 
	 * @param model
	 *            The model used for tagging.
	 * @param cg
	 *            The context generator used for feature creation.
	 * @param tagdict
	 *            The dictionary which specifies the valid set of tags for some
	 *            words.
	 */
	public A10POSTaggerME(final MaxentModel model,
			final A10DefaultPOSContextGenerator cg, final TagDictionary tagdict) {
		this(DEFAULT_BEAM_SIZE, model, cg, tagdict);
	}

	/**
	 * Creates a new tagger with the specified beam size, model, context
	 * generator, and tag dictionary.
	 * 
	 * @param beamSize
	 *            The number of alturnate tagging considered when tagging.
	 * @param model
	 *            The model used for tagging.
	 * @param cg
	 *            The context generator used for feature creation.
	 * @param tagdict
	 *            The dictionary which specifies the valid set of tags for some
	 *            words.
	 */
	public A10POSTaggerME(final int beamSize, final MaxentModel model,
			final A10DefaultPOSContextGenerator cg, final TagDictionary tagdict) {
		this.size = beamSize;
		this.posModel = model;
		this.contextGen = cg;
		this.beam = new PosBeamSearch(this.size, cg, model);
		this.tagDictionary = tagdict;
	}

	@Override
	public String getNegativeOutcome() {
		return "";
	}

	/**
	 * Returns the number of different tags predicted by this model.
	 * 
	 * @return the number of different tags predicted by this model.
	 */
	public int getNumTags() {
		return this.posModel.getNumOutcomes();
	}

	@Override
	public EventCollector getEventCollector(final Reader r) {
		return new A10POSEventCollector(r, this.contextGen);
	}

	@Override
	public List<?> tag(final List sentence) { // !
		this.bestSequence = this.beam.bestSequence(sentence, null);
		return this.bestSequence.getOutcomes();
	}

	@Override
	public String[] tag(final String[] sentence) {
		this.bestSequence = this.beam.bestSequence(sentence, null);
		List<?> t = this.bestSequence.getOutcomes();
		return t.toArray(new String[t.size()]);
	}

	/**
	 * Returns at most the specified number of taggings for the specified
	 * sentence.
	 * 
	 * @param numTaggings
	 *            The number of tagging to be returned.
	 * @param sentence
	 *            An array of tokens which make up a sentence.
	 * @return At most the specified number of taggings for the specified
	 *         sentence.
	 */
	public String[][] tag(final int numTaggings, final String[] sentence) {
		Sequence[] bestSequences = this.beam.bestSequences(numTaggings,
				sentence, null);
		String[][] tags = new String[bestSequences.length][];
		for (int si = 0; si < tags.length; si++) {
			List<?> t = bestSequences[si].getOutcomes();
			tags[si] = t.toArray(new String[t.size()]);
		}
		return tags;
	}

	/**
	 * Populates the specified array with the probabilities for each tag of the
	 * last tagged sentence.
	 * 
	 * @param probs
	 *            An array to put the probabilities into.
	 */
	public void probs(final double[] probs) {
		this.bestSequence.getProbs(probs);
	}

	/**
	 * Returns an array with the probabilities for each tag of the last tagged
	 * sentence.
	 * 
	 * @return an array with the probabilities for each tag of the last tagged
	 *         sentence.
	 */
	public double[] probs() {
		return this.bestSequence.getProbs();
	}

	@Override
	public String tag(final String sentence) {
		ArrayList<String> toks = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(sentence);
		while (st.hasMoreTokens())
			toks.add(st.nextToken());
		List<?> tags = tag(toks); // !
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tags.size(); i++)
			sb.append(toks.get(i) + "/" + tags.get(i) + " ");
		return sb.toString().trim();
	}

	@Override
	public void localEval(final MaxentModel posModel, final Reader r,
			final Evalable e, final boolean verbose) {

		this.posModel = posModel;
		float total = 0, correct = 0, sentences = 0, sentsCorrect = 0;
		BufferedReader br = new BufferedReader(r);
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sentences++;
				Pair p = A10POSEventCollector.convertAnnotatedString(line);
				List<?> words = (List<?>) p.a;
				List<?> outcomes = (List<?>) p.b;
				List<?> tags = this.beam.bestSequence(words, null)
						.getOutcomes();

				int c = 0;
				boolean sentOk = true;
				for (Iterator<?> t = tags.iterator(); t.hasNext(); c++) {
					total++;
					String tag = (String) t.next();
					if (tag.equals(outcomes.get(c)))
						correct++;
					else
						sentOk = false;
				}
				if (sentOk)
					sentsCorrect++;
			}
		} catch (IOException E) {
			E.printStackTrace();
		}

		System.out.println("Accuracy         : " + correct / total);
		System.out.println("Sentence Accuracy: " + sentsCorrect / sentences);

	}

	private class PosBeamSearch extends BeamSearch {

		PosBeamSearch(final int size, final POSContextGenerator cg,
				final MaxentModel model) {
			super(size, cg, model);
		}

		PosBeamSearch(final int size, final POSContextGenerator cg,
				final MaxentModel model, final int cacheSize) {
			super(size, cg, model, cacheSize);
		}

		@Override
		protected boolean validSequence(final int i,
				final Object[] inputSequence, final String[] outcomesSequence,
				final String outcome) {
			if (A10POSTaggerME.this.tagDictionary == null) {
				return true;
			} else {
				String[] tags = A10POSTaggerME.this.tagDictionary
						.getTags(inputSequence[i].toString());
				if (tags == null) {
					return true;
				} else {
					return Arrays.asList(tags).contains(outcome);
				}
			}
		}
	}

	public String[] getOrderedTags(final List<?> words, final List<?> tags,
			final int index) {
		return getOrderedTags(words, tags, index, null);
	}

	public String[] getOrderedTags(final List<?> words, final List<?> tags,
			final int index, final double[] tprobs) {
		double[] probs = this.posModel.eval(this.contextGen.getContext(index,
				words.toArray(), tags.toArray(new String[tags.size()])));
		String[] orderedTags = new String[probs.length];
		for (int i = 0; i < probs.length; i++) {
			int max = 0;
			for (int ti = 1; ti < probs.length; ti++) {
				if (probs[ti] > probs[max]) {
					max = ti;
				}
			}
			orderedTags[i] = this.posModel.getOutcome(max);
			if (tprobs != null) {
				tprobs[i] = probs[max];
			}
			probs[max] = 0;
		}
		return orderedTags;
	}

	/**
	 * Trains a new model.
	 * 
	 * @param evc
	 * @param modelFile
	 * @throws IOException
	 */
	public static void train(final EventStream evc, final File modelFile)
			throws IOException {
		GISModel model = train(evc, 100, 5);
		new SuffixSensitiveGISModelWriter(model, modelFile).persist();
	}

	/**
	 * Trains a new model
	 * 
	 * @param es
	 * @param iterations
	 * @param cut
	 * @return the new model
	 * @throws IOException
	 */
	public static GISModel train(final EventStream es, final int iterations,
			final int cut) throws IOException {
		return opennlp.maxent.GIS.trainModel(iterations,
				new TwoPassDataIndexer(es, cut));
	}

	private static void usage() {
		System.err
				.println("Usage: A10POSTaggerME [-encoding encoding] [-dict dict_file] training model [cutoff] [iterations]");
		System.err
				.println("This trains a new model on the specified training file and writes the trained model to the model file.");
		System.err
				.println("-encoding Specifies the encoding of the training file");
		System.err
				.println("-dict Specifies that a dictionary file should be created for use in distinguising between rare and non-rare words");
		System.exit(1);
	}

	/**
	 * Method building the dictionary.
	 * <p>
	 * A10 modification: the wanted input format is: POS_*word_#word ; e.g.
	 * NN_*bottle_1bottle
	 * </p>
	 * 
	 * @param inFile
	 * @param dict
	 * @param cutoff
	 */
	private static void buildDict(final File inFile, final String dict,
			final int cutoff) {
		try {
			System.err.println("Building dictionary");

			NGramModel ngramModel = new NGramModel();

			DataStream data = new opennlp.maxent.PlainTextByLineDataStream(
					new java.io.FileReader(inFile));
			while (data.hasNext()) {
				String tagStr = (String) data.nextToken();
				String[] tt = tagStr.split(" ");
				Token[] words = new Token[tt.length];
				for (int wi = 0; wi < words.length; wi++) {
					words[wi] =
					// originally:
					// Token.create(tt[wi].substring(0,tt[wi].lastIndexOf('_')));
					// now: word token is not at beginning, but on 2nd and 3rd
					// place, with preceding * or # <-> give an offset of 2
					Token.create(tt[wi].substring(tt[wi].lastIndexOf('_') + 2));
				}

				ngramModel.add(new TokenList(words), 1, 1);
			}

			System.out.println("Saving the dictionary");

			ngramModel.cutoff(cutoff, Integer.MAX_VALUE);
			Dictionary dictionary = ngramModel.toDictionary(true);

			/*
			 * Iterator iter = dictionary.iterator();
			 * System.out.println("dictsize:" + dictionary.size() +
			 * " -- dict: "); while (iter.hasNext()){
			 * System.out.println(iter.next().toString()); }
			 */

			dictionary.serialize(new FileOutputStream(dict));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * Trains a new pos model.
	 * </p>
	 * 
	 * <p>
	 * A10 modifications: swapped build of dictionary, set default cutoff from 5
	 * to 2
	 * </p>
	 * <p>
	 * Usage: java opennlp.postag.POStaggerME [-encoding charset] [-d dict_file]
	 * data_file new_model_name (iterations cutoff)?
	 * </p>
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InvalidFormatException
	 * 
	 */
	public static void main(final String[] args) throws IOException,
			InvalidFormatException {
		if (args.length == 0) {
			usage();
		}
		int ai = 0;
		try {
			String encoding = null;
			String dict = null;
			while (args[ai].startsWith("-")) {
				if (args[ai].equals("-encoding")) {
					ai++;
					if (ai < args.length) {
						encoding = args[ai++];
					} else {
						usage();
					}
				} else if (args[ai].equals("-dict")) {
					ai++;
					if (ai < args.length) {
						dict = args[ai++];
					} else {
						usage();
					}
				} else {
					System.err.println("Unknown option " + args[ai]);
					usage();
				}
			}
			File inFile = new File(args[ai++]);
			File outFile = new File(args[ai++]);
			int cutoff = 5;
			int iterations = 100;
			if (args.length > ai) {
				cutoff = Integer.parseInt(args[ai++]);
				iterations = Integer.parseInt(args[ai++]);
			}
			GISModel mod;
			if (dict != null) {
				/* modified: swapped into separate method */
				buildDict(inFile, dict, cutoff);
			}
			EventStream es;
			if (encoding == null) {
				if (dict == null) {
					es = new A10POSEventStream(new PlainTextByLineDataStream(
							new InputStreamReader(new FileInputStream(inFile))));
				} else {
					es = new A10POSEventStream(
							new PlainTextByLineDataStream(
									new InputStreamReader(new FileInputStream(
											inFile))), new Dictionary(
									new FileInputStream(dict)));
				}
			} else {
				if (dict == null) {
					es = new A10POSEventStream(new PlainTextByLineDataStream(
							new InputStreamReader(new FileInputStream(inFile),
									encoding)));
				} else {
					es = new A10POSEventStream(new PlainTextByLineDataStream(
							new InputStreamReader(new FileInputStream(inFile),
									encoding)), new Dictionary(
							new FileInputStream(dict)));
				}
			}
			mod = train(es, iterations, cutoff);
			System.out.println("Saving the model as: " + outFile);
			new SuffixSensitiveGISModelWriter(mod, outFile).persist();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}