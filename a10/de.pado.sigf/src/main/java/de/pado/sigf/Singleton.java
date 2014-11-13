
/** 
 * A data structure that holds one item and implements Observation
 * Inspired Ted Grenager's Triple implementation in the Stanford JavaNLP system.
 * Simple and efficient.
 */

package de.pado.sigf;

public class Singleton<E> implements Observation<E> {

  public E first;


  public Singleton(E first) {
    this.first = first;
  }

  public E first() {
    return first;
  }
  
  public E elementAt(int index) {
	if (index == 0) return first();
	else
	    throw new ArrayIndexOutOfBoundsException();
  }
  
  public int size() {
      return 1;
  }
  
  public boolean equals(Observation<E> otherObs) {
	return ((otherObs.size() == this.size()) &&
		(first().equals(otherObs.elementAt(0))));
  }
}