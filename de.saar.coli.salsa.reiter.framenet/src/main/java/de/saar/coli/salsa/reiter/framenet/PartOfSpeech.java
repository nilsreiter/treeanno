/**
 * 
 * Copyright 2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */

package de.saar.coli.salsa.reiter.framenet;

/**
 * This enum is used to represent the different parts of speech appearing in the
 * FrameNet corpus.
 * 
 * @author reiter
 * @since 0.4.1
 * @deprecated Use de.uniheidelberg.cl.reiter.pos.IPartOfSpeech instead.
 */
@Deprecated
public enum PartOfSpeech {
    /**
     * 
     */
    Noun, Verb, Adjective, Adverb, Determiner, Preposition, Pronoun, Interjection, Numeral, SCON, Conjunction, Other;

    /**
     * Maps the string-representation of a part of speech onto the enum type.
     * 
     * @param pos
     *            The string representation of the pos
     * @return The enum type
     * @deprecated
     */
    @Deprecated
    public static PartOfSpeech getPartOfSpeech(final String pos) {
	if (pos.startsWith("V") || pos.startsWith("v")) {
	    return Verb;
	}
	if (pos.startsWith("N") || pos.startsWith("n")) {
	    return Noun;
	}
	if (pos.equals("A")) {
	    return Adjective;
	}
	if (pos.equals("ADV")) {
	    return Adverb;
	}
	if (pos.equals("ART")) {
	    return Determiner;
	}
	if (pos.equals("PREP")) {
	    return Preposition;
	}
	if (pos.equals("INTJ")) {
	    return Interjection;
	}
	if (pos.equals("PRON")) {
	    return Pronoun;
	}
	if (pos.equals("C")) {
	    return Conjunction;
	}
	if (pos.equals("SCON")) {
	    return SCON;
	}
	if (pos.equals("Num")) {
	    return Numeral;
	}
	System.err.println(pos);
	return null;
    }

    /**
     * Partial mapping from Penn TreeBank pos-tags onto PartOfSpeech objects.
     * 
     * @param ptbPos
     *            The pos tag from PTB
     * @return A PartOfSpeech object
     */
    public static PartOfSpeech fromPTB(final String ptbPos) {
	if (ptbPos == null) {
	    return Other;
	}
	String tag = ptbPos.toUpperCase().intern();
	if (tag.startsWith("N")) {
	    return Noun;
	}
	if (tag.startsWith("V")) {
	    return Verb;
	}
	if (tag.startsWith("RB") || tag == "WRB") {
	    return Adverb;
	}
	if (tag.startsWith("J")) {
	    return Adjective;
	}
	if (tag == "DT" || tag == "WDT") {
	    return Determiner;
	}
	if (tag == "IN" || tag == "TO") {
	    return Preposition;
	}
	if (tag.startsWith("PR")) {
	    return Pronoun;
	}

	return Other;
    }

