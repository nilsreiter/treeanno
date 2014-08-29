///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2002 Jason Baldridge, Gann Bierner and Tom Morton
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////

package de.uniheidelberg.cl.a10.opennlp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.ngram.Token;
import opennlp.tools.postag.POSContextGenerator;
import opennlp.tools.util.Cache;

/**
 * A context generator for the POS Tagger.
 * 
 * A10 changes: stub for domain specific manipulation of context in method
 * getContext
 * 
 * @author Gann Bierner
 * @author Tom Morton
 * @version $Revision: 1.17 $, $Date: 2008/03/05 16:45:13 $
 */

public class A10DefaultPOSContextGenerator implements POSContextGenerator {

	protected final String SE = "*SE*";
	protected final String SB = "*SB*";
	private static final int PREFIX_LENGTH = 4;
	private static final int SUFFIX_LENGTH = 4;

	private static Pattern hasCap = Pattern.compile("[A-Z]");
	private static Pattern hasNum = Pattern.compile("[0-9]");

	private Cache contextsCache;
	private Object wordsKey;

	private final Dictionary dict;
	private final String[] dictGram;

	/**
	 * Initializes the current instance.
	 * 
	 * @param dict
	 */
	public A10DefaultPOSContextGenerator(final Dictionary dict) {
		this(0, dict);
	}

	/**
	 * Initializes the current instance.
	 * 
	 * @param cacheSize
	 * @param dict
	 */
	public A10DefaultPOSContextGenerator(final int cacheSize,
			final Dictionary dict) {
		this.dict = dict;
		this.dictGram = new String[1];
		if (cacheSize > 0) {
			this.contextsCache = new Cache(cacheSize);
		}
	}

	protected static String[] getPrefixes(final String lex) {
		String[] prefs = new String[PREFIX_LENGTH];
		for (int li = 0, ll = PREFIX_LENGTH; li < ll; li++) {
			prefs[li] = lex.substring(0, Math.min(li + 1, lex.length()));
		}
		return prefs;
	}

	protected static String[] getSuffixes(final String lex) {
		String[] suffs = new String[SUFFIX_LENGTH];
		for (int li = 0, ll = SUFFIX_LENGTH; li < ll; li++) {
			suffs[li] = lex.substring(Math.max(lex.length() - li - 1, 0));
		}
		return suffs;
	}

	@Override
	public String[] getContext(final int index, final Object[] sequence,
			final String[] priorDecisions, final Object[] additionalContext) {

		Object[] trgtSequence = new Object[sequence.length];
		Object[] srcSequence = new Object[sequence.length];

		for (int i = 0; i < sequence.length; i++) {
			String s = sequence[i].toString();
			trgtSequence[i] = s.split("_")[0];
			srcSequence[i] = s.split("_")[1];
		}

		return this.getContext(index, trgtSequence, srcSequence,
				priorDecisions, additionalContext);

		// return getContext(index, sequence, priorDecisions);
	}

