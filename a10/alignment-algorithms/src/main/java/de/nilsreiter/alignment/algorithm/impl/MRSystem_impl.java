package de.nilsreiter.alignment.algorithm.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.jgrapht.DirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.MinSourceSinkCut;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedSubgraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import de.nilsreiter.alignment.algorithm.MRSystem;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class MRSystem_impl<T extends HasDocument> implements MRSystem<T> {

	List<T> sequence1;
	List<T> sequence2;

	SimilarityFunction<T> similarityFunction;

	@Deprecated
	Probability threshold = Probability.NULL;

	Logger logger = Logger.getLogger(getClass().getName());

	MRSystemConfiguration config;
	String alignmentId;

	@Override
	public Alignment<T> getAlignment() {
		DirectedGraph<T, DefaultWeightedEdge> graph = this.getGraph();
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		Set<Set<T>> set = this.recurse(graph, 0);
		Alignment<T> al = new Alignment_impl<T>(alignmentId);

		for (Set<T> s : set) {
			logger.finest(s.toString());
			if (s.size() == 2) {
				Iterator<T> iter = s.iterator();
				T e1 = iter.next();
				T e2 = iter.next();
				double score;
				try {
					score =
							this.similarityFunction.sim(e1, e2)
							.getProbability();
				} catch (NullPointerException e) {
					score =
							this.similarityFunction.sim(e2, e1)
							.getProbability();
				}
				if (!al.together(e1, e2)) {
					Link<T> link = al.addAlignment(idp.getNextAlignmentId(), s);
					link.setScore(score);
				}

			} else {
				if (!al.contains(s.iterator().next())) {
					Link<T> link = al.addAlignment(idp.getNextAlignmentId(), s);
					link.setScore(1.0);
				}
			}
		}
		return al;
	}

	@SuppressWarnings("unchecked")
	protected Set<Set<T>> recurse(
			final DirectedGraph<T, DefaultWeightedEdge> graph, final int level) {
		Set<Set<T>> ret = new HashSet<Set<T>>();

		if (graph.vertexSet().size() <= 2) {
			ret.add(new HashSet<T>(graph.vertexSet()));
			return ret;
		}
		if (graph.edgeSet().isEmpty()) {
			for (T vertex : graph.vertexSet()) {
				ret.add(new HashSet<T>(Arrays.asList(vertex)));
			}
			return ret;
		}
		MinSourceSinkCut<T, DefaultWeightedEdge> mc =
				new MinSourceSinkCut<T, DefaultWeightedEdge>(graph);
		double w = Double.MAX_VALUE;
		DefaultWeightedEdge minEdge = null;
		for (DefaultWeightedEdge e : graph.edgeSet()) {
			double w0 = graph.getEdgeWeight(e);
			if (w0 < w) {
				w = w0;
				minEdge = e;
			}
		}
		if (minEdge == null) {
			logger.severe("No minimal weighted edge found. Should not be happening. The graph is "
					+ graph);
		}
		mc.computeMinCut(graph.getEdgeSource(minEdge),
				graph.getEdgeTarget(minEdge));

		Set<T> sinkPartition = mc.getSinkPartition();
		Set<T> sourcePartition = mc.getSourcePartition();
		logger.finest("Cut: " + sinkPartition.size() + " / "
				+ sourcePartition.size());

		for (Set<T> component : this.getComponents(graph, sinkPartition)) {
			DirectedGraph<T, DefaultWeightedEdge> subGraph =
					this.createSubGraph(graph, component, level + 1);
			ret.addAll(this.recurse(subGraph, level + 1));
		}

		for (Set<T> component : this.getComponents(graph, sourcePartition)) {
			DirectedGraph<T, DefaultWeightedEdge> subGraph =
					this.createSubGraph(graph, component, level + 1);
			ret.addAll(this.recurse(subGraph, level + 1));
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	protected List<Set<T>>
	getComponents(final DirectedGraph<T, DefaultWeightedEdge> graph,
			final Set<T> part) {
		DirectedWeightedSubgraph<T, DefaultWeightedEdge> subgraph =
				new DirectedWeightedSubgraph<T, DefaultWeightedEdge>(
						(WeightedGraph<T, DefaultWeightedEdge>) graph, part,
						null);
		StrongConnectivityInspector<T, DefaultWeightedEdge> source_ci =
				new StrongConnectivityInspector<T, DefaultWeightedEdge>(
						subgraph);
		return source_ci.stronglyConnectedSets();
	}

	protected DirectedGraph<T, DefaultWeightedEdge> createSubGraph(
			final DirectedGraph<T, DefaultWeightedEdge> baseGraph,
			final Set<T> vertices, final int level) {
		@SuppressWarnings("unchecked")
		DirectedWeightedSubgraph<T, DefaultWeightedEdge> subgraph =
		new DirectedWeightedSubgraph<T, DefaultWeightedEdge>(
				(WeightedGraph<T, DefaultWeightedEdge>) baseGraph,
				vertices, null);

		return subgraph;

		/*
		 * SimpleWeightedGraph<T, DefaultWeightedEdge> subGraph = new
		 * SimpleWeightedGraph<T, DefaultWeightedEdge>(
		 * DefaultWeightedEdge.class); for (T v : vertices) { if
		 * (!subGraph.containsVertex(v)) subGraph.addVertex(v); for (T v2 :
		 * vertices) { if (!subGraph.containsVertex(v2)) subGraph.addVertex(v2);
		 * if (!subGraph.containsEdge(v, v2) && !subGraph.containsEdge(v2, v)) {
		 * Iterator<DefaultWeightedEdge> baseEdgeIterator = baseGraph
		 * .getAllEdges(v, v2).iterator(); if (baseEdgeIterator.hasNext()) {
		 * DefaultWeightedEdge baseEdge = baseEdgeIterator.next();
		 * subGraph.setEdgeWeight(subGraph.addEdge(v, v2),
		 * baseGraph.getEdgeWeight(baseEdge)); } } } } return subGraph;
		 */
	}

	public DirectedGraph<T, DefaultWeightedEdge> getGraph() {
		SimpleDirectedWeightedGraph<T, DefaultWeightedEdge> graph =
				new SimpleDirectedWeightedGraph<T, DefaultWeightedEdge>(
						DefaultWeightedEdge.class);
		for (T e1 : this.getSequence1()) {
			graph.addVertex(e1);

		}
		for (T e2 : this.getSequence2()) {
			graph.addVertex(e2);

		}
		for (T e1 : this.getSequence1()) {
			for (T e2 : this.getSequence2()) {
				if (!graph.containsEdge(e1, e2)) {
					Probability w = this.getSimilarityFunction().sim(e1, e2);
					if (w.getProbability() >= threshold.getProbability()) {
						logger.finer("Creating edge between " + e1 + " and "
								+ e2 + ": " + w.getProbability());
						if (e1.equals(e2)) {
							System.err.println("1");
						}

						DefaultWeightedEdge dwe1 = graph.addEdge(e1, e2);
						graph.setEdgeWeight(dwe1, w.getProbability());
						DefaultWeightedEdge dwe2 = graph.addEdge(e2, e1);
						graph.setEdgeWeight(dwe2, w.getProbability());
					} else {
						logger.finest("Edge below threshold: " + e1 + " and "
								+ e2 + ": " + w.getProbability());
					}
				}
			}
		}
		return graph;
	}

	@Override
	public SimilarityFunction<T> getSimilarityFunction() {
		return similarityFunction;
	}

	public void setSimilarityFunction(
			final SimilarityFunction<T> similarityFunction) {
		this.similarityFunction = similarityFunction;
	}

	@Override
	public Probability getThreshold() {
		return Probability.fromProbability(config.getThreshold());
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public void setLogger(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public synchronized Alignment<T> align(String id, List<T> list1,
			List<T> list2) {
		sequence1 = list1;
		sequence2 = list2;
		alignmentId = id;
		return this.getAlignment();
	}

	public List<T> getSequence1() {
		return sequence1;
	}

	public List<T> getSequence2() {
		return sequence2;
	}

	@Override
	public Class<?> getConfigurationBean() {
		return MRSystemConfiguration.class;
	}

	public MRSystemConfiguration getConfig() {
		return config;
	}

	public void setConfig(MRSystemConfiguration config) {
		this.config = config;
	}

}