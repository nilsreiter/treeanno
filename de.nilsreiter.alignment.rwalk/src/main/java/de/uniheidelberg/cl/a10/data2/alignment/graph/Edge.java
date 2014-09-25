package de.uniheidelberg.cl.a10.data2.alignment.graph;

import de.uniheidelberg.cl.a10.data2.Event;

public class Edge {
	public Edge(final Event t1, final Event t2) {
		super();
		this.t1 = t1;
		this.t2 = t2;
	}

	Event t1;
	Event t2;

	public Event getT1() {
		return t1;
	}

	public void setT1(final Event t1) {
		this.t1 = t1;
	}

	public Event getT2() {
		return t2;
	}

	public void setT2(final Event t2) {
		this.t2 = t2;
	}
}