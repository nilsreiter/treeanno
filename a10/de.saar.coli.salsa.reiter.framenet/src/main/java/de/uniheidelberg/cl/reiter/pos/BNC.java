package de.uniheidelberg.cl.reiter.pos;

/**
 * Contains POS-Tags used in the British National Corpus, BNC.
 * 
 * @author reiter
 * 
 */
public enum BNC implements IPartOfSpeech {
    /** adjective (general or positive) e.g. good, old */
    AJ0,
    /** comparative adjective e.g. better, older */
    AJC,
    /** superlative adjective, e.g. best, oldest */
    AJS,
    /**
     * adverb (general, not sub-classified as AVP or AVQ), e.g. often, well,
     * longer, furthest.
     */
    AV0,
    /** adverb particle, e.g. up, off, out. */
    AVP,
    /**
     * wh-adverb, e.g. when, how, why, whether the word is used interrogatively
     * or to introduce a relative clause.
     */
    AVQ,
    /** coordinating conjunction, e.g. and, or, but. */
    CJC,
    /** subordinating conjunction, e.g. although, when. */
    CJS,
    /**
     * the subordinating conjunction that, when introducing a relative clause,
     * as in the day that follows Christmas.
     */
    CJT,
    /** cardinal numeral, e.g. one, 3, fifty-five, 6609. */
    CRD,
    /** ordinal numeral, e.g. first, sixth, 77th, next, last. */
    ORD,
    /** article, e.g. the, a, an, no. */
    AT0,
    /** possessive determiner form, e.g. your, their, his. */
    DPS,
    /**
     * general determiner: a determiner which is not a DTQ e.g. this both in
     * This is my house and This house is mine.
     */
    DT0,
    /**
     * wh-determiner, e.g. which, what, whose, which, whether used
     * interrogatively or to introduce a relative clause.
     */
    DTQ,
    /** common noun, neutral for number, e.g. aircraft, data, committee. */
    NN0,
    /** singular common noun, e.g. pencil, goose, time, revelation. */
    NN1,
    /** plural common noun, e.g. pencils, geese, times, revelations. */
    NN2,
    /** proper noun, e.g. London, Michael, Mars, IBM. */
    NP0,
    /** indefinite pronoun, e.g. none, everything, one (pronoun), nobody. */
    PNI,
    /**
     * personal pronoun, e.g. I, you, them, ours. possessive pronouns such // as
     * ours and theirs are included in this category.
     */
    PNP,
    /** wh-pronoun, e.g. who, whoever, whom. */
    PNQ,
    /** reflexive pronoun, e.g. myself, yourself, itself, ourselves. */
    PNX,
    /**
     * the possessive or genitive marker 's or ', tagged as a distinct word.
     */
    POS,
    /** the preposition of. */
    PRF,
    /**
     * preposition, other than of, e.g. about, at, in, on behalf of, with.
     * Prepositional phrases like on behalf of or in spite of treated as single
     * words.
     */
    PRP,
    /**
     * the present tense forms of the verb be, except for is or 's: am, are 'm,
     * 're, be (subjunctive or imperative), ai (as in ain't).
     */
    VBB,
    /** the past tense forms of the verb be: was, were. */
    VBD,
    /** -ing form of the verb be: being. */
    VBG,
    /** the infinitive form of the verb be: be. */
    VBI,
    /** the past participle form of the verb be: been */
    VBN,
    /** the -s form of the verb be: is, 's. */
    VBZ,
    /** the finite base form of the verb do: do. */
    VDB,
    /** the past tense form of the verb do: did. */
    VDD,
    /** the -ing form of the verb do: doing. */
    VDG,
    /** the infinitive form of the verb do: do. */
    VDI,
    /** the past participle form of the verb do: done. */
    VDN,
    /** the -s form of the verb do: does. */
    VDZ,
    /** the finite base form of the verb have: have, 've. */
    VHB,
    /** the past tense form of the verb have: had, 'd. */
    VHD,
    /** the -ing form of the verb have: having. */
    VHG,
    /** the infinitive form of the verb have: have. */
    VHI,
    /** the past participle form of the verb have: had. */
    VHN,
    /** the -s form of the verb have: has, 's. */
    VHZ,
    /**
     * modal auxiliary verb, e.g. can, could, will, 'll, 'd, wo (as in won't)
     */
    VM0,
    /**
     * the finite base form of lexical verbs, e.g. forget, send, live, return.
     * This tag is used for imperatives and the present subjunctive forms, but
     * not for the infinitive (VVI).
     */
    VVB,
    /**
     * the past tense form of lexical verbs, e.g. forgot, sent, lived, returned.
     */
    VVD,
    /**
     * the -ing form of lexical verbs, e.g. forgetting, sending, living,
     * returning.
     */
    VVG,
    /**
     * the infinitive form of lexical verbs , e.g. forget, send, live, return.
     */
    VVI,
    /**
     * the past participle form of lexical verbs, e.g. forgotten, sent, lived,
     * returned.
     */
    VVN,
    /** the -s form of lexical verbs, e.g. forgets, sends, lives, returns. */
    VVZ,
    /**
     * existential there, the word there appearing in the constructions there
     * is..., there are ....
     */
    EX0,
    /** interjection or other isolate, e.g. oh, yes, mhm, wow. */
    ITJ,
    /** the infinitive marker to. */
    TO0,
    /**
     * unclassified items which are not appropriately classified as items of the
     * English lexicon.
     */
    UNC,
    /** the negative particle not or n't. */
    XX0,
    /** alphabetical symbols, e.g. A, a, B, b, c, d. */
    ZZ0,
    /**
     * Punctuation: left bracket - i.e. ( or [
     */
    PUL,
    /**
     * Punctuation: general separating mark - i.e. . , ! , : ; - or ?
     */
    PUN,

