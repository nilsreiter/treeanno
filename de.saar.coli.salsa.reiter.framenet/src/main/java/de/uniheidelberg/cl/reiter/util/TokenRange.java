package de.uniheidelberg.cl.reiter.util;

public class TokenRange extends Range {

    public TokenRange(final Integer e1, final Integer e2) {
	super(e1, e2);
	// TODO Auto-generated constructor stub
    }

    @Override
    public TokenRange copy() {
	TokenRange ret = new TokenRange(this.elemnt1, this.elemnt2);
	return ret;
    }

    @Override
    public boolean equals(final Object other) {
	if (other.getClass() == this.getClass()) {
	    if (this.hashCode() != other.hashCode()) {
		return false;
	    }
	    TokenRange r = (TokenRange) other;
	    return this.getElement1() == r.getElement1()
		    && this.getElement2() == r.getElement2();
	} else {
	    return false;
	}
    }

}
