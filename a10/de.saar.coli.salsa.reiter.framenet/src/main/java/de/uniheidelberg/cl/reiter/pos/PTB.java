package de.uniheidelberg.cl.reiter.pos;

/**
 * This enum represents part of speech tags used in the Penn Treebank.
 * 
 * @author reiter
 * 
 */
public enum PTB implements IPartOfSpeech, Comparable<PTB> {

    /** Coordinating conjunction. */
    CC, /** Cardinal number. */
    CD, /** Determiner. */
    DT, /** Existential there. */
    EX, //
    /** Foreign word */
    FW, //
    /** Preposition or subordinating conjunction */
    IN, //
    /** Adjective */
    JJ, //
    /** Adjective, comparative */
    JJR, //
    /** Adjective, superlative */
    JJS, //
    /** List item marker */
    LS, //
    /** Modal */
    MD, //
    /** Noun, singular or mass */
    NN, //
    /** Noun, plural */
    NNS, //
    /** Proper noun, singular */
    NNP, //
    /** Proper noun, plural */
    NNPS, //
    /** Predeterminer */
    PDT, //
    /** Possessive ending */
    POS, //
    /** Personal pronoun */
    PRP, //
    /** Possessive pronoun (final prolog version PRP-S) */
    PRPS, //
    /** Adverb */
    RB, //
    /** Adverb, comparative */
    RBR, //
    /** Adverb, superlative */
    RBS, //
    /** Particle */
    RP, //
    /** Symbol */
    SYM, //
    /** to */
    TO, //
    /** Interjection */
    UH, //
    /** Verb, base form */
    VB, //
    /** Verb, past tense */
    VBD, //
    /** Verb, gerund or present participle */
    VBG, //
    /** Verb, past participle */
    VBN, //
    /** Verb, non-3rd person singular present */
    VBP, //
    /** Verb, 3rd person singular present */
    VBZ,
    /** Wh-determiner */
    WDT,
    /** Wh-pronoun */
    WP,
    /** Possessive wh-pronoun (final prolog version WP-S) */
    WPS,
    /** Wh-adverb */
    WRB,
    /** dollar */
    DOLLAR,
    /**
     * opening quotation mark
     */
    QUOTATION_OPEN,
    /**
     * closing quotation mark
     */
    QUOTATION_CLOSE,
    /**
     * opening parenthesis
     */
    PARENTHESIS_OPEN,
    /**
     * closing parenthesis
     */
    PARENTHESIS_CLOSE,
    /**
     * dash
     */
    DASH,
    /**
     * sentence terminator
     */
    SENT,
    /**
     * colon or ellipsis
     */
    COLON,
    /**
     * comma
     */
    COMMA;

    /**
     * If the pos tag is SYM, we store the actual symbol here
     */
    String symbolContent = null;

    @Override
    public WordClass getWordClass() {
	switch (this) {
	case JJ:
	case JJR:
	case JJS:
	    return WordClass.Adjective;
	case RB:
	case RBR:
	case RBS:
	case WRB:
	    return WordClass.Adverb;
	case VB:
	case VBD:
	case VBG:
	case VBN:
	case VBP:
	case VBZ:
	    return WordClass.Verb;
	case NN:
	case NNS:
	case NNP:
	case NNPS:
	    return WordClass.Noun;
	default:
	    return null;
	}
    }

    @Override
    public FN asFN() {
	switch (this) {
	case NN:
	case NNS:
	case NNP:
	case NNPS:
	    return FN.Noun;
	case VB:
	case VBD:
	case VBG:
	case VBN:
	case VBP:
	case VBZ:
	    return FN.Verb;
	case RB:
	case RBR:
	case RBS:
	case WRB:
	    return FN.Adverb;
	case JJ:
	case JJR:
	case JJS:
	    return FN.Adjective;
	case DT:
	case WDT:
	    return FN.Determiner;
	case IN:
	case TO:
	    return FN.Preposition;
	case PRP:
	case PRPS:
	    return FN.Pronoun;
	case UH:
	    return FN.Interjection;
	default:
	    return null;
	}
    }

    @Override
    public String toShortString() {
	switch (this) {
	case PARENTHESIS_OPEN:
	    return "(";
	case PARENTHESIS_CLOSE:
	    return ")";
	case QUOTATION_OPEN:
	    return "``";
	case QUOTATION_CLOSE:
	    return "''";
	case COMMA:
	    return ",";
	case COLON:
	    return ":";
	case SENT:
	    return ".";
	case DASH:
	    return "-";
	case SYM:
	    return symbolContent;
	default:
	    return super.toString();
	}
    }

    /**
     * 
     * @param s
     *            The string representation of the part of speech
     * @return The POS tag
     */
    public static PTB fromString(final String s) {
	try {
	    return PTB.valueOf(s);
	} catch (IllegalArgumentException e) {
	    if (s.matches("([({\\[]|-LRB-)")) {
		return PTB.PARENTHESIS_OPEN;
	    }
	    if (s.matches("([)}\\]]|-RRB-)")) {
		return PTB.PARENTHESIS_CLOSE;
	    }
	    if (s.equals(".")) {
		return PTB.SENT;
	    }
	    if (s.equals(",")) {
		return PTB.COMMA;
	    }
	    if (s.equals(":")) {
		return PTB.COLON;
	    }
	    if (s.equals("NP")) {
		return PTB.NNP;
	    }
	    if (s.equals("''")) {
		return PTB.QUOTATION_CLOSE;
	    }
	    if (s.equals("``")) {
		return PTB.QUOTATION_OPEN;
	    }
	    if (s.equals("-")) {
		return PTB.DASH;
	    }
	}
	PTB r = PTB.SYM;
	r.symbolContent = s;
	/* System.err.println("No valid PTB tag:" + s); */
	return r;
    }

    @Override
    public boolean hasSpaceBefore() {
	switch (this) {
	case COLON:
	case SENT:
	case COMMA:
	case QUOTATION_CLOSE:
	case PARENTHESIS_OPEN:
	    return false;

	default:
	    return true;
	}
    }

    @Override
    public boolean hasSpaceAfter() {
	switch (this) {
	case PARENTHESIS_OPEN:
	case QUOTATION_OPEN:
	    return false;
	}
	return true;
    }

}
