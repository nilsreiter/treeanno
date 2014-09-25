package de.uniheidelberg.cl.a10.graph;

import java.util.List;

import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.graph.Edge;
import de.uniheidelberg.cl.reiter.util.Counter;
import edu.uci.ics.jung.algorithms.importance.Ranking;
import edu.uci.ics.jung.graph.Graph;

public interface RankingAlgorithm {

	List<Ranking<?>> getRanking(Graph<Token, Edge> graph, Counter<Token> values);

}