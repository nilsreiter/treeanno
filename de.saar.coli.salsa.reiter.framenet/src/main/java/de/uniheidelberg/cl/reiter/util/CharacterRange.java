package de.uniheidelberg.cl.reiter.util;

/**
 * 
 * @author reiter
 * 
 */
public class CharacterRange extends Range {

    public CharacterRange(final Integer e1, final Integer e2) {
	super(e1, e2);
    }

    @Override
    public CharacterRange copy() {
	CharacterRange ret = new CharacterRange(this.elemnt1, this.elemnt2);
	return ret;
    }

    @Override
    public boolean equals(final Object other) {
	if (other.getClass() == CharacterRange.class) {
	    if (this.hashCode() != other.hashCode()) {
		return false;
	    }
	    CharacterRange r = (CharacterRange) other;
	    return this.getElement1() == r.getElement1()
		    && this.getElement2() == r.getElement2();
	} else {
	    return false;
	}
    }

}
