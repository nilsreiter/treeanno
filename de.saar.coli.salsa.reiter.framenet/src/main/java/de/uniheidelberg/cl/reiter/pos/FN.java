package de.uniheidelberg.cl.reiter.pos;

/**
 * 
 * @author reiter
 * 
 */
public enum FN implements IPartOfSpeech {
    /**
     * Noun
     */
    Noun, // Nouns

    /**
     * Verb
     */
    Verb, // Verbs

    /**
     * Adjective
     */
    Adjective,

    /**
     * Adverb
     */
    Adverb,

    /**
     * Determiner
     */
    Determiner,

    /**
     * Preposition
     */
    Preposition,

    /**
     * Pronoun
     */
    Pronoun,

    /**
     * Interjection
     */
    Interjection,

    /**
     * Numeral
     */
    Numeral,

    /**
     * Unknown meaning, i.e., not documented in FrameNet
     */
    SCON,

    /**
     * Conjunction
     */
    Conjunction;

    @Override
    public WordClass getWordClass() {
	switch (this) {
	case Noun:
	    return WordClass.Noun;
	case Verb:
	    return WordClass.Verb;
	case Adjective:
	    return WordClass.Adjective;
	case Adverb:
	    return WordClass.Adverb;
	default:
	    return null;
	}
    }

    @Override
    public FN asFN() {
	return this;
    }

    @Override
    public String toShortString() {
	switch (this) {
	case Adjective:
	    return "a";
	case Adverb:
	    return "adv";
	case Noun:
	    return "n";
	case Verb:
	    return "v";
	case Numeral:
	    return "num";
	case Conjunction:
	    return "c";
	case SCON:
	    return "scon";
	case Determiner:
	    return "art";
	case Preposition:
	    return "prep";
	case Interjection:
	    return "intj";
	case Pronoun:
	    return "pron"; // TODO: This is a hack, because Semafor can't handle
			   // pronouns (and FrameNet doesn't use them)
	default:
	    return "";
	}
    }

    /**
     * Converts from a short string (as returned by {@link #toShortString()}
     * into a FN-POS-object.
     * 
     * @param shortString
     *            The short string
     * @return A pos-tag
     */
    public static FN fromShortString(final String shortString) {
	if (shortString.equalsIgnoreCase("a")) {
	    return FN.Adjective;
	}
	if (shortString.equalsIgnoreCase("adv")) {
	    return FN.Adverb;
	}
	if (shortString.equalsIgnoreCase("n")) {
	    return FN.Noun;
	}
	if (shortString.equalsIgnoreCase("v")) {
	    return FN.Verb;
	}
	if (shortString.equalsIgnoreCase("num")) {
	    return FN.Numeral;
	}
	if (shortString.equalsIgnoreCase("c")) {
	    return FN.Conjunction;
	}
	if (shortString.equalsIgnoreCase("scon")) {
	    return FN.SCON;
	}
	if (shortString.equalsIgnoreCase("art")) {
	    return FN.Determiner;
	}
	if (shortString.equalsIgnoreCase("prep")) {
	    return FN.Preposition;
	}
	if (shortString.equalsIgnoreCase("intj")) {
	    return FN.Interjection;
	}
	if (shortString.equalsIgnoreCase("pron")) {
	    return FN.Pronoun;
	}

	return null;
    }

    @Override
    public boolean hasSpaceBefore() {
	return true;
    }

    @Override
    public boolean hasSpaceAfter() {
	// TODO: This is not a real implementation
	return true;
    }

}