    /**
     * Punctuation: quotation mark - i.e. ' or "
     */
    PUQ,

    /**
     * Punctuation: right bracket - i.e. ) or ]
     */
    PUR;

    /**
     * True if this is a ambiguity tag
     */
    boolean amiguityTag = false;

    /**
     * The second choice for ambiguity tags. If this is not an ambiguity tag,
     * this field is null.
     */
    BNC secondChoice = null;

    @Override
    public WordClass getWordClass() {
	switch (this) {
	case NN0:
	case NN1:
	case NN2:
	case NP0:
	    return WordClass.Noun;
	case AJ0:
	case AJC:
	case AJS:
	    return WordClass.Adjective;
	case AV0:
	case AVP:
	case AVQ:
	    return WordClass.Adverb;
	case VBB:
	case VBD:
	case VBG:
	case VBI:
	case VBN:
	case VBZ:
	case VDB:
	case VDD:
	case VDG:
	case VDI:
	case VDN:
	case VDZ:
	case VHB:
	case VHD:
	case VHG:
	case VHI:
	case VHN:
	case VHZ:
	case VM0:
	case VVB:
	case VVD:
	case VVG:
	case VVI:
	case VVN:
	case VVZ:
	    return WordClass.Verb;
	default:
	    return null;
	}
    }

    @Override
    public FN asFN() {
	switch (this) {
	case AJ0:
	case AJC:
	case AJS:
	case ORD:
	    return FN.Adjective;
	case AV0:
	case AVQ:
	    return FN.Adverb;
	case CJC:
	case CJS:
	case CJT:
	    return FN.Conjunction;
	case CRD:
	    return FN.Numeral;
	case AT0:
	case DPS:
	case DT0:
	case DTQ:
	case EX0:
	    return FN.Determiner;
	case NN0:
	case NN1:
	case NN2:
	    return FN.Noun;
	case PNI:
	case PNP:
	case PNQ:
	case PNX:
	    return FN.Pronoun;
	case PRF:
	case PRP:
	    return FN.Preposition;
	case VBB:
	case VBD:
	case VBG:
	case VBI:
	case VBN:
	case VBZ:
	case VDB:
	case VDD:
	case VDG:
	case VDI:
	case VDN:
	case VDZ:
	case VHB:
	case VHD:
	case VHG:
	case VHI:
	case VHN:
	case VHZ:
	case VM0:
	case VVB:
	case VVD:
	case VVG:
	case VVI:
	case VVZ:
	case VVN:
	    return FN.Verb;
	case ITJ:
	    return FN.Interjection;
	default:
	    return null;
	}
    }

