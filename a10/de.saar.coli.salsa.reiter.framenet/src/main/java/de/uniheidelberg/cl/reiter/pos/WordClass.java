package de.uniheidelberg.cl.reiter.pos;

/**
 * 
 * @author reiter
 * 
 */
public enum WordClass implements IPartOfSpeech {
    /** Noun */
    Noun, /** Verb */
    Verb, /** Adjective */
    Adjective, /** Adverb */
    Adverb;

    @Override
    public WordClass getWordClass() {
	return this;
    }

    @Override
    public FN asFN() {
	switch (this) {
	case Noun:
	    return FN.Noun;
	case Verb:
	    return FN.Verb;
	case Adjective:
	    return FN.Adjective;
	case Adverb:
	    return FN.Adverb;
	default:
	    return null;
	}
    }

    @Override
    public String toShortString() {
	switch (this) {
	case Adverb:
	    return "r";
	default:
	    return this.toString().substring(0, 1);
	}
    }

    @Override
    public boolean hasSpaceBefore() {
	return false;
    }

    public static WordClass fromString(final String string) {
	return fromChar(string.charAt(0));
    }

    public static WordClass fromChar(final char character) {
	switch (character) {
	case 'n':
	case 'N':
	    return Noun;
	case 'v':
	case 'V':
	    return Verb;
	case 'a':
	case 'A':
	    return Adjective;
	case 'r':
	case 'R':
	    return Adverb;
	default:
	    return null;

	}
    }

    @Override
    public boolean hasSpaceAfter() {
	return true;
    }

}
