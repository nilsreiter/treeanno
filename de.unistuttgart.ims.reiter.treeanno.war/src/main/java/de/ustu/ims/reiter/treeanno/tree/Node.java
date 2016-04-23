package de.ustu.ims.reiter.treeanno.tree;

import java.util.LinkedList;
import java.util.List;

public class Node<T> {
	T object;
	List<Node<T>> children = new LinkedList<Node<T>>();

	public Node(T obj) {
		object = obj;
	}

	public Node(T obj, List<Node<T>> children) {
		this.object = obj;
		this.children = children;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public List<Node<T>> getChildren() {
		return children;
	}

	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}
}
