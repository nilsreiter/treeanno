package de.nilsreiter.event.similarity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.mroth.measures.beans.NomBank;
import edu.mit.jwi.item.POS;
import edu.sussex.nlp.jws.JWS;

public class WordNet implements EventSimilarityFunction {
	private static final Logger logger = Logger.getLogger(WordNet.class
			.getName());

	public static final long serialVersionUID = 2l;
	JWS ws = null;
	NomBank nb = null;

	File wnPath = null;
	File nbPath = null;

	Semaphore sem1 = new Semaphore(1, true);
	// sample mean measured over 2 mio. predicate pairs
	public final static double mean = 0.238271558127602;

	int counter = 0;

	static final int reinitAfter = 100000;

	public WordNet(File wnPath, File nbPath) throws IOException {
		// JWS is the WordNet API that I use
		// IIRC, you can get it here:
		// http://www.sussex.ac.uk/Users/drh21/
		// RedirectIO.redirectOUT();

		// RedirectIO.resetOUT();

		this.wnPath = wnPath;
		this.nbPath = nbPath;

		this.init();
	}

	protected void init() throws IOException {
		if (ws != null) ws.getDictionary().close();
		ws = new JWS(wnPath.getAbsolutePath(), "3.0");
		nb = new NomBank(nbPath);
	}

	@Override
	public Probability sim(final Event arg0, final Event arg1) {

		Probability p = Probability.NULL;

		p = Probability.fromProbability(this.msim(arg0, arg1));

		return p;
	}

	public double msim(final Event n1, final Event n2) {
		if (++counter % reinitAfter == 0) {
			try {
				logger.info("Re-Initializing WordNet similarity");
				this.init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Token token1 = n1.firstToken();
		Token token2 = n2.firstToken();

		char pos1 = token1.getPartOfSpeech().charAt(0);
		char pos2 = token2.getPartOfSpeech().charAt(0);
		String p1 = null;
		String p2 = null;

		// return 1.0 if predicates are equal
		if (token1.getLemma().equals(token2.getLemma())) {
			return 1.0;
		}

		// if predicate is a noun, look up source verb in NomBank
		if (pos1 == 'V')
			p1 = token1.getLemma();
		else if (pos1 == 'N') {
			p1 = nb.getSourceVerb(token1.getLemma());
			if (p1 != null) p1 = p1.split("\\.")[0];
		}
		if (pos2 == 'V')
			p2 = token2.getLemma();
		else if (pos2 == 'N') {
			p2 = nb.getSourceVerb(token2.getLemma());
			if (p2 != null) p2 = p2.split("\\.")[0];
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

	@Override
	public void readConfiguration(Object tc) {
		// TODO Auto-generated method stub

	}
}
