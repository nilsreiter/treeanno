package de.uniheidelberg.cl.a10.patterns.data;

import gnu.trove.map.hash.THashMap;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.Pair;

public class ProbabilityDistribution<T> implements Map<T, Probability>,
		Serializable {

	private static final long serialVersionUID = 1L;

	Map<T, Probability> probabilities = null;

	Probability max = Probability.NULL;

	public ProbabilityDistribution() {
		probabilities = new THashMap<T, Probability>();
	}

	public ProbabilityDistribution(final ProbabilityDistribution<T> pd) {
		probabilities = new THashMap<T, Probability>();
		max = pd.max;
		for (T t : pd.keySet()) {
			this.probabilities.put(t, pd.get(t));
		}
	}

	public Pair<Probability, Set<T>> getMax() {
		HashSet<T> set = new HashSet<T>();

		for (T k : this.keySet()) {
			if (this.get(k).equals(max)) {
				set.add(k);
			}
		}

		return new Pair<Probability, Set<T>>(max, set);

	}

	public Probability getHighestProbability() {
		return this.getMax().getFirst();
	}

	public Set<T> getObjectsWithHighestProbability() {
		return this.getMax().getSecond();
	}

	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		probabilities.clear();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(final Object key) {
		return probabilities.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(final Object value) {
		return probabilities.containsValue(value);
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<T, Probability>> entrySet() {
		return probabilities.entrySet();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		return probabilities.equals(o);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Probability get(final Object key) {
		if (!this.probabilities.containsKey(key)) {
			return Probability.NULL;
		}
		return probabilities.get(key);
	}

	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	@Override
	public int hashCode() {
		return probabilities.hashCode();
	}

	/**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return probabilities.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<T> keySet() {
		return probabilities.keySet();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Probability put(final T key, final Probability value) {
		if (value == Probability.NULL) {
			return Probability.NULL;
		}
		this.max = PMath.max(this.max, value);
		return probabilities.put(key, value);
	}

	public Probability put(final T key, final double value) {
		return this.put(key, Probability.fromProbability(value));
	}

	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends T, ? extends Probability> m) {
		probabilities.putAll(m);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public Probability remove(final Object key) {
		return probabilities.remove(key);
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return probabilities.size();
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<Probability> values() {
		return probabilities.values();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("{");
		for (Map.Entry<T, Probability> entry : this.probabilities.entrySet()) {
			b.append(entry.getKey().toString()).append('=');
			b.append(entry.getValue()).append(',');
		}
		b.append("}");
		return b.toString();
	}

	public double sum() {
		double d = 0.0;
		for (T t : this.keySet()) {
			d += this.get(t).getProbability();
		}
		return d;
	}
}
