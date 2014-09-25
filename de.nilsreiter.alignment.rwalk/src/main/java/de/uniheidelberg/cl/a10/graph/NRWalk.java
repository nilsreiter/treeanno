package de.uniheidelberg.cl.a10.graph;

import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.graph.Edge;
import de.uniheidelberg.cl.reiter.util.Counter;
import edu.uci.ics.jung.algorithms.importance.Ranking;
import edu.uci.ics.jung.graph.Graph;

public class NRWalk implements RankingAlgorithm {
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

	public Counter<Event> doWalk(final Graph<Event, Edge> graph) {
		Counter<Event> values = new Counter<Event>();

		double c = 0.0;

		// making random walks
		for (int i = 0; i < n; i++) {
			for (Event token : graph.getVertices()) {
				int v = step(graph, token, k, new HashSet<Event>());
				values.add(token, v);
				c += v;
				// System.err.println();
			}
		}
		this.averageScore = c / (n * graph.getVertexCount());
		return values;
	}

	@Override
	public List<Ranking<?>> getRanking(final Graph<Token, Edge> graph,
			final Counter<Token> values) {
		List<Ranking<?>> ret = new LinkedList<Ranking<?>>();
		for (Token token : graph.getVertices()) {
			Ranking<Token> ra = new Ranking<Token>(0, values.get(token), token);
			ret.add(ra);
		}
		return ret;
	}

	public SortedSet<Ranking<?>> getSortedSetRanking(
			final Graph<Token, Edge> graph, final Counter<Token> values) {
		return new TreeSet<Ranking<?>>(this.getRanking(graph, values));
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

	public String generateTikzCode(final Collection<Document> documents,
			final Graph<Token, Edge> graph, final Counter<Token> values,
			final Alignment<Token> alignment) {

		StringBuilder b = new StringBuilder();
		List<Document> docs = new LinkedList<Document>(documents);

		for (int doc = 0; doc < docs.size(); doc++) {
			Document rd = docs.get(doc);
			List<Token> tokens = new LinkedList<Token>();
			Map<Token, Double> yValues = new HashMap<Token, Double>();
			Map<Token, String> topCoords = new HashMap<Token, String>();

			for (Token token : rd.getTokens()) {
				if (graph.containsVertex(token)) {
					double scaledY =
							scale(0, values.getHighestCount(), 0, maxY,
									values.get(token));
					tokens.add(token);
					yValues.put(token, scaledY);
				}
			}
			boolean first = true;
			int i = 0;
			int orientationFactor = 1;
			if (mirror) orientationFactor = (doc == 0 ? 1 : -1);
			// Formatter formatter = new Formatter(b, Locale.US);
			b.append("\\draw ");
			if (!mirror) b.append("[").append(lineStyles[doc]).append("]");

			for (Token token : tokens) {
				double scaledX = scale(0, tokens.size(), 0, maxX, i++);
				if (first) {
					first = false;
					b.append("(");
				} else {
					b.append(" -- (");
				}
				StringBuilder coordB = new StringBuilder();
				Formatter formatter = new Formatter(coordB);
				formatter.format("%1$3.2f, %2$3.3f", scaledX, orientationFactor
						* yValues.get(token));
				formatter.close();
				String coord = coordB.toString();
				if (this.isInTopNodes(token,
						this.getSortedSetRanking(graph, values)))
					topCoords.put(token, "(" + coord + ")");
				b.append(coord);
				b.append(')');
			}

			b.append(";\n");

			for (Token token : topCoords.keySet()) {
				b.append("\\node (");
				b.append(token.getGlobalId().replaceAll("-", ""));
				b.append(") at ");
				b.append(topCoords.get(token));
				b.append(" [right,rotate=90] {\\tiny ");
				b.append(token.getId());
				b.append("};\n");
			}

			b.append("\\draw ");
			b.append("[").append(lineStyles[doc]).append("] ");
			b.append(" (0.5,").append(orientationFactor * 0.5 + doc)
					.append(")");
			b.append(" to node ");
			b.append(" [above] {\\footnotesize \\rd{");
			b.append(this.getNumericId(rd));
			b.append("}} node {} ");
			b.append(" (1.5,").append(orientationFactor * 0.5 + doc)
					.append(")");
			b.append(";\n");

		}
		b.append("\\draw [->] (-0.2,0.0) -- (" + maxX + ",0);\n");
		b.append("\\draw [->] (0,-0.2) -- (0," + maxY + ");\n");
		return b.toString();
	}

	public int getNumericId(final Document rd) {
		int numericId;
		if (rd.getId().contains(".txt"))
			numericId = Integer.parseInt(rd.getId().replaceAll(".txt", ""));
		else
			numericId = Integer.parseInt(rd.getId().substring(1));
		return numericId;
	}

	private int step(final Graph<Event, Edge> graph, final Event token,
			final int k, final Set<Event> visited) {
		List<Event> neighbors =
				new LinkedList<Event>(graph.getSuccessors(token));

		if (!cycles) neighbors.removeAll(visited);
		if (neighbors.size() < 1) return 0;
		Event nextToken = neighbors.get(random.nextInt(neighbors.size()));

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