	/**
	 * Returns the context for making a pos tag decision at the specified token
	 * index given the specified tokens and previous tags.
	 * 
	 * @param index
	 *            The index of the token for which the context is provided.
	 * @param tokens
	 *            The tokens in the sentence.
	 * @param tags
	 *            The tags assigned to the previous words in the sentence.
	 * @return The context for making a pos tag decision at the specified token
	 *         index given the specified tokens and previous tags.
	 */
	public String[] getContext(final int index, final Object[] tokens,
			final String[] tags) {
		String next, nextnext, lex, prev, prevprev;
		String tagprev, tagprevprev;
		tagprev = tagprevprev = null;
		next = nextnext = lex = prev = prevprev = null;

		lex = tokens[index].toString();
		if (tokens.length > index + 1) {
			next = tokens[index + 1].toString();
			if (tokens.length > index + 2)
				nextnext = tokens[index + 2].toString();
			else
				nextnext = this.SE; // Sentence End

		} else {
			next = this.SE; // Sentence End
		}

		if (index - 1 >= 0) {
			prev = tokens[index - 1].toString();
			tagprev = tags[index - 1];

			if (index - 2 >= 0) {
				prevprev = tokens[index - 2].toString();
				tagprevprev = tags[index - 2];
			} else {
				prevprev = this.SB; // Sentence Beginning
			}
		} else {
			prev = this.SB; // Sentence Beginning
		}
		String cacheKey = index + tagprev + tagprevprev;
		if (this.contextsCache != null) {
			if (this.wordsKey == tokens) {
				String[] cachedContexts = (String[]) this.contextsCache
						.get(cacheKey);
				if (cachedContexts != null) {
					return cachedContexts;
				}
			} else {
				this.contextsCache.clear();
				this.wordsKey = tokens;
			}
		}
		List<String> e = new ArrayList<String>();
		e.add("default");
		// add the word itself
		e.add("w=" + lex);
		this.dictGram[0] = lex;
		if (this.dict == null
				|| !this.dict.contains(Token.create(this.dictGram))) {
			// do some basic suffix analysis
			String[] suffs = getSuffixes(lex);
			for (int i = 0; i < suffs.length; i++) {
				e.add("suf=" + suffs[i]);
			}

			String[] prefs = getPrefixes(lex);
			for (int i = 0; i < prefs.length; i++) {
				e.add("pre=" + prefs[i]);
			}
			// see if the word has any special characters
			if (lex.indexOf('-') != -1) {
				e.add("h");
			}

			if (hasCap.matcher(lex).find()) {
				e.add("c");
			}

			if (hasNum.matcher(lex).find()) {
				e.add("d");
			}
		}
		// add the words and pos's of the surrounding context
		if (prev != null) {
			e.add("p=" + prev);
			if (tagprev != null) {
				e.add("t=" + tagprev);
			}
			if (prevprev != null) {
				e.add("pp=" + prevprev);
				if (tagprevprev != null) {
					e.add("t2=" + tagprevprev + "," + tagprev);
				}
			}
		}

		if (next != null) {
			e.add("n=" + next);
			if (nextnext != null) {
				e.add("nn=" + nextnext);
			}
		}
		String[] contexts = e.toArray(new String[e.size()]);
		if (this.contextsCache != null) {
			this.contextsCache.put(cacheKey, contexts);
		}
		return contexts;
	}

	/**
	 * A10 modification: additional parameter domSequence
	 * 
	 */
	public String[] getContext(final int index, final Object[] sequence,
			final Object[] domSequence, final String[] priorDecisions,
			final Object[] additionalContext) {
		return getContext(index, sequence, domSequence, priorDecisions);
	}