    public PTB asPTB() {
	switch (this) {
	case AJ0:
	    return PTB.JJ;
	case AJC:
	    return PTB.JJR;
	case AJS:
	    return PTB.JJS;
	case AV0:
	    return PTB.RB;
	case AVQ:
	    return PTB.WRB;
	case AVP:
	    return PTB.RP;
	case CJC:
	    return PTB.CC;
	case CJS:
	    return PTB.IN;
	case CJT:
	    return PTB.IN;
	case CRD:
	    return PTB.CD;
	case AT0:
	    return PTB.DT;
	case DPS:
	    return PTB.PRPS;
	case DT0:
	    return PTB.DT;
	case DTQ:
	    return PTB.WDT;
	case EX0:
	    return PTB.EX;
	case NN0:
	case NN1:
	    return PTB.NN;
	case NN2:
	    return PTB.NNS;
	case NP0:
	    return PTB.NNP;
	case ORD:
	    return PTB.JJ;
	case PNI:
	    return PTB.PDT;
	case PNP:
	    return PTB.PRP;
	case PNQ:
	    return PTB.WP;
	case PNX:
	    return PTB.PRP;
	case POS:
	    return PTB.POS;
	case PRF:
	    return PTB.IN;
	case PRP:
	    return PTB.IN;
	case PUL:
	    return PTB.PARENTHESIS_OPEN;
	case PUR:
	    return PTB.PARENTHESIS_CLOSE;
	case PUN:
	    return PTB.COMMA;
	case PUQ:
	    return PTB.QUOTATION_OPEN;
	case VBB:
	    return PTB.VBP;
	case VBD:
	    return PTB.VBD;
	case VBG:
	    return PTB.VBG;
	case VBI:
	    return PTB.VB;
	case VBN:
	    return PTB.VBN;
	case VBZ:
	    return PTB.VBZ;
	case VDB:
	    return PTB.VBD;
	case VDD:
	    return PTB.VBD;
	case VDG:
	    return PTB.VBG;
	case VDI:
	    return PTB.VB;
	case VDN:
	    return PTB.VBD;
	case VDZ:
	    return PTB.VBZ;
	case VHB:
	    return PTB.VBP;
	case VHD:
	    return PTB.VBD;
	case VHG:
	    return PTB.VBG;
	case VHI:
	    return PTB.VB;
	case VHN:
	    return PTB.VBN;
	case VHZ:
	    return PTB.VBZ;
	case VM0:
	    return PTB.MD;
	case VVB:
	    return PTB.VB;
	case VVD:
	    return PTB.VBD;
	case VVG:
	    return PTB.VBG;
	case VVI:
	    return PTB.VB;
	case VVZ:
	    return PTB.VBZ;
	case VVN:
	    return PTB.VBN;
	case ITJ:
	    return PTB.UH;
	case TO0:
	    return PTB.TO;
	case UNC:
	    return PTB.FW;
	case XX0:
	    return PTB.RB;
	case ZZ0:
	    return PTB.SYM;
	default:
	    return null;
	}
    }

    @Override
    public String toString() {
	if (this.amiguityTag) {
	    return super.toString() + "-" + this.getSecondChoice().toString();
	}
	return super.toString();
    }

    @Override
    public String toShortString() {
	return this.toString();
    }

    public static BNC fromString(final String s) {
	BNC bncTag = null;

	try {
	    bncTag = BNC.valueOf(s);
	} catch (IllegalArgumentException e) {
	    if (s.contains("-")) {
		String[] p = s.split("-");
		bncTag = BNC.valueOf(p[0]);
		bncTag.amiguityTag = true;
		bncTag.secondChoice = BNC.valueOf(p[1]);
	    }
	}
	return bncTag;
    }

    public boolean isAmbiguous() {
	return this.amiguityTag;
    }

    /**
     * @return the secondChoice
     */
    public BNC getSecondChoice() {
	return secondChoice;
    }

    @Override
    public boolean hasSpaceBefore() {
	switch (this) {
	case PUN:
	    return false;
	default:
	    return true;
	}
    }

    @Override
    public boolean hasSpaceAfter() {
	// TODO: This is not a real implementation
	return true;
    }

}
