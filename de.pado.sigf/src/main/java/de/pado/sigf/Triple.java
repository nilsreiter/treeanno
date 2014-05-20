
/** 
 * A data structure that holds three items of the same type.
 * Inspired Ted Grenager's Triple implementation in the Stanford JavaNLP system.
 * Simple and efficient.
 */
package de.pado.sigf;

public class Triple<E> implements Observation<E> {

  public E first, second, third;


  public int size() {
      return 3;
  }
  
  public Triple(E first, E second, E third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public E first() {
    return first;
  }

  public E second() {
    return second;
  }

  public E third() {
    return third;
  }
  
  public E elementAt(int index) {
	if (index == 0) return first();
	if (index == 1) return second();
	if (index == 2) return third();
	else
	    throw new ArrayIndexOutOfBoundsException();
  }
  
  public boolean equals(Observation<E> otherObs) {
	return ((otherObs.size() == this.size()) &&
		(first().equals(otherObs.elementAt(0))) &&
		(second().equals(otherObs.elementAt(1))) &&
		(third().equals(otherObs.elementAt(2))));
  }
}