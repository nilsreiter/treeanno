package de.uniheidelberg.cl.a10.data;

import is2.data.SentenceData09;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DirectedMultigraph;

import de.uniheidelberg.cl.a10.parser.dep.IDependency;
import de.uniheidelberg.cl.a10.parser.dep.StanfordDependency;
import de.uniheidelberg.cl.a10.semafortraining.uima.SemaforSentence;
import de.uniheidelberg.cl.reiter.pos.BNC;
import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;
import de.uniheidelberg.cl.reiter.pos.PTB;
import edu.stanford.nlp.ling.WordTag;
import edu.stanford.nlp.process.Morphology;

public class BaseSentence implements IHasDependencyStyle,
		IHasPartOfSpeechStyle, List<BaseToken>, Comparable<BaseSentence> {

	List<BaseToken> list;

	Class<? extends IPartOfSpeech> partOfSpeechStyle = null;
	Class<? extends IDependency> dependencyStyle = null;
	public Map<String, String> data = null;

	public BaseSentence() {
		this.init();
	}

	public BaseSentence(final Class<? extends IPartOfSpeech> class1) {
		this.partOfSpeechStyle = class1;
		this.init();
	}

	private void init() {
		list = new LinkedList<BaseToken>();
		data = new HashMap<String, String>();
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (BaseToken pt : this) {
			buf.append(pt.toString());
			buf.append('/');
			buf.append(pt.getDependencyRelation());
			buf.append('/');
			buf.append(pt.getGovernor());
			buf.append('\n');
		}
		return buf.toString();
	};

	public String toTokenizedString() {
		StringBuffer buf = new StringBuffer();
		for (BaseToken pt : this) {
			buf.append(pt.toString());
			buf.append(' ');
		}
		return buf.toString().trim();
	}

	public String toSurfaceString() {
		StringBuffer buf = new StringBuffer();
		boolean firstToken = true;
		for (BaseToken pt : this) {
			if (pt.getPartOfSpeech().hasSpaceBefore() && !firstToken) {
				buf.append(' ');
			}
			pt.setBegin(buf.length());
			pt.setEnd(buf.length() + pt.getWord().length());

			buf.append(pt.getWord());
			firstToken = false;
		}
		return buf.toString();
	}

	public BaseToken getRoot() {
		for (BaseToken pt : this) {
			if (pt.getGovernor() == null)

				return pt;
		}
		return null;
	}

	protected int numberOfRoots() {
		int i = 0;
		for (BaseToken pt : this) {
			if (pt.getGovernor() == null) i++;
		}
		return i;
	}

	public boolean isSane() {
		boolean sane = true;
		sane = sane && (numberOfRoots() == 1);
		for (BaseToken bt : this) {
			sane =
					sane
					&& (bt.getPartOfSpeechStyle() == this
					.getPartOfSpeechStyle());
			sane = sane && (this.checkConsistency(bt.getDependencyStyle()));
		}
		return sane;
	}

	public SemaforSentence getSemaforSentence(final int sentenceNumber) {
		SemaforSentence ret = new SemaforSentence(sentenceNumber);

		for (int i = 0; i < this.size(); i++) {
			// for (BaseToken pt : this) {
			BaseToken pt = this.get(i);
			IPartOfSpeech bPos = pt.getPartOfSpeech();
			PTB ptb;
			if (pt.getPartOfSpeechStyle() == BNC.class) {
				ptb = ((BNC) bPos).asPTB();
			} else {
				ptb = (PTB) bPos;
			}
			ret.addToken(pt.word(), ptb, pt.getLemma());
		}

		for (int i = 0; i < this.size(); i++) {
			BaseToken pt = get(i);
			if (pt.getDependencyRelation() != null) {
				ret.setDependency(i,
						(StanfordDependency) pt.getDependencyRelation(),
						indexOf(pt.getGovernor()));
			} else {
				ret.setDependency(i, null, -1);
			}
		}
		return ret;
	}

	public SentenceData09 getSentenceData09() {
		String[] forms = new String[this.size() + 1];
		String[] ppos = new String[this.size() + 1];
		String[] lemmas = new String[this.size() + 1];

		boolean dep = false;
		String[] deprel = new String[this.size() + 1];
		int[] governor = new int[this.size() + 1];
		for (int i = 0; i < this.size(); i++) {
			BaseToken token = this.get(i);
			forms[i + 1] = this.get(i).getWord();
			ppos[i + 1] = this.get(i).getPartOfSpeech().toString();
			lemmas[i + 1] = this.get(i).getLemma();
			if (token.getDependencyRelation() != null) {
				deprel[i + 1] = token.getDependencyRelation().toString();
				dep = true;
			} else {
				deprel[i + 1] = "root";
			}
			if (token.getGovernor() != null) {
				governor[i + 1] = this.indexOf(token.getGovernor()) + 1;
			} else {
				governor[i + 1] = 0;
			}
		}

		forms[0] = "<root>";
		ppos[0] = "<root-POS>";
		if (dep) {
			governor[0] = 0;
			deprel[0] = "root";
		}
		SentenceData09 sentence_input = new SentenceData09();
		sentence_input.init(forms);
		sentence_input.gpos = ppos;
		sentence_input.ppos = ppos;
		sentence_input.plemmas = lemmas;
		sentence_input.lemmas = lemmas;
		if (dep) {
			sentence_input.labels = deprel;
			sentence_input.plabels = deprel;
			sentence_input.heads = governor;
			sentence_input.pheads = governor;
		}
		return sentence_input;

	}

	public BaseSentence deepCopy() {
		BaseSentence ret = new BaseSentence();
		ret.dependencyStyle = this.dependencyStyle;
		ret.partOfSpeechStyle = this.partOfSpeechStyle;
		for (BaseToken tok : this) {
			ret.add(tok.deepCopy());
		}

		for (int i = 0; i < ret.size(); i++) {
			BaseToken token = ret.get(i);
			int tokenNumber = this.indexOf(this.get(i).getGovernor());
			token.setGovernor((tokenNumber < 0 ? null : ret.get(tokenNumber)));
		}
		return ret;
	}

	public void addLemmas(final Morphology morphology) {
		for (BaseToken pt : this) {
			pt.setLemma(morphology.lemmatize(
					new WordTag(pt.getWord(), pt.getPartOfSpeech()
							.toShortString())).lemma());
		}
	}

	public boolean addDependency(final int dep, final int gov,
			final IDependency relation) {
		if (this.checkConsistency(relation.getClass())) {
			if (gov >= 0) {
				this.get(dep).setGovernor(this.get(gov));
			}
			this.get(dep).setDependencyRelation(relation);
			return true;
		}
		return false;
	}

	public boolean hasDependencyAnnotation() {
		return this.numberOfRoots() == 1;
	}

	/**
	 * This method checks whether the token to be added is compatible with the
	 * existing pos-annotation style of the sentence. If this is the case, the
	 * token is added. If not, we return false.
	 */
	@Override
	public boolean add(final BaseToken token) {
		if (this.checkConsistency(token)) {
			list.add(token);
			return true;
		}
		return false;

	}

	protected boolean checkConsistency(
			final Class<? extends IDependency> dependencyRelationStyle) {
		if (dependencyRelationStyle == null) return true;
		if (this.dependencyStyle == null) {
			this.dependencyStyle = dependencyRelationStyle;
			return true;
		}
		if (this.dependencyStyle == dependencyRelationStyle) {
			return true;
		}
		return false;
	}

	protected boolean checkConsistency(final BaseToken token) {
		if (this.partOfSpeechStyle == null) {
			this.partOfSpeechStyle = token.getPartOfSpeechStyle();
			return true;
		}
		if (this.partOfSpeechStyle == token.getPartOfSpeechStyle()) {
			return true;
		}
		return false;
	}

	@Override
	public Class<? extends IPartOfSpeech> getPartOfSpeechStyle() {
		return partOfSpeechStyle;
	}

	@Override
	public Class<? extends IDependency> getDependencyStyle() {
		return dependencyStyle;
	}

	protected Set<BaseToken> getDependents(final BaseToken bt) {
		Set<BaseToken> ret = new HashSet<BaseToken>();
		for (BaseToken token : this) {
			if (token.getGovernor() == bt) {
				ret.add(token);
			}
		}
		return ret;
	}

	/**
	 * This method sorts the sentence according to dataKey. Usually, dataKey is
	 * used as a key in the data-map, but several special keys are defined:
	 * <ul>
	 * <li><tt>uimaId</tt>: The value is retrieved from the data map, but
	 * splitted according to the meaning of the uima ids.</li>
	 * <li><tt>partOfSpeech</tt>: We use the partOfSpeech-field of the tokens.</li>
	 * </ul>
	 * 
	 * @param dataKey
	 * @param numeric
	 */
	public void sort(final String dataKey, final boolean numeric) {
		Collections.sort(this.list, new Comparator<BaseToken>() {

			@Override
			public int compare(final BaseToken arg0, final BaseToken arg1) {
				String v1 = (String) arg0.data.get(dataKey);
				String v2 = (String) arg1.data.get(dataKey);

				if (dataKey.equals("uimaId")) {
					v1 = (v1).split("_")[3];
					v2 = (v2).split("_")[3];
				} else if (dataKey.equals(Constants.DATA_KEY_PARTOFSPEECH)) {
					v1 = arg0.getPartOfSpeech().toString();
					v2 = arg1.getPartOfSpeech().toString();
				}

				if (numeric) {
					Integer i1 = Integer.valueOf(v1);
					Integer i2 = Integer.valueOf(v2);
					return i1.compareTo(i2);
				}
				return v1.compareTo(v2);
			}

		});
	}

	public static <D extends IPartOfSpeech> BaseSentence newSentence(
			final BaseToken... tokens) {
		if (tokens.length > 0) {
			BaseSentence ret = new BaseSentence();
			for (int i = 0; i < tokens.length; i++) {
				ret.add(tokens[i]);
				tokens[i].setSentence(ret);
			}
			return ret;
		}
		return null;

	}

	public boolean convertPartOfSpeech(final PartOfSpeechConverter converter) {
		if (this.getPartOfSpeechStyle() != converter.from()) return false;
		for (BaseToken token : this) {
			token.convertPartOfSpeech(converter);
		}
		return true;
	}

	/**
	 * If arg1 does not match the part of speech style of the sentence, it won't
	 * be added.
	 */
	@Override
	public void add(final int arg0, final BaseToken arg1) {
		if (this.checkConsistency(arg1)) {
			list.add(arg0, arg1);
		}
	}

	@Override
	public boolean addAll(final Collection<? extends BaseToken> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(final int arg0,
			final Collection<? extends BaseToken> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		list.clear();
		this.partOfSpeechStyle = null;
		this.dependencyStyle = null;
	}

	@Override
	public boolean contains(final Object arg0) {
		return list.contains(arg0);
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		return list.containsAll(arg0);
	}

	@Override
	public BaseToken get(final int arg0) {
		return list.get(arg0);
	}

	@Override
	public int indexOf(final Object arg0) {
		return list.indexOf(arg0);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<BaseToken> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(final Object arg0) {
		return list.lastIndexOf(arg0);
	}

	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator<BaseToken> listIterator() {
		return list.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator<BaseToken> listIterator(final int index) {
		return list.listIterator(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	@Override
	public BaseToken remove(final int index) {
		return list.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object o) {
		return list.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(final Collection<?> c) {
		return list.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(final Collection<?> c) {
		return list.retainAll(c);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	@Override
	public BaseToken set(final int index, final BaseToken element) {
		if (this.checkConsistency(element)) {
			return list.set(index, element);
		}
		return null;
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	@Override
	public int size() {
		return list.size();
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public List<BaseToken> subList(final int fromIndex, final int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return list.toArray(a);
	}

	public String[] toWordArray() {
		String[] ret = new String[this.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = this.get(i).getWord();
		}
		return ret;
	}

	public DirectedMultigraph<BaseToken, DependencyEdge> getDependencyGraph() {
		this.enumerateTokens();
		DirectedMultigraph<BaseToken, DependencyEdge> ret =
				new DirectedMultigraph<BaseToken, DependencyEdge>(
						DependencyEdge.class);
		for (BaseToken bt : this) {
			ret.addVertex(bt);
		}
		for (BaseToken bt : this) {
			if (bt.getGovernor() != null) {
				ret.addEdge(bt, bt.getGovernor(),
						new DependencyEdge(bt.getDependencyRelation()));
			}
		}
		return ret;
	}

	public void enumerateTokens() {
		for (int i = 0; i < this.size(); i++) {
			this.get(i).data.put(Constants.DATA_KEY_ID, String.valueOf(i));
		}
	}

	public static DOTExporter<BaseToken, DependencyEdge> getDOTExporter() {

		DOTExporter<BaseToken, DependencyEdge> ret =
				new DOTExporter<BaseToken, DependencyEdge>(
						new VertexNameProvider<BaseToken>() {

							@Override
							public String getVertexName(final BaseToken arg0) {
								return "word_"
										+ arg0.data.get(Constants.DATA_KEY_ID);
							}

						}, new VertexNameProvider<BaseToken>() {

							@Override
							public String getVertexName(final BaseToken arg0) {
								return arg0.data.get(Constants.DATA_KEY_ID)
										+ ": " + arg0.toString();
							}
						}, new EdgeNameProvider<DependencyEdge>() {

							@Override
							public String
							getEdgeName(final DependencyEdge arg0) {
								return arg0.toString();
							}
						});

		return ret;
	}

	@Override
	public int compareTo(final BaseSentence arg0) {
		return 0;
	}

	public static class DependencyEdge {
		IDependency dependency;
		String label = null;

		public DependencyEdge(final IDependency dep) {
			this.dependency = dep;
		}

		public DependencyEdge(final IDependency dep, final String lab) {
			this.dependency = dep;
			this.label = lab;
		}

		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * @param label
		 *            the label to set
		 */
		public void setLabel(final String label) {
			this.label = label;
		}

		/**
		 * @return the dependency
		 */
		public IDependency getDependency() {
			return dependency;
		}

		@Override
		public String toString() {
			if (label == null) return this.getDependency().toString();
			return label;
		}
	}

	public void clearPartOfSpeechAnnotation() {
		for (BaseToken bt : this) {
			bt.setPartOfSpeech(null);
			bt.partOfSpeechStyle = null;
		}
		this.partOfSpeechStyle = null;
	}

	public void clearDependencyAnnotations() {
		for (BaseToken bt : this) {
			bt.setDependencyRelation(null);
			bt.setGovernor(null);
			bt.dependencyStyle = null;
		}
		this.dependencyStyle = null;
	}

	public static BaseSentence fromSentenceData09(final SentenceData09 sd09) {
		BaseSentence bs = new BaseSentence();
		for (int i = 1; i < sd09.length(); i++) {
			BaseToken bt = new BaseToken();
			bt.setWord(sd09.forms[i]);
			bt.setPartOfSpeech(PTB.fromString(sd09.gpos[i]));
			bt.setLemma(sd09.lemmas[i]);
			bt.setDependencyRelation(StanfordDependency
					.fromString(sd09.labels[i]));
			bs.add(bt);
		}
		for (int i = 1; i < sd09.length(); i++) {
			if (sd09.heads[i] != 0) {
				bs.get(i - 1).setGovernor(bs.get(sd09.heads[i] - 1));
			}
		}
		return bs;
	}

	public static BaseSentence fromTokenizedString(final String sentenceString) {
		StringTokenizer st = new StringTokenizer(sentenceString, " ");
		BaseSentence sentence = new BaseSentence();
		while (st.hasMoreTokens()) {
			BaseToken bt = new BaseToken(st.nextToken());
			sentence.add(bt);
			bt.setSentence(sentence);
		}
		return sentence;
	}

}
