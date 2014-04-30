package de.nilsreiter.event.similarity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniheidelberg.cl.a10.Util;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.IncompatibleException;
import de.uniheidelberg.cl.a10.patterns.similarity.Levenshtein;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityConfiguration;

public class ArgumentText implements DBSimilarityFunction {
	public static final long serialVersionUID = 3l;

	boolean debug = false;

	boolean includeCoref;

	public double idf(final String lemma) {
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
		Probability p = Probability.NULL;
		if (arg0.equals(arg1))
			return Probability.ONE;

		Set<String> bagOfWords1 = this.getBagOfWords(arg0);
		Set<String> bagOfWords2 = this.getBagOfWords(arg1);

		p = this.sim(bagOfWords1, bagOfWords2, Double.MAX_VALUE);
		return p;

	}

	private Set<String> getBagOfWords(final Event event) {
		Set<String> s = new HashSet<String>();
		for (String feRole : event.getArguments().keySet()) {
			for (AnnotationObjectInDocument aoi : event.getArguments().get(
					feRole)) {
				Token token = (Token) aoi;
				String lemma = ((Token) aoi).getLemma();
				if (lemma.contains(" ")) {
					for (String lp : lemma.split(" ")) {
						s.add(lp);
					}
				} else
					s.add(token.getLemma());
				if (includeCoref) {
					for (Mention m : token.getMentions()) {
						for (Mention m2 : m.getEntity().getMentions()) {
							for (Token token1 : m2) {
								s.add(token1.getLemma());
							}
						}
					}
				}
			}

		}
		return s;
	}

	@Override
	public void readConfiguration(final SimilarityConfiguration tc) {
	}

	public boolean isIncludeCoref() {
		return includeCoref;
	}

	public void setIncludeCoref(boolean includeCoref) {
		this.includeCoref = includeCoref;
	}

}
