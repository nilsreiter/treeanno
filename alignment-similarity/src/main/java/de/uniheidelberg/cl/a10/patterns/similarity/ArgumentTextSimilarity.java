package de.uniheidelberg.cl.a10.patterns.similarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniheidelberg.cl.a10.Util;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class ArgumentTextSimilarity extends AbstractSimilarityFunction<Event>
		implements SimilarityFunction<Event> {
	public static final long serialVersionUID = 3l;

	boolean debug = false;
	// boolean resolveCoreference = false;
	Map<String, Double> idfMap;

	public double idf(final String lemma) {
		if (this.idfMap != null && this.idfMap.containsKey(lemma))
			return this.idfMap.get(lemma);
		return 1.0;
	}

	public Probability sim(final String[] bow1, final String[] bow2) {
		Set<String> bagOfWords1 = new HashSet<String>();
		Set<String> bagOfWords2 = new HashSet<String>();

		List<String> l1 = Arrays.asList(bow1);
		List<String> l2 = Arrays.asList(bow2);
		bagOfWords1.addAll(l1);
		bagOfWords2.addAll(l2);

		return this.sim(bagOfWords1, bagOfWords2, Double.MAX_VALUE);
	}

	public Probability sim(final Set<String> bagOfWords1,
			final Set<String> bagOfWords2, final double llimit) {

		double retval = 0.0;
		double size1 = 0.0;
		double size2 = 0.0;

		for (String w1 : bagOfWords1) {
			size1 += this.idf(w1);
			for (String w2 : bagOfWords2) {
				if (llimit == Double.MAX_VALUE) {
					if (w1.equals(w2))
						retval += this.idf(w1);
				} else {
					if (Levenshtein.distance(w1, w2).getProbability() > llimit) {
						retval += this.idf(w1);
					}
				}
			}
		}
		for (String w2 : bagOfWords2)
			size2 += this.idf(w2);

		if (size1 != 0 || size2 != 0)
			retval /= (size1 + size2);

		return Probability.fromProbability(Util.scale(0.0, 0.5, 0.0, 1.0,
				retval));
	}

	@Override
	public Probability sim(final Event arg0, final Event arg1)
			throws IncompatibleException {
		if (this.config == null)
			throw new UnconfiguredException();
		if (positivePreCheck(arg0, arg1))
			return Probability.ONE;
		if (negativePreCheck(arg0, arg1))
			return Probability.NULL;
		if (arg0.equals(arg1))
			return Probability.ONE;
		Probability p = this.getFromHistory(arg0, arg1);
		if (p != null)
			return p;

		Set<String> bagOfWords1 = this.getBagOfWords(arg0.getFrame());
		Set<String> bagOfWords2 = this.getBagOfWords(arg1.getFrame());

		p = this.sim(bagOfWords1, bagOfWords2, Double.MAX_VALUE);
		this.putInHistory(arg0, arg1, p);
		return p;

	}

	private Set<String> getBagOfWords(final Frame arg0) {
		Set<String> s = new HashSet<String>();
		for (FrameElement fe : arg0.getFrameElms()) {
			for (Token token : fe) {
				String lemma = token.getLemma();
				if (lemma.contains(" ")) {
					for (String lp : lemma.split(" ")) {
						s.add(lp);
					}
				} else
					s.add(token.getLemma());
			}
			if (this.config.sf_arg_coref) {
				for (Mention m : fe.getCoreferringMentions()) {
					for (Mention m2 : m.getEntity().getMentions()) {
						for (Token token : m2) {
							s.add(token.getLemma());
						}
					}
				}
			}
		}
		return s;
	}

	@Override
	public void readConfiguration(final SimilarityConfiguration tc) {
		super.readConfiguration(tc);
		this.idfMap = null;
		if (tc.sf_arg_idf) {
			try {
				this.idfMap = readIDF("data/train.sconll");
				// dbs = new SimDBs();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected static Map<String, Double> readIDF(final String filename)
			throws IOException {
		// init general variables
		Map<String, Double> retval = new HashMap<String, Double>();
		int docs = 0;

		// init reader
		BufferedReader br = new BufferedReader(new FileReader(
				new File(filename)));
		String line;
		List<String> words = new LinkedList<String>();

		// read file
		while ((line = br.readLine()) != null) {
			// file is supposed to contain all documents
			// -> detect end of document; here: empty line
			if (line.equals("")) {
				// ignore 1-word-sentences
				if (words.size() > 1) {
					// increase document counter for each word
					for (String w : words) {
						if (!retval.containsKey(w))
							retval.put(w, 0.0);
						retval.put(w, retval.get(w) + 1.0);
					}
				}
				words = new LinkedList<String>();
				docs++;
			}
			// process current word if line is not a document end
			else {
				// word form is in the 2nd column in CoNLL format
				String w = line.split("\t")[1];
				// only add a word type once per document
				if (!words.contains(w))
					words.add(w);
			}
		}
		br.close();
		// if file ended without a document ending at the end
		if (words.size() > 1) {
			// same as before
			for (String w : words) {
				if (!retval.containsKey(w))
					retval.put(w, 0.0);
				retval.put(w, retval.get(w) + 1.0);
			}
			docs++;
		}

		// up until here, we stored document frequencies for each word
		// lets invert and log them to get IDF values...
		for (String w : retval.keySet())
			retval.put(w, Math.log(docs / retval.get(w)));

		return retval;
	}
}
