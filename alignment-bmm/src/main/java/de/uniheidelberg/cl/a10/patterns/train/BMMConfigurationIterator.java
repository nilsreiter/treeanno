package de.uniheidelberg.cl.a10.patterns.train;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.reiter.util.CollectionIterator;
import de.uniheidelberg.cl.reiter.util.ConcatenatingIterator;
import de.uniheidelberg.cl.reiter.util.DoubleIterator;
import de.uniheidelberg.cl.reiter.util.ResetableIterator;

public class BMMConfigurationIterator implements Iterator<BMMConfiguration> {

	BMMConfiguration current;

	List<Field> fields;
	List<ResetableIterator<? extends Object>> iterators;
	boolean inited = false;

	public BMMConfigurationIterator(final BMMConfiguration sConf) {
		try {
			current = sConf.clone();
		} catch (CloneNotSupportedException e) {
			// This can't happen
		}
		fields = new ArrayList<Field>();
		iterators = new ArrayList<ResetableIterator<? extends Object>>();

	}

	public void setInterestingFields() {
		try {
			List<ResetableIterator<Double>> iters = new LinkedList<ResetableIterator<Double>>();
			iters.add(new CollectionIterator<Double>(Arrays
					.asList(Double.MIN_VALUE)));
			iters.add(new DoubleIterator(0.1, 0.9, 0.1));

			addField("threshold", new ConcatenatingIterator<Double>(iters));

			/*
			 * addField( "combination", new
			 * EnumIterator<Operation>(Arrays.asList(Operation .values())));
			 */

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a new field to iterate over.
	 * 
	 * @param fieldName
	 *            The name of the field
	 * @param iterator
	 *            An iterator for the values of the field
	 * @throws SecurityException
	 *             If the field is not accessible
	 * @throws NoSuchFieldException
	 *             If a field with the given name does not exist
	 */

	public synchronized void addField(final String fieldName,
			final ResetableIterator<? extends Object> iterator)
			throws SecurityException, NoSuchFieldException {
		this.fields.add(BMMConfiguration.class.getField(fieldName));
		this.iterators.add(iterator);
	}

	@Override
	public boolean hasNext() {
		boolean r = false;
		for (Iterator<? extends Object> it : this.iterators) {
			r = r || it.hasNext();
		}
		return r;
	}

	public BMMConfiguration next(final int i) {
		Field field = this.fields.get(i);
		ResetableIterator<? extends Object> iter = this.iterators.get(i);

		if (iter.hasNext() && inited) {
			try {
				field.set(current, iter.next());
				/*
				 * for (int j = i; j < fields.size(); j++) {
				 * fields.get(j).set(current, iterators.get(j).current()); }
				 */
				return current.clone();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (CloneNotSupportedException e) {
				// this can't happen
			}
		} else {
			iter.reset();
			try {
				field.set(current, iter.next());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BMMConfiguration r = null;
			if (i < this.fields.size() - 1) {
				r = next(i + 1);
			} else {
				try {
					r = current.clone();
				} catch (CloneNotSupportedException e) {
					// This can't happen
				}
			}
			inited = true;
			return r;
		}
		return null;
	}

	@Override
	public BMMConfiguration next() {
		return next(0);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
