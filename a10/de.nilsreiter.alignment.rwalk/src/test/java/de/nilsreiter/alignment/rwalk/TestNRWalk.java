package de.nilsreiter.alignment.rwalk;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasDocument;
import de.uniheidelberg.cl.a10.data2.alignment.graph.Edge;
import de.uniheidelberg.cl.reiter.util.Counter;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class TestNRWalk {
	UndirectedGraph<HasDocument, Edge<HasDocument>> graph;

	HasDocument[][] objects = new HasDocument[2][];
	Document[] documents = new Document[2];

	NRWalk<HasDocument> nrw;

	@Before
	public void setUp() {
		graph = new UndirectedSparseGraph<HasDocument, Edge<HasDocument>>();

		documents[0] = mock(Document.class);
		documents[1] = mock(Document.class);

		int m = 10, n = 10;
		objects[0] = new HasDocument[n];
		for (int i = 0; i < n; i++) {
			objects[0][i] = mock(HasDocument.class);
			when(objects[0][i].getRitualDocument()).thenReturn(documents[0]);

			graph.addVertex(objects[0][i]);
			if (i > 0) {
				graph.addEdge(new Edge<HasDocument>(objects[0][i - 1],
						objects[0][i]), objects[0][i - 1], objects[0][i]);

			}
		}
		objects[1] = new HasDocument[m];
		for (int i = 0; i < m; i++) {
			objects[1][i] = mock(HasDocument.class);
			when(objects[1][i].getRitualDocument()).thenReturn(documents[1]);

			graph.addVertex(objects[1][i]);
			if (i > 0) {
				graph.addEdge(new Edge<HasDocument>(objects[1][i - 1],
						objects[1][i]), objects[1][i - 1], objects[1][i]);

			}

		}

	}

	@Test
	public void testNRWalkNoConnection() {
		nrw = new NRWalk<HasDocument>();
		Counter<HasDocument> counter = nrw.doWalk(graph);
		for (int i = 0; i < objects.length; i++) {
			for (int j = 0; j < objects[i].length; j++) {
				assertEquals(0, counter.get(objects[i][j]).intValue());
			}
		}
	}

	@Test
	public void testNRWalkHarmonicConnection() {
		nrw = new NRWalk<HasDocument>(10, 1000);

		for (int i = 0; i < objects[0].length; i++) {
			graph.addEdge(new Edge<HasDocument>(objects[0][i], objects[1][i]),
					objects[0][i], objects[1][i]);
		}

		Counter<HasDocument> counter = nrw.doWalk(graph);

		assertEquals(0.43, counter.get(objects[0][0]), 0.2);
	}
}
