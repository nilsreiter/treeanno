package de.uniheidelberg.cl.a10.parser.dep;

public enum StanfordDependency implements IDependency {
	/**
	 * root
	 */
	ROOT, /** dependent */
	DEP, /** auxiliary */
	AUX, /** passive auxiliary */
	AUXPASS, /** copula */
	COP, /** argument */
	ARG, /** agent */
	AGENT, /** complement */
	COMP,
	/** adjectival complement */
	ACOMP,
	/** attributive */
	ATTR,
	/** clausal complement with internal subject */
	CCOMP,
	/** clausal complement with external subject */
	XCOMP,
	/** prepositional complement */
	PCOMP,
	/** complementizer */
	COMPLM,
	/** object */
	OBJ,
	/** direct object */
	DOBJ,
	/** indirect object */
	IOBJ,
	/** object of preposition */
	POBJ,
	/** marker (word introducing an advcl) */
	MARK,
	/** relative (word introducing a rcmod) */
	REL,
	/** subject */
	SUBJ,
	/** nominal subject */
	NSUBJ,
	/** passive nominal subject */
	NSUBJPASS,
	/** clausal subject */
	CSUBJ,
	/** passive clausal subject */
	CSUBJPASS,
	/** coordination */
	CC,
	/** conjunct */
	CONJ,
	/** expletive (expletive “there”) */
	EXPL,
	/** modifier */
	MOD,
	/** abbreviation modifier */
	ABBREV,
	/** adjectival modifier */
	AMOD,
	/** appositional modifier */
	APPOS,
	/** adverbial clause modifier */
	ADVCL,
	/** purpose clause modifier */
	PURPCL,
	/** determiner */
	DET,
	/** predeterminer */
	PREDET,
	/** preconjunct */
	PRECONJ,
	/** infinitival modifier */
	INFMOD,
	/** multi-word expression modifier */
	MWE,
	/** participial modifier */
	PARTMOD,
	/** adverbial modifier */
	ADVMOD,
	/** negation modifier */
	NEG,
	/** relative clause modifier */
	RCMOD,
	/** quantifier modifier */
	QUANTMOD,
	/** noun compound modifier */
	NN,
	/** noun phrase adverbial modifier */
	NPADVMOD,
	/** temporal modifier */
	TMOD,
	/** numeric modifier */
	NUM,
	/** element of compound number */
	NUMBER,
	/** prepositional modifier */
	PREP,
	/** possession modifier */
	POSS,
	/** possessive modifier (’s) */
	POSSESSIVE,
	/** phrasal verb particle */
	PRT,
	/** parataxis */
	PARATAXIS,
	/** punctuation */
	PUNCT,
	/** referent */
	REF,
	/** semantic dependent */
	SDEP,
	/** controlling subject */
	XSUBJ,
	/**
	 * link between a subject and its predicate, e.g. 'Reagan died' -
	 * pred(Reagan,died)
	 */
	PRED,
	/**
	 * link from an adjective to a measure word, e.g. 'he is 65 years old' -
	 * measure(old,years)
	 */
	MEASURE;

	@Override
	public IDependency getParent() {
		switch (this) {
		case AUX:
		case ARG:
		case CC:
		case CONJ:
		case EXPL:
		case MOD:
		case PARATAXIS:
		case PUNCT:
		case REF:
		case SDEP:
		case PRED:
			return DEP;
		case XSUBJ:
			return SDEP;
		case AUXPASS:
		case COP:
			return AUX;
		case AGENT:
		case COMP:
		case SUBJ:
			return ARG;
		case ACOMP:
		case ATTR:
		case CCOMP:
		case XCOMP:
		case COMPLM:
		case OBJ:
		case MARK:
		case REL:
		case PCOMP:
			return COMP;
		case NSUBJ:
		case CSUBJ:
			return SUBJ;
		case NSUBJPASS:
			return NSUBJ;
		case CSUBJPASS:
			return CSUBJ;
		case DOBJ:
		case IOBJ:
		case POBJ:
			return OBJ;
		default:
			return MOD;
		}
	}

	@Override
	public boolean isRootRelation() {
		return this == ROOT;
	}

	public static StanfordDependency fromString(final String s) {
		return StanfordDependency.valueOf(s.toUpperCase());
	}

	@Override
	public IDependency[] getCategories() {
		return values();
	}

}
