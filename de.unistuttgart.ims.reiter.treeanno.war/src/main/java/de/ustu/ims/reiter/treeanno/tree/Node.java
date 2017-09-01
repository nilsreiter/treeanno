package de.ustu.ims.reiter.treeanno.tree;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Node<T> {
	T object;
	SortedSet<Node<T>> children;
	Comparator<Node<T>> comparator;

	public Node(T obj, Comparator<Node<T>> comp) {
		object = obj;
		comparator = comp;
		children = new TreeSet<Node<T>>(comparator);
	}

	public Node(T obj, Comparator<Node<T>> comp, SortedSet<Node<T>> children) {
		this.object = obj;
		this.comparator = comp;
		this.children = children;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public SortedSet<Node<T>> getChildren() {
		return children;
	}

	public void setChildren(SortedSet<Node<T>> children) {
		this.children = children;
	}

	public void depthFirstWalk(Walker<T, ? extends Object> walker) {
		walker.init();
		depthFirstWalk(walker, this, null);
	}

	protected void depthFirstWalk(Walker<T, ? extends Object> walker, Node<T> node, Node<T> parent) {
		walker.beginNode(node, parent);
		for (Node<T> child : node.getChildren()) {
			depthFirstWalk(walker, child, node);
		}
		walker.endNode(node, parent);
	}
}
