package de.uniheidelberg.cl.reiter.statistics;

public interface IHasPRF {

	double p();

	double r();

	double f();

	double f(double beta);

	String name();
}
