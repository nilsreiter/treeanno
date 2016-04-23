package de.ustu.ims.reiter.treeanno.tree;

import java.util.HashMap;
import java.util.Map;

public class Tree<T> {
	Node<T> root;
	Map<T, Node<T>> nodeIndex = new HashMap<T, Node<T>>();

	public Node<T> getRoot() {
		return root;
	}

	public void setRoot(Node<T> root) {
		this.root = root;
		if (root.getObject() != null)
			this.nodeIndex.put(root.getObject(), root);
	}

	public void addChild(T parent, T child) {
		Node<T> cNode = new Node<T>(child);
		this.nodeIndex.put(child, cNode);
		if (parent == null)
			this.getRoot().getChildren().add(cNode);
		else
			this.nodeIndex.get(parent).getChildren().add(cNode);

	}

	public void depthFirstWalk(Walker<T> walker) {
		walker.init();
		depthFirstWalk(walker, getRoot());
	}

	protected void depthFirstWalk(Walker<T> walker, Node<T> node) {
		walker.beginNode(node);
		for (Node<T> child : node.getChildren()) {
			depthFirstWalk(walker, child);
		}
		walker.endNode(node);
	}
}
