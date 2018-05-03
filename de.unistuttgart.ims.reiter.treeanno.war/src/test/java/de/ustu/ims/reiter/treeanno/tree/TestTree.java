package de.ustu.ims.reiter.treeanno.tree;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

public class TestTree {

	final Object[] objects = new Object[] { new Object(), new Object(), new Object(), new Object(), new Object(),
			new Object(), new Object(), new Object(), new Object() };
	Comparator<Node<Object>> comp;

	@Before
	public void setUp() {
		comp = new Comparator<Node<Object>>() {
			@Override
			public int compare(Node<Object> o1, Node<Object> o2) {
				return Integer.compare(ArrayUtils.indexOf(objects, o1.getObject()),
						ArrayUtils.indexOf(objects, o2.getObject()));
			}

		};
	}

	@Test
	public void testTreeCreation() {
		Walker<Object, String> w = new PrintParenthesesWalker<Object>();

		Tree<Object> tree = new Tree<Object>(comp);
		tree.setRoot(new Node<Object>(objects[0], comp));
		tree.depthFirstWalk(w);
		assertEquals("()", w.toString());

		tree.addChild(objects[0], objects[1]);
		tree.depthFirstWalk(w);
		assertEquals("(())", w.toString());

		tree.addChild(objects[0], objects[2]);
		tree.depthFirstWalk(w);
		assertEquals("(()())", w.toString());

		tree.addChild(objects[0], objects[4]);
		tree.depthFirstWalk(w);
		assertEquals("(()()())", w.toString());

		tree.addChild(objects[1], objects[3]);
		tree.depthFirstWalk(w);
		assertEquals("((())()())", w.toString());

		tree.addChild(objects[1], objects[5]);
		tree.depthFirstWalk(w);
		assertEquals("((()())()())", w.toString());
	}
}
