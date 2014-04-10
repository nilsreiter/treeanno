/**
 * 
 * Copyright 2009-2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */
package de.uniheidelberg.cl.reiter.util;

/**
 * This class can be used to represent pairs of any class of objects.
 * 
 * @author reiter
 * 
 * @param <S>
 * @param <T>
 */
public class Pair<S extends Comparable<S>, T extends Comparable<T>> implements
	Comparable<Pair<S, T>> {
    /**
     * The first element.
     */
    S elemnt1;

    /**
     * The second element.
     */
    T elemnt2;

    /**
     * A constructor taking the two elements.
     * 
     * @param e1
     *            The first element
     * @param e2
     *            The second element
     */
    public Pair(final S e1, final T e2) {
	this.elemnt1 = e1;
	this.elemnt2 = e2;
    }

    @Override
    public int compareTo(final Pair<S, T> other) {
	int c1 = this.getElement1().compareTo(other.getElement1());
	if (c1 != 0) {
	    return c1;
	}
	return this.getElement2().compareTo(other.getElement2());
    }

    /**
     * This method determines whether two pairs are equal. Two pairs are equal,
     * if the first as well as second element of both pairs is equal,
     * correspondingly.
     * 
     * @param other
     *            The other pair
     * @return true or false
     */
    public boolean equals(final Pair<S, T> other) {
	return this.elemnt1.equals(other.getElement1())
		&& this.getElement2().equals(other.getElement2());
    }

    @Override
    public int hashCode() {
	return this.elemnt1.hashCode() + this.elemnt2.hashCode();
    }

    /**
     * Accessor for the first element.
     * 
     * @return the first element
     */
    public S getElement1() {
	return elemnt1;
    }

    /**
     * Accessor for the second element.
     * 
     * @return the second element
     */
    public T getElement2() {
	return elemnt2;
    }

    @Override
    public String toString() {
	return elemnt1.toString() + "-" + elemnt2.toString();
    }

    /**
     * Creates a duplicate of this object.
     * 
     * @return A new instance of Pair<S, T>, with the same values
     */
    public Pair<S, T> copy() {
	Pair<S, T> newPair = new Pair<S, T>(this.elemnt1, this.elemnt2);

	return newPair;

    }

    /**
     * Settor for the first element.
     * 
     * @param element1
     *            the element1 to set
     */
    public void setElement1(final S element1) {
	this.elemnt1 = element1;
    }

    /**
     * Setter for the second element.
     * 
     * @param element2
     *            the element2 to set
     */
    public void setElement2(final T element2) {
	this.elemnt2 = element2;
    }
}
