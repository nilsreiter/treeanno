package de.uniheidelberg.cl.a10.parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniheidelberg.cl.a10.data.BaseChunk;
import de.uniheidelberg.cl.a10.data.BaseSentence;
import de.uniheidelberg.cl.a10.data.BaseToken;
import de.uniheidelberg.cl.a10.data.IHasDependencyStyle;
import de.uniheidelberg.cl.a10.parser.dep.IDependency;
import de.uniheidelberg.cl.a10.parser.dep.StanfordDependency;
import de.uniheidelberg.cl.reiter.pos.PTB;
import de.uniheidelberg.cl.reiter.pos.WordClass;

public class ChunkerFromDependencies implements IHasDependencyStyle {
	Class<? extends IDependency> dependencyStyle = StanfordDependency.class;

	Set<StanfordDependency> exceptionsForNP = new HashSet<StanfordDependency>();
	Set<StanfordDependency> exceptionsForPP = new HashSet<StanfordDependency>();

	public ChunkerFromDependencies() {
		this.exceptionsForNP.add(StanfordDependency.PREP);
		this.exceptionsForNP.add(StanfordDependency.PUNCT);
		this.exceptionsForNP.add(StanfordDependency.ADVMOD);

		this.exceptionsForPP.add(StanfordDependency.PUNCT);
		this.exceptionsForPP.add(StanfordDependency.PREP);

	}

	@Override
	public Class<? extends IDependency> getDependencyStyle() {
		return this.dependencyStyle;
	}

	public List<BaseChunk> getChunks(final BaseSentence bs) {
		List<BaseChunk> ret = new ArrayList<BaseChunk>();
		ret.addAll(this.getNPChunks(bs));
		ret.addAll(this.getPPChunks(bs));

		return ret;
	}

	public List<BaseChunk> getChunks(final BaseSentence bs,
			final WordClass wordClass) {
		List<BaseChunk> ret = new ArrayList<BaseChunk>();
		for (BaseToken bt : bs) {
			if (bt.getPartOfSpeech().getWordClass() == wordClass) {
				SortedSet<BaseToken> chunkTokens = new TreeSet<BaseToken>(
						new Comparator<BaseToken>() {

							@Override
							public int compare(final BaseToken arg0,
									final BaseToken arg1) {
								return Integer.valueOf(bs.indexOf(arg0))
										.compareTo(bs.indexOf(arg1));
							}
						});
				chunkTokens.add(bt);
				for (BaseToken dep : bt.getDependents()) {
					if (dep.getDependents().isEmpty()
							&& dep.getDependencyRelation() != StanfordDependency.PUNCT) {
						chunkTokens.add(dep);
					}
				}
				BaseChunk chunkRange = new BaseChunk(bs, bs.indexOf(chunkTokens
						.first()), bs.indexOf(chunkTokens.last()));
				ret.add(chunkRange);
			}
		}
		return ret;
	}

	protected List<BaseChunk> getPPChunks(final BaseSentence bs) {
		List<BaseChunk> ret = new ArrayList<BaseChunk>();
		for (BaseToken bt : bs) {
			if (bt.getPartOfSpeech() == PTB.IN) {
				SortedSet<BaseToken> chunkTokens = new TreeSet<BaseToken>(
						new Comparator<BaseToken>() {

							@Override
							public int compare(final BaseToken arg0,
									final BaseToken arg1) {
								return Integer.valueOf(bs.indexOf(arg0))
										.compareTo(bs.indexOf(arg1));
							}
						});
				chunkTokens
						.addAll(this.followDependencies(bt, exceptionsForPP));

				BaseChunk chunkRange = new BaseChunk(bs, bs.indexOf(chunkTokens
						.first()), bs.indexOf(chunkTokens.last()));
				chunkRange.setCategory("PP");
				chunkRange.setHead(bt);
				ret.add(chunkRange);
			}
		}
		return ret;
	}

	protected List<BaseChunk> getNPChunks(final BaseSentence bs) {
		List<BaseChunk> ret = new ArrayList<BaseChunk>();
		for (BaseToken bt : bs) {
			if (bt.getPartOfSpeech().getWordClass() == WordClass.Noun) {
				SortedSet<BaseToken> chunkTokens = new TreeSet<BaseToken>(
						new Comparator<BaseToken>() {

							@Override
							public int compare(final BaseToken arg0,
									final BaseToken arg1) {
								return Integer.valueOf(bs.indexOf(arg0))
										.compareTo(bs.indexOf(arg1));
							}
						});
				chunkTokens
						.addAll(this.followDependencies(bt, exceptionsForNP));
				BaseChunk chunkRange = new BaseChunk(bs, bs.indexOf(chunkTokens
						.first()), bs.indexOf(chunkTokens.last()));
				chunkRange.setCategory("NP");
				chunkRange.setHead(bt);
				ret.add(chunkRange);
			}
		}
		return ret;
	}

	protected Set<BaseToken> followDependencies(final BaseToken start,
			final Set<StanfordDependency> exceptions) {
		Set<BaseToken> r = new HashSet<BaseToken>();
		r.add(start);
		for (BaseToken dep : start.getDependents()) {
			if (!exceptions.contains(dep.getDependencyRelation())) {
				r.add(dep);
				r.addAll(this.followDependencies(dep, exceptions));
			}
		}
		return r;

	}
}
