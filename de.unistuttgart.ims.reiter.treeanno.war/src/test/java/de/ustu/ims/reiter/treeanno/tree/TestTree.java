package de.ustu.ims.reiter.treeanno.tree;

import org.junit.Test;

public class TestTree {
	@Test
	public void testTreeCreation() {
		Node<String> n = new Node<String>("A");
		Tree<String> tree = new Tree<String>();
		tree.setRoot(n);
		tree.addChild("A", "B");
		tree.addChild("A", "B");
		tree.addChild("B", "C");

		Walker<String> w = new PrintParenthesesWalker<String>();
		tree.depthFirstWalk(w);
		System.out.println(w.toString());
	}
}
