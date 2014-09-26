package de.uniheidelberg.cl.a10.data2.alignment.graph;

import java.util.List;

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
public class AlignmentGraphFactory<T> {

	public AlignmentGraphFactory() {

	}

	public DirectedGraph<T, Edge<T>> getDirectedGraph(
			final Alignment<T> alignment, final List<T> tl1, final List<T> tl2) {
		DirectedSparseMultigraph<T, Edge<T>> graph =
				new DirectedSparseMultigraph<T, Edge<T>>();

		// adding vertices of first list
		graph.addVertex(tl1.get(0));
		for (int i = 1; i < tl1.size(); i++) {
			T t0 = tl1.get(i - 1);
			T t1 = tl1.get(i);
			graph.addVertex(tl1.get(i));
			graph.addEdge(new Edge<T>(t0, t1), t0, t1);
		}

		// adding vertices of second list
		graph.addVertex(tl2.get(0));
		for (int i = 1; i < tl2.size(); i++) {
			T t0 = tl2.get(i - 1);
			T t1 = tl2.get(i);
			graph.addVertex(tl2.get(i));
			graph.addEdge(new Edge<T>(t0, t1), t0, t1);
		}

		// adding edges for alignments
		for (Link<T> link : alignment.getAlignments()) {
			for (T token1 : link.getElements()) {
				for (T token2 : link.getElements()) {
					if (token1 != token2)
						graph.addEdge(new Edge<T>(token1, token2), token1,
								token2);
				}
			}
		}
		return graph;
	}

	public UndirectedGraph<T, Edge<T>> getUndirectedGraph(
			final Alignment<T> alignment, final List<T> tl1, final List<T> tl2) {
		UndirectedSparseGraph<T, Edge<T>> graph =
				new UndirectedSparseGraph<T, Edge<T>>();

		// adding vertices of first list
		graph.addVertex(tl1.get(0));
		for (int i = 1; i < tl1.size(); i++) {
			T t0 = tl1.get(i - 1);
			T t1 = tl1.get(i);
			graph.addVertex(t1);
			graph.addEdge(new Edge<T>(t0, t1), t0, t1);
		}

		// adding vertices of second list
		graph.addVertex(tl2.get(0));
		for (int i = 1; i < tl2.size(); i++) {
			T t0 = tl2.get(i - 1);
			T t1 = tl2.get(i);
			graph.addVertex(t1);
			graph.addEdge(new Edge<T>(t0, t1), t0, t1);
		}

		// adding edges for alignments
		for (Link<T> link : alignment.getAlignments()) {
			for (T token1 : link.getElements()) {
				for (T token2 : link.getElements()) {
					if (token1 != token2)
						graph.addEdge(new Edge<T>(token1, token2), token1,
								token2);
				}
			}
		}
		return graph;
	}

}
