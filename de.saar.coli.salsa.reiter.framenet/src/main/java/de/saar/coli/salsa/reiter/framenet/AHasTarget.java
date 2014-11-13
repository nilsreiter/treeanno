package de.saar.coli.salsa.reiter.framenet;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniheidelberg.cl.reiter.util.CharacterRange;
import de.uniheidelberg.cl.reiter.util.Range;
import de.uniheidelberg.cl.reiter.util.TokenRange;

/**
 * Abstract class for all classes with targets.
 * 
 * @author reiter
 * 
 */
public abstract class AHasTarget implements IHasTarget, Comparable<AHasTarget> {
    /**
     * the target of the annotation.
     */
    SortedSet<IToken> target_ = null;

    public AHasTarget() {
	target_ = new TreeSet<IToken>(new TokenComparator());
    }

    /**
     * Returns a string representation of the target, using sep as separator.
     * 
     * @param sep
     *            the character used to separate the target tokens.
     * @return a string
     */
    public String getTargetString(final char sep) {
	StringBuffer buf = new StringBuffer();
	for (IToken token : this.getTargetList()) {
	    buf.append(token.toString());
	    buf.append(sep);
	}
	return buf.deleteCharAt(buf.length() - 1).toString();
    }

    /**
     * Returns the target tokens space-separated.
     * 
     * @return A string consisting of the target tokens, using a space as
     *         separator.
     */
    public String getTargetString() {
	return this.getTargetString(' ');
    }

    public TokenRange getTargetTokenRange() {
	int b = Integer.MAX_VALUE, e = Integer.MIN_VALUE;
	if (this.getTargetList().size() > 0) {
	    for (IToken token : this.getTargetList()) {
		b = Math.min(b, token.getTokenRange().getElement1());
		e = Math.max(e, token.getTokenRange().getElement2());
	    }
	    return new TokenRange(b, e);
	}

	return new TokenRange(0, 0);
    }

    /**
     * This method returns a range object covering the complete list of targets.
     * This only works if the targets are ordered and we assume that they are
     * continuous. In case the object has no targets or the targets have no
     * ranges, the range covers 0 to 0.
     * 
     * @return A range object
     */
    public CharacterRange getTargetCharacterRange() {

	if (this.getTargetList().size() > 0) {
	    CharacterRange first =
		    this.getTargetList().first().getCharacterRange();
	    CharacterRange r = first.copy();
	    r.setElement2(this.getTargetList().last().getCharacterRange()
		    .getElement2());
	    return r;
	}

	return new CharacterRange(0, 0);

    }

    /**
     * This method returns the range, subtracting 1 from the second element. It
     * shouldn't be used anymore.
     * 
     * @deprecated Use
     *             <code>{@link #getTargetCharacterRange()}.{@link de.uniheidelberg.cl.reiter.de.uniheidelberg.cl.reiter.util.Range#copy(int, int) copy(0,-1)}</code>
     *             instead.
     * @return a Range object
     */
    @Deprecated
    public Range getXMLTargetRange() {
	Range r = this.getTargetCharacterRange();
	r.setElement2(r.getElement2() - 1);
	return r;
    }

    @Override
    public int compareTo(final AHasTarget aht) {
	if (this.isNullInstantiated() || this.getTargetList().size() == 0) {
	    return -1;
	}
	if (aht.isNullInstantiated() || aht.getTargetList().size() == 0) {
	    return 1;
	}

	return this.getTargetCharacterRange().compareTo(
		aht.getTargetCharacterRange());
    }

    /**
     * This method should not be used, as it returns only the first token of the
     * target. As of 0.4.3, this method always throws an
     * UnsupportedOperationException.
     * 
     * @return the target
     */
    @Override
    public IToken getTarget() {
	return this.target_.first();
    }

    /**
     * Returns the ith target.
     * 
     * @param i
     *            The number of the target
     * @return The ith target
     */
    @Override
    public IToken getTarget(final int i) {
	Iterator<IToken> tokIter = target_.iterator();
	IToken current = null;
	for (int k = 0; k <= i; k++) {
	    current = tokIter.next();
	}
	return current;
    }

    /**
     * @param target
     *            the target to set
     */
    @Override
    public void setTarget(final IToken target) {
	if (this.target_ != null) {
	    this.target_.clear();
	} else {
	    this.target_ = new TreeSet<IToken>(new TokenComparator());
	}
	this.target_.add(target);
    }

    @Override
    public void setTargetList(final SortedSet<IToken> targetList) {
	this.target_ = targetList;
    }

    @Override
    public SortedSet<IToken> getTargetList() {
	if (this.target_ == null) {
	    this.target_ = new TreeSet<IToken>(new TokenComparator());
	}
	return this.target_;
    }

    @Override
    public void addTarget(final IToken target) {
	if (this.target_ == null) {
	    this.target_ = new TreeSet<IToken>(new TokenComparator());
	}
	this.target_.add(target);
    }
}
