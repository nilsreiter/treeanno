package de.nilsreiter.event.similarity;

import java.io.File;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.mroth.measures.beans.NomBank;
import de.uniheidelberg.cl.mroth.measures.beans.SemLink;

public class VerbNet implements EventSimilarityFunction {
	public static final long serialVersionUID = 2l;

	SemLink sl;
	NomBank nb;

	// this is the sample mean measured over 2 mio. predicate pairs
	static final double mean = 0.129240981405388;

	public VerbNet(File nbPath, File slPath) {
		nb = new NomBank(nbPath);
		sl = new SemLink(new File(slPath, "vn-pb").getAbsolutePath());
	}

	@Override
	public Probability sim(final Event arg0, final Event arg1) {
		Probability p = Probability.NULL;
		if (arg0.equals(arg1))
			p = Probability.ONE;
		else
			p = Probability.fromProbability(this.msim(arg0, arg1));
		return p;
	}

	protected double msim(Event n1, Event n2) {
		Token target1 = n1.firstToken();
		Token target2 = n2.firstToken();
		char pos1 = target1.getPartOfSpeech().charAt(0);
		char pos2 = target2.getPartOfSpeech().charAt(0);
		String p1 = null;
		String p2 = null;

		// return 1.0 if both predicates are identical
		if (target1.getLemma().equals(target2.getLemma())) {
			return 1.0;
		}

		// retrieve source verb if predicates are nouns
		if (pos1 == 'V')
			p1 = target1.getLemma();
		else if (pos1 == 'N') {
			p1 = nb.getSourceVerb(target1.getLemma());
		}
		if (pos2 == 'V')
			p2 = target2.getLemma();
		else if (pos2 == 'N') {
			p2 = nb.getSourceVerb(target2.getLemma());
		}

		// return mean similarity for all predicate pairs
		// for which the similarity cannot be calculated
		if (p1 == null || p2 == null) {
			return mean;
		}

		// look up potential VerbNet classes of both predicates
		String[] vnclasses1 = sl.getVerbNetClasses(p1 + ".01");
		String[] vnclasses2 = sl.getVerbNetClasses(p2 + ".01");

		// see above
		if (vnclasses1.length == 0 || vnclasses2.length == 0) {
			return mean;
		}

		// look-up class/sub-class overlap in VerbNet
		// three possibilities:
		// 1.0 -- there exists a class that contains both verbs
		// 0.8 -- one verb is in a sub-class of a class of the other verb
		// 0.0 -- verbs are only in disjunct classes
		double sim = 0.0;
		for (int i = 0; i < vnclasses1.length; i++) {
			for (int j = 0; j < vnclasses2.length; j++) {
				if (vnclasses1[i].equals(vnclasses2[j])) {

					return 1.0;
				}
				if (vnclasses1[i].substring(0, 2).equals(
						vnclasses2[j].substring(0, 2))) sim = 0.8;
			}
		}

		return sim;
	}

	@Override
	public void readConfiguration(Object tc) {
		// TODO Auto-generated method stub

	}
}
