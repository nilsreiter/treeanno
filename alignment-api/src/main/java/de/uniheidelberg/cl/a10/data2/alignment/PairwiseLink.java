package de.uniheidelberg.cl.a10.data2.alignment;

public interface PairwiseLink<T> extends Link<T> {

	void setElement2(final T element2);

	T getElement2();

	void setElement1(final T element1);

	T getElement1();

	T getElement(final int i);

}
