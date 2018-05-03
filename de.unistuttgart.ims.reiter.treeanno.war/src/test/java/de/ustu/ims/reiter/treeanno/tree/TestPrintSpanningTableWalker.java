package de.ustu.ims.reiter.treeanno.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Comparator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestPrintSpanningTableWalker {
	Tree<Integer> tree;
	Comparator<Node<Integer>> intComparator = new Comparator<Node<Integer>>() {
		@Override
		public int compare(Node<Integer> o1, Node<Integer> o2) {
			return Integer.compare(o1.getObject(), o2.getObject());
		}
	};

	@Before
	public void setUp() {
		tree = new Tree<Integer>(intComparator);
		tree.setRoot(new Node<Integer>(0, intComparator));
		tree.addChild(0, 1);
		tree.addChild(0, 2);
		tree.addChild(0, 3);
		tree.addChild(1, 4);
	}

	@Test
	public void testSpanningTableWalker() {
		PrintSpanningTableWalker<Integer> pstw = new PrintSpanningTableWalker<Integer>(
				(Integer i) -> String.valueOf(i));
		tree.getRoot().depthFirstWalk(pstw);
		Map<Integer, Map<Integer, Integer>> m = pstw.getResult();
		assertNotNull(m);
		System.out.println(pstw.toString());
		assertEquals(1, m.size());
	}
}
