
package de.pado.sigf;

interface Observation<E> {
    
    E elementAt(int index);
    boolean equals(Observation<E> otherObservation);
    int size();
}
