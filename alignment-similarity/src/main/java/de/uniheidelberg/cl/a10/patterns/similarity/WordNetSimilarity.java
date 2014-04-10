package de.uniheidelberg.cl.a10.patterns.similarity;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import de.uniheidelberg.cl.a10.RedirectIO;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.mroth.measures.beans.NomBank;
import edu.mit.jwi.item.POS;
import edu.sussex.nlp.jws.JWS;

public class WordNetSimilarity extends AbstractSimilarityFunction<Event>
		implements SimilarityFunction<Event> {
	public static final long serialVersionUID = 2l;
	static JWS ws = null;
	static NomBank nb = null;

	Semaphore sem1 = new Semaphore(1, true);
	// sample mean measured over 2 mio. predicate pairs
	public final static double mean = 0.238271558127602;

	public WordNetSimilarity() throws IOException {
		// JWS is the WordNet API that I use
		// IIRC, you can get it here:
		// http://www.sussex.ac.uk/Users/drh21/
		RedirectIO.redirectOUT();
		if (ws == null)
			ws = new JWS(System.getProperty("nr.JWS.WORDNET"), "3.0");
		if (nb == null)
			nb = new NomBank(System.getProperty("nr.NOMBANK"));
		RedirectIO.resetOUT();

	}

	@Override
	public Probability sim(final Event arg0, final Event arg1) {
		if (positivePreCheck(arg0, arg1))
			return Probability.ONE;
		if (negativePreCheck(arg0, arg1))
			return Probability.fromProbability(mean);
		Probability p = this.getFromHistory(arg0, arg1);
		if (p != null)
			return p;

		p = Probability.fromProbability(this.msim(arg0, arg1));

		this.putInHistory(arg0, arg1, p);
		return p;
	}

	public double msim(final Event n1, final Event n2) {
		char pos1 = n1.getTarget().getPartOfSpeech().charAt(0);
		char pos2 = n2.getTarget().getPartOfSpeech().charAt(0);
		String p1 = null;
		String p2 = null;

		// return 1.0 if predicates are equal
		if (n1.getTarget().getLemma().equals(n2.getTarget().getLemma())) {
			return 1.0;
		}

		// if predicate is a noun, look up source verb in NomBank
		if (pos1 == 'V')
			p1 = n1.getTarget().getLemma();
		else if (pos1 == 'N') {
			p1 = nb.getSourceVerb(n1.getTarget().getLemma());
			if (p1 != null)
				p1 = p1.split("\\.")[0];
		}
		if (pos2 == 'V')
			p2 = n2.getTarget().getLemma();
		else if (pos2 == 'N') {
			p2 = nb.getSourceVerb(n2.getTarget().getLemma());
			if (p2 != null)
				p2 = p2.split("\\.")[0];
		}
		double r = mean;

		try {
			// System.err.println(sem1.getQueueLength());
			sem1.acquire();

			// if both verbs cannot be found in WordNet, return mean similarity
			if (p1 == null || p2 == null
					|| ws.getDictionary().getIndexWord(p1, POS.VERB) == null
					|| ws.getDictionary().getIndexWord(p2, POS.VERB) == null) {

				return mean;
			}

			// otherwise return the best Lin score that can be measures
			// between all pairs of candidate synsets

			r = ws.getLin().max(p1, p2, "v");
		} catch (InterruptedException e) {
			// this should not happen
			e.printStackTrace();
		} finally {
			sem1.release();
		}

		return r;
	}
}