    /**
     * Returns a PartOfSpeech object from pos tags in BNC style.
     * 
     * @param bncPos
     *            The pos tag
     * @return a PartOfSpeech object
     */
    public static PartOfSpeech fromBNC(final String bncPos) {
	String itag = bncPos.toUpperCase().intern();

	if (itag == "AJ0" || itag == "AJC" || itag == "AJS") {
	    return Adjective;
	} else if (itag == "AV0") {
	    return Adverb;
	} else if (itag == "AVP") {
	    return Other; // adverb particle, e.g. up, off, out.
	} else if (itag == "AVQ") {
	    return Adverb; // wh-adverb, e.g. when, how, why, whether the word
			   // is used
	    // interrogatively or to introduce a relative clause.
	} else if (itag == "CJC") {
	    return Conjunction; // coordinating conjunction, e.g. and, or, but.
	} else if (itag == "CJS") {
	    return Conjunction; // subordinating conjunction, e.g. although,
				// when.
	} else if (itag == "CJT") {
	    return Conjunction; // the subordinating conjunction that, when
				// introducing a relative
	    // clause, as in the day that follows Christmas.
	} else if (itag == "CRD") {
	    return Numeral; // cardinal numeral, e.g. one, 3, fifty-five, 6609.
	} else if (itag == "ORD") {
	    return Adjective; // ordinal numeral, e.g. first, sixth, 77th, next,
			      // last.
	} else if (itag == "AT0") {
	    return Determiner; // AT0 article, e.g. the, a, an, no.
	} else if (itag == "DPS") {
	    return Determiner; // possessive determiner form, e.g. your, their,
			       // his.
	} else if (itag == "DT0") {
	    return Determiner; // general determiner: a determiner which is not
			       // a DTQ e.g. this both
	    // in This is my house and This house is mine.
	} else if (itag == "DTQ") {
	    return Determiner; // wh-determiner, e.g. which, what, whose, which,
			       // whether used
	    // interrogatively or to introduce a relative clause.
	} else if (itag == "NN0") {
	    return Noun; // common noun, neutral for number, e.g. aircraft,
			 // data, committee.
	} else if (itag == "NN1") {
	    return Noun; // singular common noun, e.g. pencil, goose, time,
			 // revelation.
	} else if (itag == "NN2") {
	    return Noun; // plural common noun, e.g. pencils, geese, times,
			 // revelations.
	} else if (itag == "NP0") {
	    return Other; // proper noun, e.g. London, Michael, Mars, IBM.
	} else if (itag == "PNI") {
	    return Pronoun; // indefinite pronoun, e.g. none, everything, one
			    // (pronoun), nobody.
	} else if (itag == "PNP") {
	    return Pronoun; // personal pronoun, e.g. I, you, them, ours.
			    // possessive pronouns such
	    // as ours and theirs are included in this category.
	} else if (itag == "PNQ") {
	    return Pronoun; // wh-pronoun, e.g. who, whoever, whom.
	} else if (itag == "PNX") {
	    return Pronoun; // reflexive pronoun, e.g. myself, yourself, itself,
			    // ourselves.
	} else if (itag == "POS") {
	    return Other; // the possessive or genitive marker 's or ', tagged
			  // as a distinct
	    // word.
	} else if (itag == "PRF") {
	    return Preposition; // the preposition of.
	} else if (itag == "PRP") {
	    return Preposition; // preposition, other than of, e.g. about, at,
				// in, on behalf of, with.
	    // Prepositional phrases like on behalf of or in spite of treated as
	    // single words.
	} else if (itag == "VBB") {
	    return Verb; // the present tense forms of the verb be, except for
			 // is or 's: am,
			 // are 'm, 're, be (subjunctive or imperative), ai (as
			 // in ain't).
	} else if (itag == "VBD") {
	    return Verb; // the past tense forms of the verb be: was, were.
	} else if (itag == "VBG") {
	    return Verb; // -ing form of the verb be: being.
	} else if (itag == "VBI") {
	    return Verb; // the infinitive form of the verb be: be.
	} else if (itag == "VBN") {
	    return Verb; // the past participle form of the verb be: been
	} else if (itag == "VBZ") {
	    return Verb; // the -s form of the verb be: is, 's.
	} else if (itag == "VDB") {
	    return Verb; // the finite base form of the verb do: do.
	} else if (itag == "VDD") {
	    return Verb; // the past tense form of the verb do: did.
	} else if (itag == "VDG") {
	    return Verb; // the -ing form of the verb do: doing.
	} else if (itag == "VDI") {
	    return Verb; // the infinitive form of the verb do: do.
	} else if (itag == "VDN") {
	    return Verb; // the past participle form of the verb do: done.
	} else if (itag == "VDZ") {
	    return Verb; // the -s form of the verb do: does.
	} else if (itag == "VHB") {
	    return Verb; // the finite base form of the verb have: have, 've.
	} else if (itag == "VHD") {
	    return Verb; // the past tense form of the verb have: had, 'd.
	} else if (itag == "VHG") {
	    return Verb; // the -ing form of the verb have: having.
	} else if (itag == "VHI") {
	    return Verb; // the infinitive form of the verb have: have.
	} else if (itag == "VHN") {
	    return Verb; // the past participle form of the verb have: had.
	} else if (itag == "VHZ") {
	    return Verb; // the -s form of the verb have: has, 's.
	} else if (itag == "VM0") {
	    return Verb; // modal auxiliary verb, e.g. can, could, will, 'll,
			 // 'd, wo (as in
			 // won't)
	} else if (itag == "VVB") {
	    return Verb; // the finite base form of lexical verbs, e.g. forget,
			 // send, live,
			 // return. This tag is used for imperatives and the
			 // present
			 // subjunctive forms, but not for the infinitive (VVI).
	} else if (itag == "VVD") {
	    return Verb; // the past tense form of lexical verbs, e.g. forgot,
			 // sent, lived,
			 // returned.
	} else if (itag == "VVG") {
	    return Verb; // the -ing form of lexical verbs, e.g. forgetting,
			 // sending, living,
			 // returning.
	} else if (itag == "VVI") {
	    return Verb; // the infinitive form of lexical verbs , e.g. forget,
			 // send, live,
			 // return.
	} else if (itag == "VVN") {
	    return Verb; // the past participle form of lexical verbs, e.g.
			 // forgotten, sent,
			 // lived, returned.
	} else if (itag == "VVZ") {
	    return Verb; // the -s form of lexical verbs, e.g. forgets, sends,
			 // lives, returns.
	} else if (itag == "EX0") {
	    return Determiner; // existential there, the word there appearing in
			       // the constructions
	    // there is..., there are ....
	} else if (itag == "ITJ") {
	    return Interjection; // interjection or other isolate, e.g. oh, yes,
				 // mhm, wow.
	} else if (itag == "TO0") {
	    return Other; // the infinitive marker to.
	} else if (itag == "UNC") {
	    return Other; // unclassified items which are not appropriately
			  // classified as items
	    // of the English lexicon.
	} else if (itag == "XX0") {
	    return Other; // the negative particle not or n't.
	} else if (itag == "ZZ0") {
	    return Other;
	} //
	return Other;
    }

    @Override
    public String toString() {
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
	    return "v"; // TODO: This is a hack, because Semafor can't handle
			// pronouns (and FrameNet doesn't use them)
	case Other:
	default:
	    return "v";
	}
    }

    /**
     * Maps the part of speech information given in FrameNet to PartOfSpeech
     * objects.
     * 
     * @param itag
     *            The pos information
     * @return An object describing the POS
     */
    public static PartOfSpeech fromFN(final String itag) {
	String tag = itag.toUpperCase().intern();
	if (tag == "N") {
	    return Noun;
	}
	if (tag == "A") {
	    return Adjective;
	}
	if (tag == "ADV") {
	    return Adverb;
	}
	if (tag == "C") {
	    return Conjunction;
	}
	if (tag == "ART") {
	    return Determiner;
	}
	if (tag == "INTJ") {
	    return Interjection;
	}
	if (tag == "NUM") {
	    return Numeral;
	}
	if (tag == "PREP") {
	    return Preposition;
	}
	if (tag == "PR") {
	    return Pronoun;
	}
	if (tag == "SCON") {
	    return SCON;
	}
	if (tag == "V") {
	    return Verb;
	}
	return Other;
    }
}
