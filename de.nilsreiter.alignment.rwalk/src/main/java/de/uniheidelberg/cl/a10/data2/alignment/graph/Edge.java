package de.uniheidelberg.cl.a10.data2.alignment.graph;


public class Edge<T> {
	public Edge(final T t1, final T t2) {
		super();
		this.t1 = t1;
		this.t2 = t2;
	}

	T t1;
	T t2;

	public T getT1() {
		return t1;
	}

	public void setT1(final T t1) {
		this.t1 = t1;
	}

	public T getT2() {
		return t2;
	}

	public void setT2(final T t2) {
		this.t2 = t2;
	}
}