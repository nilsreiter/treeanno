package de.uniheidelberg.cl.a10.data;

import java.util.List;

import org.jgrapht.graph.DirectedMultigraph;

import de.uniheidelberg.cl.a10.data.BaseSentence.DependencyEdge;
import de.uniheidelberg.cl.a10.data.Comparator.BaseTokenPair;
import de.uniheidelberg.cl.reiter.util.Pair;

public class SentencePair extends Pair<BaseSentence, BaseSentence> {

	public SentencePair(final BaseSentence e1, final BaseSentence e2) {
		super(e1, e2);
	}

	public List<BaseTokenPair> getDifferences() {
		return new Comparator(this.getElement1().getDependencyStyle())
				.compareSentences(getElement1(), getElement2());
	}

	public DirectedMultigraph<BaseToken, DependencyEdge> getGraphDiff() {
		DirectedMultigraph<BaseToken, DependencyEdge> graph = this
				.getElement1().getDependencyGraph();

		for (int i = 0; i < this.getElement2().size(); i++) {
			BaseToken tok2 = this.getElement2().get(i);
			BaseToken tok1 = this.getElement1().get(i);
			BaseToken gov1 = tok1.getGovernor();
			if (!graph.containsEdge(tok1, gov1)) {
				// graph.addVertex(tok1);
				if (gov1 != null) {
					// graph.addVertex(gov1);
					graph.addEdge(tok1, gov1,
							new DependencyEdge(tok2.getDependencyRelation(),
									tok2.getDependencyRelation().toString()
											+ "*"));
				}
			} else {
				DependencyEdge edge = graph.getEdge(tok1, gov1);
				if (edge.getDependency() != tok2.getDependencyRelation()) {
					graph.addEdge(tok1, gov1,
							new DependencyEdge(tok2.getDependencyRelation(),
									tok2.getDependencyRelation().toString()
											+ "*"));
				}
			}
		}

		return graph;
	}
}
