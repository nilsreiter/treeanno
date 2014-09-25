package de.uniheidelberg.cl.a10.data2.alignment.graph;

import java.util.List;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * This factory creates a directed graph from an alignment and two lists of
 * tokens.
 * 
 * @author reiter
 * 
 */
public class AlignmentGraphFactory {

	static AlignmentGraphFactory agf = null;

	private AlignmentGraphFactory() {

	}

	public static AlignmentGraphFactory getInstance() {
		if (agf == null) agf = new AlignmentGraphFactory();
		return agf;
	}

	public DirectedGraph<Event, Edge> getDirectedGraph(
			final Alignment<Event> alignment, final List<Event> tl1,
			final List<Event> tl2) {
		DirectedSparseMultigraph<Event, Edge> graph =
				new DirectedSparseMultigraph<Event, Edge>();

		// adding vertices of first list
		graph.addVertex(tl1.get(0));
		for (int i = 1; i < tl1.size(); i++) {
			Event t0 = tl1.get(i - 1);
			Event t1 = tl1.get(i);
			graph.addVertex(tl1.get(i));
			graph.addEdge(new Edge(t0, t1), t0, t1);
		}

		// adding vertices of second list
		graph.addVertex(tl2.get(0));
		for (int i = 1; i < tl2.size(); i++) {
			Event t0 = tl2.get(i - 1);
			Event t1 = tl2.get(i);
			graph.addVertex(tl2.get(i));
			graph.addEdge(new Edge(t0, t1), t0, t1);
		}

		// adding edges for alignments
		for (Link<Event> link : alignment.getAlignments()) {
			for (Event token1 : link.getElements()) {
				for (Event token2 : link.getElements()) {
					if (token1 != token2)
						graph.addEdge(new Edge(token1, token2), token1, token2);
				}
			}
		}
		return graph;
	}

	public UndirectedGraph<Event, Edge> getUndirectedGraph(
			final Alignment<Event> alignment, final List<Event> tl1,
			final List<Event> tl2) {
		UndirectedSparseGraph<Event, Edge> graph =
				new UndirectedSparseGraph<Event, Edge>();

		// adding vertices of first list
		graph.addVertex(tl1.get(0));
		for (int i = 1; i < tl1.size(); i++) {
			Event t0 = tl1.get(i - 1);
			Event t1 = tl1.get(i);
			graph.addVertex(t1);
			graph.addEdge(new Edge(t0, t1), t0, t1);
		}

		// adding vertices of second list
		graph.addVertex(tl2.get(0));
		for (int i = 1; i < tl2.size(); i++) {
			Event t0 = tl2.get(i - 1);
			Event t1 = tl2.get(i);
			graph.addVertex(t1);
			graph.addEdge(new Edge(t0, t1), t0, t1);
		}

		// adding edges for alignments
		for (Link<Event> link : alignment.getAlignments()) {
			for (Event token1 : link.getElements()) {
				for (Event token2 : link.getElements()) {
					if (token1 != token2)
						graph.addEdge(new Edge(token1, token2), token1, token2);
				}
			}
		}
		return graph;
	}

}
