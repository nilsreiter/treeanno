package de.ustu.ims.reiter.treeanno.tree;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestTree {

	Object[] objects;

	@Before
	public void setUp() {
		objects =
				new Object[] { new Object(), new Object(), new Object(),
						new Object(), new Object(), new Object(), new Object(),
						new Object(), new Object() };

	}

	@Test
	public void testTreeCreation() {
		Walker<Object> w = new PrintParenthesesWalker<Object>();

		Tree<Object> tree = new Tree<Object>();
		tree.setRoot(new Node<Object>(objects[0]));
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
