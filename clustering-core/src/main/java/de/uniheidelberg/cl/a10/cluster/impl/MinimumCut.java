package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.math3.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.UndirectedWeightedSubgraph;

import de.uniheidelberg.cl.a10.cluster.ICluster;
import de.uniheidelberg.cl.a10.cluster.IExitCondition;
import de.uniheidelberg.cl.a10.cluster.IFullPartition;
import de.uniheidelberg.cl.a10.cluster.IPartition;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

@Deprecated
public class MinimumCut<D> extends AbstractClusteringWithExitCondition<D> {
	List<Partition> history = null;

	@Override
	public List<? extends IFullPartition<D>> internalCluster(
			final Collection<D> documents) {
		WeightedGraph<D, DefaultWeightedEdge> graph = new SimpleWeightedGraph<D, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);

		// 1. Creating the graph
		for (D doc1 : documents) {
			graph.addVertex(doc1);
			for (D doc2 : documents) {
				graph.addVertex(doc2);
				Probability p = getDocumentSimilarityFunction().getSimilarity(
						doc1, doc2);
				if (doc1 != doc2 && p != null
						&& p.compareTo(Probability.NULL) > 0) {
					DefaultWeightedEdge edge = new DefaultWeightedEdge();
					graph.addEdge(doc1, doc2, edge);
					graph.setEdgeWeight(edge, getDocumentSimilarityFunction()
							.getSimilarity(doc1, doc2).getProbability());
				}
			}
		}

		// 2. Applying minimum cut recursively
		// int n = 0;
		Queue<WeightedGraph<D, DefaultWeightedEdge>> subgraphs = new ArrayDeque<WeightedGraph<D, DefaultWeightedEdge>>();
		subgraphs.add(graph);
		history = new ArrayList<Partition>();
		history.add(new Partition(subgraphs));

		while (!exitCondition.matches(history)
				&& !new ECMinimalClusters().matches(history)) {
			WeightedGraph<D, DefaultWeightedEdge> g = subgraphs.poll();
			if (g.vertexSet().size() > 1) {

				Pair<WeightedGraph<D, DefaultWeightedEdge>, WeightedGraph<D, DefaultWeightedEdge>> p = this
						.oneCut(g);
				if (p.getKey().vertexSet().size() > 0)
					subgraphs.offer(p.getKey());
				if (p.getValue().vertexSet().size() > 0)
					subgraphs.offer(p.getValue());
			} else {
				subgraphs.offer(g);
			}
			// n++;
			history.add(0, new Partition(subgraphs));
		}

		// 3. Returning a new partition
		return history;
	}

	/**
	 * This method makes one minimal cut and returns the pair of subgraphs
	 * 
	 * @param graph
	 *            The graph to be cut
	 * @return A pair of subgraphs, the two sides of the cut
	 */
	@Deprecated
	protected Pair<WeightedGraph<D, DefaultWeightedEdge>, WeightedGraph<D, DefaultWeightedEdge>> oneCut(
			final WeightedGraph<D, DefaultWeightedEdge> graph) {
		Set<D> allVertices = new HashSet<D>(graph.vertexSet());
		StoerWagnerMinimumCut<D, DefaultWeightedEdge> mincut = null;// new
																	// StoerWagnerMinimumCut<D,
																	// DefaultWeightedEdge>(
		// graph);
		UndirectedWeightedSubgraph<D, DefaultWeightedEdge> side1 = new UndirectedWeightedSubgraph<D, DefaultWeightedEdge>(
				graph, mincut.minCut(), null);
		allVertices.removeAll(side1.vertexSet());
		UndirectedWeightedSubgraph<D, DefaultWeightedEdge> side2 = new UndirectedWeightedSubgraph<D, DefaultWeightedEdge>(
				graph, allVertices, null);
		return new Pair<WeightedGraph<D, DefaultWeightedEdge>, WeightedGraph<D, DefaultWeightedEdge>>(
				side1, side2);

	}

	class Partition implements IFullPartition<D> {
		LinkedList<ICluster<D>> set = new LinkedList<ICluster<D>>();

		public Partition(final Collection<? extends Graph<D, ?>> iset) {
			for (Graph<D, ?> g : iset) {
				set.add(new Cluster_impl<D>(g.vertexSet()));
			}
		}

		@Override
		public ICluster<D> getCluster(final D object) {
			for (ICluster<D> cl : set) {
				if (cl.contains(object))
					return cl;
			}
			return null;
		}

		@Override
		public Collection<ICluster<D>> getClusters() {
			return set;
		}

		@Override
		public Collection<D> getObjects() {
			Set<D> coll = new HashSet<D>();
			for (ICluster<D> cl : set) {
				coll.addAll(cl);
			}
			return coll;
		}

		@Override
		public int size() {
			return set.size();
		}

		@Override
		public boolean together(final D o1, final D o2) {
			return this.getCluster(o1) == this.getCluster(o2);
		}

		@Override
		public String toString() {
			return this.set.toString();
		}
	}

	class ECMinimalClusters implements IExitCondition<D> {

		public boolean imatches(final IPartition<D> cluster) {
			for (ICluster<?> cl : cluster.getClusters()) {
				if (cl.size() > 1)
					return false;
			}
			return true;
		}

		@Override
		public boolean matches(final List<? extends IPartition<D>> history) {
			return imatches(history.get(0));
		}

	}

	@Override
	public List<? extends IPartition<D>> getPartitionHistory() {
		return history;
	}
}
