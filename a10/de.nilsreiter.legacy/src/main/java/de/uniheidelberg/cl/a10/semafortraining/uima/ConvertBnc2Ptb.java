package de.uniheidelberg.cl.a10.semafortraining.uima;

public class ConvertBnc2Ptb {

	/**
	 * A class to map BNC-PoS-Tags into Penn Treebank-PoS-Tags
	 * 
	 * @param A valid BNC-PoS as string
	 * @return A string of the PTB-PoS corresponding to the given BNC-PoS
	 */

	public static String convertTag(String tag) {
		
		if (tag == null) {return null;} 
		String itag = tag.toUpperCase().intern();
		String otag = null;
		
		if (itag == "AJ0") {otag = "JJ";} //adjective (general or positive) e.g. good, old
		else if (itag == "AJC") {otag = "JJR";} //comparative adjective e.g. better, older
		else if (itag == "AJS") {otag = "JJS";} //superlative adjective, e.g. best, oldest
		else if (itag == "AV0") {otag = "RB";} //adverb (general, not sub-classified as AVP or AVQ), e.g. often, well, longer, furthest.
		else if (itag == "AVP") {otag = "RP";} //adverb particle, e.g. up, off, out.
		else if (itag == "AVQ") {otag = "WRB";} //wh-adverb, e.g. when, how, why, whether the word is used interrogatively or to introduce a relative clause.
		else if (itag == "CJC") {otag = "CC";} //coordinating conjunction, e.g. and, or, but.
		else if (itag == "CJS") {otag = "IN";} //subordinating conjunction, e.g. although, when.
		else if (itag == "CJT") {otag = "IN";} //the subordinating conjunction that, when introducing a relative clause, as in the day that follows Christmas.
		else if (itag == "CRD") {otag = "CD";} //cardinal numeral, e.g. one, 3, fifty-five, 6609.
		else if (itag == "ORD") {otag = "JJ";} //ordinal numeral, e.g. first, sixth, 77th, next, last.
		else if (itag == "AT0") {otag = "DT";} //AT0	article, e.g. the, a, an, no.
		else if (itag == "DPS") {otag = "PRP$";} //possessive determiner form, e.g. your, their, his.
		else if (itag == "DT0") {otag = "DT";} //general determiner: a determiner which is not a DTQ e.g. this both in This is my house and This house is mine.
		else if (itag == "DTQ") {otag = "WDT";} //wh-determiner, e.g. which, what, whose, which, whether used interrogatively or to introduce a relative clause.
		else if (itag == "NN0") {otag = "NN";} //common noun, neutral for number, e.g. aircraft, data, committee.
		else if (itag == "NN1") {otag = "NN";} //singular common noun, e.g. pencil, goose, time, revelation.
		else if (itag == "NN2") {otag = "NNS";} //plural common noun, e.g. pencils, geese, times, revelations.
		else if (itag == "NP0") {otag = "NNP";} //proper noun, e.g. London, Michael, Mars, IBM.
		else if (itag == "PNI") {otag = "PDT";} //indefinite pronoun, e.g. none, everything, one (pronoun), nobody.
		else if (itag == "PNP") {otag = "PRP";} //personal pronoun, e.g. I, you, them, ours. possessive pronouns such as ours and theirs are included in this category.
		else if (itag == "PNQ") {otag = "WP";} //wh-pronoun, e.g. who, whoever, whom.
		else if (itag == "PNX") {otag = "PRP";} //reflexive pronoun, e.g. myself, yourself, itself, ourselves.
		else if (itag == "POS") {otag = "POS";} //the possessive or genitive marker 's or ', tagged as a distinct word.
		else if (itag == "PRF") {otag = "IN";} //the preposition of.
		else if (itag == "PRP") {otag = "IN";} //preposition, other than of, e.g. about, at, in, on behalf of, with. Prepositional phrases like on behalf of or in spite of treated as single words.
		else if (itag == "VBB") {otag = "VBP";} //the present tense forms of the verb be, except for is or 's: am, are 'm, 're, be (subjunctive or imperative), ai (as in ain't).
		else if (itag == "VBD") {otag = "VBD";} //the past tense forms of the verb be: was, were.
		else if (itag == "VBG") {otag = "VBG";} //-ing form of the verb be: being.
		else if (itag == "VBI") {otag = "VB";} //the infinitive form of the verb be: be.
		else if (itag == "VBN") {otag = "VBN";} //the past participle form of the verb be: been
		else if (itag == "VBZ") {otag = "VBZ";} //the -s form of the verb be: is, 's.
		else if (itag == "VDB") {otag = "VBP";} //the finite base form of the verb do: do.
		else if (itag == "VDD") {otag = "VBD";} //the past tense form of the verb do: did.
		else if (itag == "VDG") {otag = "VBG";} //the -ing form of the verb do: doing.
		else if (itag == "VDI") {otag = "VB";} //the infinitive form of the verb do: do.
		else if (itag == "VDN") {otag = "VBD";} //the past participle form of the verb do: done.
		else if (itag == "VDZ") {otag = "VBZ";} //the -s form of the verb do: does.
		else if (itag == "VHB") {otag = "VBP";} //the finite base form of the verb have: have, 've.
		else if (itag == "VHD") {otag = "VBD";} //the past tense form of the verb have: had, 'd.
		else if (itag == "VHG") {otag = "VBG";} //the -ing form of the verb have: having.
		else if (itag == "VHI") {otag = "VB";} //the infinitive form of the verb have: have.
		else if (itag == "VHN") {otag = "VBN";} //the past participle form of the verb have: had.
		else if (itag == "VHZ") {otag = "VBZ";} //the -s form of the verb have: has, 's.
		else if (itag == "VM0") {otag = "MD";} //modal auxiliary verb, e.g. can, could, will, 'll, 'd, wo (as in won't)
		else if (itag == "VVB") {otag = "VB";} //the finite base form of lexical verbs, e.g. forget, send, live, return. This tag is used for imperatives and the present subjunctive forms, but not for the infinitive (VVI).
		else if (itag == "VVD") {otag = "VBD";} //the past tense form of lexical verbs, e.g. forgot, sent, lived, returned.
		else if (itag == "VVG") {otag = "VBG";} //the -ing form of lexical verbs, e.g. forgetting, sending, living, returning.
		else if (itag == "VVI") {otag = "VB";} //the infinitive form of lexical verbs , e.g. forget, send, live, return.
		else if (itag == "VVN") {otag = "VBN";} //the past participle form of lexical verbs, e.g. forgotten, sent, lived, returned.
		else if (itag == "VVZ") {otag = "VBZ";} //the -s form of lexical verbs, e.g. forgets, sends, lives, returns.
		else if (itag == "EX0") {otag = "EX";} //existential there, the word there appearing in the constructions there is..., there are ....
		else if (itag == "ITJ") {otag = "UH";} //interjection or other isolate, e.g. oh, yes, mhm, wow.
		else if (itag == "TO0") {otag = "TO";} //the infinitive marker to.
		else if (itag == "UNC") {otag = "FW";} //unclassified items which are not appropriately classified as items of the English lexicon.
		else if (itag == "XX0") {otag = "RB"; } //the negative particle not or n't.
		else if (itag == "ZZ0") {otag = "SYM";} //alphabetical symbols, e.g. A, a, B, b, c, d.

		return otag;
	}
}