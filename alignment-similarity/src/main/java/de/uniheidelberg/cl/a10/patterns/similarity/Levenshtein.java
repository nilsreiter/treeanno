package de.uniheidelberg.cl.a10.patterns.similarity;

import de.uniheidelberg.cl.a10.patterns.data.Probability;

public class Levenshtein extends AbstractSimilarityFunction<String> {

	public static Probability distance(final String s, final String t) {
		return new Levenshtein().sim(s, t);
	}

	@Override
	public Probability sim(final String s, final String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}
		int n = s.length(); // length of s
		int m = t.length(); // length of t

		if (n == 0) {
			return Probability.fromProbability(m);
		} else if (m == 0) {
			return Probability.fromProbability(n);
		}

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char t_j; // jth character of t

		int cost; // cost

		for (i = 0; i <= n; i++) {
			p[i] = i;
		}

		for (j = 1; j <= m; j++) {
			t_j = t.charAt(j - 1);
			d[0] = j;

			for (i = 1; i <= n; i++) {
				cost = s.charAt(i - 1) == t_j ? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally left
				// and up +cost
				d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
						+ cost);
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		}

		// our last action in the above loop was to switch d and p, so p now
		// actually has the most recent cost counts
		return Probability.fromProbability(1.0 - ((double) p[n] / (double) Math
				.max(m, n)));
	}

	public static void main(final String[] args) {
		Levenshtein l = new Levenshtein();
		System.out.println(l.sim("abcde", "abcfg"));
	}
}
