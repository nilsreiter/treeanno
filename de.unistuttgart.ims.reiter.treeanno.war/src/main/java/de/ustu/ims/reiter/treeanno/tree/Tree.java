package de.ustu.ims.reiter.treeanno.tree;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Tree<T> {
	Comparator<Node<T>> comparator;
	Node<T> root;
	Map<T, Node<T>> nodeIndex = new HashMap<T, Node<T>>();

	public Tree(Comparator<Node<T>> comp) {
		comparator = comp;
	}

	public Node<T> getRoot() {
		return root;
	}

	public void setRoot(Node<T> root) {
		this.root = root;
		if (root.getObject() != null)
			this.nodeIndex.put(root.getObject(), root);
	}

	public void addChild(T parent, T child) {
		Node<T> cNode = new Node<T>(child, comparator);
		this.nodeIndex.put(child, cNode);
		if (parent == null)
			this.getRoot().getChildren().add(cNode);
		else
			this.nodeIndex.get(parent).getChildren().add(cNode);

	}

	@Deprecated
	public void depthFirstWalk(Walker<T> walker) {
		walker.init();
		depthFirstWalk(walker, getRoot(), null);
	}

	@Deprecated
	protected void
	depthFirstWalk(Walker<T> walker, Node<T> node, Node<T> parent) {
		walker.beginNode(node, parent);
		for (Node<T> child : node.getChildren()) {
			depthFirstWalk(walker, child, node);
		}
		walker.endNode(node, parent);
	}
}
