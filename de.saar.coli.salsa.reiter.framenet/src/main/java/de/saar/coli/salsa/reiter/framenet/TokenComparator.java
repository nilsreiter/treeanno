package de.saar.coli.salsa.reiter.framenet;

import java.util.Comparator;

public class TokenComparator implements Comparator<IToken> {

    @Override
    public int compare(final IToken o1, final IToken o2) {
	return o1.getCharacterRange().compareTo(o2.getCharacterRange());
    }

}
