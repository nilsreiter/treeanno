package de.uniheidelberg.cl.a10.data2.impl;

import java.lang.reflect.Array;
import java.util.Collection;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.data2.AnnotationObject;
import de.uniheidelberg.cl.a10.data2.Type;

/**
 * Abstract class representing any data object.
 * 
 * @author hartmann
 * 
 */
public abstract class AnnotationObject_impl implements HasId, AnnotationObject {

	/**
	 * Each object has an changeable id.
	 */
	String id;

	public AnnotationObject_impl(final String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniheidelberg.cl.a10.data2.DataObject#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/**
	 * Provides a basic string representation for all ancestors
	 */
	@Override
	public String toString() {
		return this.id;
	}

	/**
	 * This implementation of {@link java.lang.Object#equals(Object)} assumes
	 * uniqueness of ids. In case we operate on multiple documents, ids might
	 * not be unique. The method should be overwritten then, as in class
	 * {@link Frame_impl} .
	 */
	@Override
	public boolean equals(final Object other) {
		if (this == other) return true;
		if (!(other instanceof AnnotationObject_impl)) return false;
		AnnotationObject_impl doo = (AnnotationObject_impl) other;
		return this.getId().equals(doo.getId());
	}

	/**
	 * See {@link #equals(Object)}, the same remarks are valid for this method.
	 */
	@Override
	public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, id);
		return result;
	}

	@Override
	@Deprecated
	public Type getType() {
		return Type.getType(this.getClass());
	}

	final static class HashCodeUtil {

		/**
		 * An initial value for a <code>hashCode</code>, to which is added
		 * contributions from fields. Using a non-zero value decreases collisons
		 * of <code>hashCode</code> values.
		 */
		public static final int SEED = 23;

		/**
		 * booleans.
		 */
		public static int hash(final int aSeed, final boolean aBoolean) {
			return firstTerm(aSeed) + (aBoolean ? 1 : 0);
		}

		/**
		 * chars.
		 */
		public static int hash(final int aSeed, final char aChar) {
			return firstTerm(aSeed) + aChar;
		}

		/**
		 * ints.
		 */
		public static int hash(final int aSeed, final int aInt) {
			/*
			 * Implementation Note Note that byte and short are handled by this
			 * method, through implicit conversion.
			 */
			return firstTerm(aSeed) + aInt;
		}

		/**
		 * longs.
		 */
		public static int hash(final int aSeed, final long aLong) {
			return firstTerm(aSeed) + (int) (aLong ^ (aLong >>> 32));
		}

		/**
		 * floats.
		 */
		public static int hash(final int aSeed, final float aFloat) {
			return hash(aSeed, Float.floatToIntBits(aFloat));
		}

		/**
		 * doubles.
		 */
		public static int hash(final int aSeed, final double aDouble) {
			return hash(aSeed, Double.doubleToLongBits(aDouble));
		}

		/**
		 * <code>aObject</code> is a possibly-null object field, and possibly an
		 * array.
		 * 
		 * If <code>aObject</code> is an array, then each element may be a
		 * primitive or a possibly-null object.
		 */
		public static int hash(final int aSeed, final Object aObject) {
			int result = aSeed;
			if (aObject == null) {
				result = hash(result, 0);
			} else if (isCollection(aObject)) {
				Collection<?> coll = (Collection<?>) aObject;
				for (Object o : coll) {
					result = hash(result, o);
				}
			} else if (isArray(aObject)) {
				int length = Array.getLength(aObject);
				for (int idx = 0; idx < length; ++idx) {
					Object item = Array.get(aObject, idx);
					// recursive call!
					result = hash(result, item);
				}

			} else {
				result = hash(result, aObject.hashCode());
			}
			return result;
		}

		// / PRIVATE ///
		private static final int fODD_PRIME_NUMBER = 37;

		private static int firstTerm(final int aSeed) {
			return fODD_PRIME_NUMBER * aSeed;
		}

		private static boolean isCollection(final Object object) {
			return (object instanceof Collection<?>);
		}

		private static boolean isArray(final Object aObject) {
			return aObject.getClass().isArray();
		}
	}
}
