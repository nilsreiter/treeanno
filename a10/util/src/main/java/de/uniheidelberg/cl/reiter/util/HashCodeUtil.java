package de.uniheidelberg.cl.reiter.util;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Collected methods which allow easy implementation of <code>hashCode</code>.
 * 
 * Example use case:
 * 
 * <pre>
 * public int hashCode() {
 * 	int result = HashCodeUtil.SEED;
 * 	// collect the contributions of various fields
 * 	result = HashCodeUtil.hash(result, fPrimitive);
 * 	result = HashCodeUtil.hash(result, fObject);
 * 	result = HashCodeUtil.hash(result, fArray);
 * 	return result;
 * }
 * </pre>
 */
public final class HashCodeUtil {

	/**
	 * An initial value for a <code>hashCode</code>, to which is added
	 * contributions from fields. Using a non-zero value decreases collisons of
	 * <code>hashCode</code> values.
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
	 * If <code>aObject</code> is an array, then each element may be a primitive
	 * or a possibly-null object.
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
