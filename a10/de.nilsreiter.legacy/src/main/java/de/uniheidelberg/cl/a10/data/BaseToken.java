package de.uniheidelberg.cl.a10.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.uniheidelberg.cl.a10.parser.dep.IDependency;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import edu.stanford.nlp.ling.HasWord;

public class BaseToken implements HasWord, IHasPartOfSpeech, IHasDependency,
		Comparable<BaseToken> {
	private static final long serialVersionUID = 1l;

	String word;

	BaseSentence sentence = null;

	SortedSet<BaseToken> dependents = null;

	IPartOfSpeech partOfSpeech = null;

	Token token;

	BaseToken governor = null;

	int governorNumber = Integer.MIN_VALUE;

	IDependency dependencyRelation;

	Class<? extends IPartOfSpeech> partOfSpeechStyle = null;
	Class<? extends IDependency> dependencyStyle = null;

	int begin;
	int end;

	public Map<String, Object> data = new HashMap<String, Object>();

	public BaseToken() {

	};

	public BaseToken(final BaseSentence bs) {
		this.sentence = bs;
	};

	public BaseToken(final String word) {
		this.word = word;
	}

	public BaseToken(final BaseSentence bs, final String word) {
		this.sentence = bs;
		this.word = word;
	}

	public BaseToken(final String word, final IPartOfSpeech partOfSpeech,
			final String lemma) {
		this.word = word;
		this.partOfSpeech = partOfSpeech;
		this.data.put(Constants.DATA_KEY_LEMMA, lemma);
		this.partOfSpeechStyle = partOfSpeech.getClass();
	}

	@Override
	public void setWord(final String word) {
		this.word = word;
	}

	@Override
	public String word() {
		return this.word;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(final Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return this.getWord() + "/" + this.getPartOfSpeech() + "/"
				+ this.getLemma();
	}

	/**
	 * @return the lemma
	 */
	public String getLemma() {
		return (String) this.data.get(Constants.DATA_KEY_LEMMA);
	}

	/**
	 * @param lemma
	 *            the lemma to set
	 */
	public void setLemma(final String lemma) {
		this.data.put(Constants.DATA_KEY_LEMMA, lemma);
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @return the governor
	 */
	public BaseToken getGovernor() {
		if (this.governor == null && this.governorNumber != Integer.MIN_VALUE
				&& this.sentence != null) {
			if (this.governorNumber < 0) {
				this.governor = null;
				this.governorNumber = Integer.MIN_VALUE;
			} else {
				this.governor = this.sentence.get(this.governorNumber);
				this.governorNumber = Integer.MIN_VALUE;

			}
		}
		return governor;
	}

	/**
	 * @param governor
	 *            the governor to set
	 */
	public void setGovernor(final BaseToken governor) {

		if (this.getDependencyStyle() == null) {
			this.governor = governor;
			this.dependencyStyle =
					(governor != null ? governor.getDependencyStyle() : null);
		}
		if (governor == null
				|| governor.getDependencyStyle() == this.getDependencyStyle()) {
			this.governor = governor;
		}
	}

	public void setGovernor(final int governorNumber) {
		this.governorNumber = governorNumber;
	}

	/**
	 * @return the dependencyRelation
	 */
	@Override
	public IDependency getDependencyRelation() {
		return dependencyRelation;
	}

	/**
	 * @param dependencyRelation
	 *            the dependencyRelation to set
	 */
	public void setDependencyRelation(final IDependency dependencyRelation) {
		this.dependencyRelation = dependencyRelation;
	}

	protected boolean reachingRoot(final BaseSentence sentence) {
		BaseToken gov = this.getGovernor();
		while (gov != null) {
			gov = gov.getGovernor();
		}
		return (gov == sentence.getRoot());
	}

	public BaseToken deepCopy() {

		BaseToken tok = new BaseToken();
		tok.word = this.word;
		tok.partOfSpeech = this.partOfSpeech;
		for (String key : this.data.keySet()) {
			tok.data.put(key, this.data.get(key));
		}
		tok.dependencyStyle = this.dependencyStyle;
		tok.partOfSpeechStyle = this.partOfSpeechStyle;
		tok.governor = this.governor;
		tok.dependencyRelation = dependencyRelation;
		tok.token = token;
		return tok;
	}

	/**
	 * @return the partOfSpeech
	 */
	@Override
	public IPartOfSpeech getPartOfSpeech() {
		return partOfSpeech;
	}

	/**
	 * @param pos2
	 *            the partOfSpeech to set
	 */
	public void setPartOfSpeech(final IPartOfSpeech pos2) {
		if (this.getPartOfSpeechStyle() == null) {
			this.partOfSpeech = pos2;
			if (pos2 != null) {
				this.partOfSpeechStyle = pos2.getClass();
			}
			return;
		}
		if (pos2 != null && this.getPartOfSpeechStyle() == pos2.getClass()) {
			this.partOfSpeech = pos2;
		}
	}

	/**
	 * @return the partOfSpeechStyle
	 */
	@Override
	public Class<? extends IPartOfSpeech> getPartOfSpeechStyle() {
		return partOfSpeechStyle;
	}

	/**
	 * @return the dependencyStyle
	 */
	@Override
	public Class<? extends IDependency> getDependencyStyle() {
		return dependencyStyle;
	}

	@Override
	public int compareTo(final BaseToken arg0) {
		return 0;
	}

	/**
	 * @return the sentence
	 */
	public BaseSentence getSentence() {
		return sentence;
	}

	/**
	 * @param sentence
	 *            the sentence to set
	 */
	public void setSentence(final BaseSentence sentence) {
		this.sentence = sentence;
	}

	/**
	 * @return the dependents
	 */
	public SortedSet<BaseToken> getDependents() {
		if (sentence == null) return null;
		if (dependents == null) {
			dependents = new TreeSet<BaseToken>(new Comparator<BaseToken>() {

				@Override
				public int compare(final BaseToken arg0, final BaseToken arg1) {
					return Integer.valueOf(arg0.getSentence().indexOf(arg0))
							.compareTo(arg0.getSentence().indexOf(arg1));
				}
			});
			for (BaseToken token : this.getSentence()) {
				if (token.getGovernor() == this) {
					dependents.add(token);
				}
			}
		}
		return dependents;
	}

	public SortedSet<BaseToken> getRecursiveDependents() {
		if (sentence == null) return null;
		SortedSet<BaseToken> dep =
				new TreeSet<BaseToken>(new Comparator<BaseToken>() {
					@Override
					public int compare(final BaseToken arg0,
							final BaseToken arg1) {
						return Integer
								.valueOf(arg0.getSentence().indexOf(arg0))
								.compareTo(arg0.getSentence().indexOf(arg1));
					}
				});
		for (BaseToken token : this.getSentence()) {
			if (token.getGovernor() == this) {
				dep.add(token);
				dep.addAll(token.getRecursiveDependents());
			}
		}
		return dep;
	}

	public BaseToken getDependent(final IDependency relation) {
		for (BaseToken tok : this.getDependents()) {
			if (tok.getDependencyRelation() == relation) return tok;
		}
		return null;
	}

	public String getExpansion() {
		SortedSet<BaseToken> t = this.getDependents();
		t.add(this);
		StringBuilder b = new StringBuilder();
		for (BaseToken bt : t) {
			b.append(bt.getLemma());
			b.append(' ');
		}
		return b.toString();
	}

	/**
	 * @return the begin
	 */
	public int getBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(final int begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(final int end) {
		this.end = end;
	}

	public boolean convertPartOfSpeech(final PartOfSpeechConverter converter) {
		if (this.getPartOfSpeechStyle() == converter.from()) {
			this.partOfSpeechStyle = converter.to();
			this.setPartOfSpeech(converter.convert(getPartOfSpeech()));
			return true;
		}
		return false;
	}

}