// (c) sebastian pado 06 2006

/** Interface for statistical measures **/


package de.pado.sigf;

interface Statistic<E extends Observation<?>> extends Cloneable {

    public double compute();    
    public void add_observation(E observation); 
    public Statistic<E> clone();
    public long num_observations();

}
    