	/**
	 * <p>
	 * Returns the context for making a pos tag decision at the specified token
	 * index given the specified tokens and previous tags.
	 * </p>
	 * <p>
	 * A10 modification: work with additional input parameter for domain
	 * specific contexts, duplicated features
	 * </p>
	 * 
	 * @param index
	 *            The index of the token for which the context is provided.
	 * @param tokens
	 *            The tokens in the sentence.
	 * @param tags
	 *            The tags assigned to the previous words in the sentence.
	 * @return The context for making a pos tag decision at the specified token
	 *         index given the specified tokens and previous tags.
	 */
	public String[] getContext(final int index, final Object[] tokens,
			final Object[] domTokens, final String[] tags) {
		String next, nextnext, lex, prev, prevprev;

		// A10 inserted: Strings for domain specific context
		String domNext, domNextnext, domLex, domPrev, domPrevprev;
		String tagprev, tagprevprev;
		tagprev = tagprevprev = null;
		next = nextnext = lex = prev = prevprev = domNext = domNextnext = domLex = domPrev = domPrevprev = null;

		lex = tokens[index].toString();
		domLex = domTokens[index].toString();

		// build prev and next tokens for GENERAL sense
		if (tokens.length > index + 1) {
			next = tokens[index + 1].toString();
			if (tokens.length > index + 2)
				nextnext = tokens[index + 2].toString();
			else
				nextnext = this.SE; // Sentence End
		} else {
			next = this.SE; // Sentence End
		}
		if (index - 1 >= 0) {
			prev = tokens[index - 1].toString();
			tagprev = tags[index - 1];

			if (index - 2 >= 0) {
				prevprev = tokens[index - 2].toString();
				tagprevprev = tags[index - 2];
			} else {
				prevprev = this.SB; // Sentence Beginning
			}
		} else {
			prev = this.SB; // Sentence Beginning
		}

		// build prev/prevprev and next/nextnext tokens for DOMAIN SPECIFIC
		// sense
		if (domTokens.length > index + 1) {
			domNext = domTokens[index + 1].toString();
			if (domTokens.length > index + 2)
				domNextnext = domTokens[index + 2].toString();
			else
				domNextnext = this.SE; // Sentence End
		} else {
			domNext = this.SE; // Sentence End
		}
		if (index - 1 >= 0) {
			domPrev = domTokens[index - 1].toString();
			if (index - 2 >= 0) {
				domPrevprev = domTokens[index - 2].toString();
			} else {
				domPrevprev = this.SB; // Sentence Beginning
			}
		} else {
			domPrev = this.SB; // Sentence Beginning
		}

		String cacheKey = index + tagprev + tagprevprev;
		if (this.contextsCache != null) {
			if (this.wordsKey == tokens) {
				String[] cachedContexts = (String[]) this.contextsCache
						.get(cacheKey);
				if (cachedContexts != null) {
					return cachedContexts;
				}
			} else {
				this.contextsCache.clear();
				this.wordsKey = tokens;
			}
		}

		// build feature array
		List<String> e = new ArrayList<String>();
		e.add("default");
		// add the word itself
		e.add("w=" + lex);
		this.dictGram[0] = lex;
		if (this.dict == null
				|| !this.dict.contains(Token.create(this.dictGram))) {
			// do some basic suffix analysis
			String[] suffs = getSuffixes(lex);
			for (int i = 0; i < suffs.length; i++) {
				e.add("suf=" + suffs[i]);
			}

			String[] prefs = getPrefixes(lex);
			for (int i = 0; i < prefs.length; i++) {
				e.add("pre=" + prefs[i]);
			}
			// see if the word has any special characters
			if (lex.indexOf('-') != -1) {
				e.add("h");
			}

			if (hasCap.matcher(lex).find()) {
				e.add("c");
			}

			if (hasNum.matcher(lex).find()) {
				e.add("d");
			}
		}
		// add the domWord itself
		e.add("w=" + domLex);
		this.dictGram[0] = domLex;
		if (this.dict == null
				|| !this.dict.contains(Token.create(this.dictGram))) {
			// do some basic suffix analysis
			String[] suffs = getSuffixes(domLex);
			for (int i = 0; i < suffs.length; i++) {
				e.add("suf=" + suffs[i]);
			}

			String[] prefs = getPrefixes(domLex);
			for (int i = 0; i < prefs.length; i++) {
				e.add("pre=" + prefs[i]);
			}
			// see if the word has any special characters
			if (domLex.indexOf('-') != -1) {
				e.add("h");
			}

			if (hasCap.matcher(domLex).find()) {
				e.add("c");
			}

			if (hasNum.matcher(domLex).find()) {
				e.add("d");
			}
		}

		// add the words and pos's of the surrounding context
		// for general
		if (prev != null) {
			e.add("p=" + prev);
			if (tagprev != null) {
				e.add("t=" + tagprev);
			}
			if (prevprev != null) {
				e.add("pp=" + prevprev);
				if (tagprevprev != null) {
					e.add("t2=" + tagprevprev + "," + tagprev);
				}
			}
		}
		if (next != null) {
			e.add("n=" + next);
			if (nextnext != null) {
				e.add("nn=" + nextnext);
			}
		}
		// for domain
		if (domPrev != null) {
			e.add("p=" + domPrev);
			if (domPrevprev != null) {
				e.add("pp=" + domPrevprev);
			}
		}

		if (domNext != null) {
			e.add("n=" + domNext);
			if (domNextnext != null) {
				e.add("nn=" + domNextnext);
			}
		}

		String[] contexts = e.toArray(new String[e.size()]);
		if (this.contextsCache != null) {
			this.contextsCache.put(cacheKey, contexts);
		}

		return contexts;
	}

}