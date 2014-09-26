package de.nilsreiter.alignment.rwalk;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.graph.Edge;
import de.uniheidelberg.cl.reiter.util.Counter;
import edu.uci.ics.jung.algorithms.importance.Ranking;
import edu.uci.ics.jung.graph.Graph;

public class NRWalk<T extends HasDocument> {
	public NRWalk() {
		super();
	}

	public NRWalk(final int k, final int n) {
		super();
		this.k = k;
		this.n = n;
	}

	int maxX = 10;
	int maxY = 1;
	boolean mirror = false;

	boolean cycles = true;

	int connectTop = 5;

	int k = 10;
	int n = 100;
	Random random = new Random(System.currentTimeMillis());

	String[] lineStyles = new String[] { "dashed", "solid" };

	double averageScore = Double.NaN;

	public Counter<T> doWalk(final Graph<T, Edge<T>> graph) {
		Counter<T> values = new Counter<T>();

		double c = 0.0;

		// making random walks
		for (int i = 0; i < n; i++) {
			for (T token : graph.getVertices()) {
				int v = step(graph, token, k, new HashSet<T>());
				values.add(token, v);
				c += v;
				// System.err.println();
			}
		}
		this.averageScore = c / (n * graph.getVertexCount());
		return values;
	}

	public boolean isInTopNodes(final Token token,
			final SortedSet<Ranking<?>> ranking) {
		Iterator<Ranking<?>> iter = ranking.iterator();
		int i = 0;
		while (iter.hasNext() && i < connectTop) {
			Ranking<?> r = iter.next();
			Token rToken = (Token) r.getRanked();
			if (token.equals(rToken)) return true;
			if (rToken.getRitualDocument().equals(token.getRitualDocument())) {
				i++;
			}
		}
		return false;
	}

	public int getNumericId(final Document rd) {
		int numericId;
		if (rd.getId().contains(".txt"))
			numericId = Integer.parseInt(rd.getId().replaceAll(".txt", ""));
		else
			numericId = Integer.parseInt(rd.getId().substring(1));
		return numericId;
	}

	private int step(final Graph<T, Edge<T>> graph, final T token, final int k,
			final Set<T> visited) {
		List<T> neighbors = new LinkedList<T>(graph.getSuccessors(token));

		if (!cycles) neighbors.removeAll(visited);
		if (neighbors.size() < 1) return 0;
		T nextToken = neighbors.get(random.nextInt(neighbors.size()));

		// System.err.print(nextToken);
		int v =
				(token.getRitualDocument()
						.equals(nextToken.getRitualDocument()) ? 0 : 1);
		visited.add(token);
		if (k > 0)
			return step(graph, nextToken, k - 1, visited) + v;
		else
			return v;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(final int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(final int maxY) {
		this.maxY = maxY;
	}

	public boolean isCycles() {
		return cycles;
	}

	public void setCycles(final boolean cycles) {
		this.cycles = cycles;
	}

	public double getAverageScore() {
		return averageScore;
	}

	/**
	 * This method does mathematical scaling of values, as described <a href =
	 * "http://stackoverflow.com/questions/5294955/how-to-scale-down-a-range-of
	 * -numbers-with-a-known-min-and-max-value">here</a>.
	 * 
	 * @param min
	 *            The minimal value of x
	 * @param max
	 *            The maximal value of x
	 * @param a
	 *            The lower bound of the target range
	 * @param b
	 *            The upper bound of the target range
	 * @param x
	 *            The actual value x
	 * @return A scaled version of x
	 */
	public static double scale(final double min, final double max,
			final double a, final double b, final double x) {
		double r = ((b - a) * (x - min) / (max - min)) + a;
		return r;
	}
}
