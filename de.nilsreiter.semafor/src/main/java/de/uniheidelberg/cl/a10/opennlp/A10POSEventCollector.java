///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2002 Jason Baldridge and Gann Bierner
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

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import opennlp.maxent.Event;
import opennlp.maxent.EventCollector;
import opennlp.tools.util.Pair;

/**
 * <p>
 * An event generator for the maxent POS Tagger.
 * </p>
 * <p>
 * A10 changes: modified method getEvents(), getEvents(boolean)
 * </p>
 * <p>
 * added A10 specific methods doubleSplit, convertDoubleAnnotatedString,
 * getDoubleEvents
 * </p>
 * 
 * @author Gann Bierner, zeller
 * @version $Revision: 1.10 $, $Date: 2006/11/17 12:30:19 $
 */

public class A10POSEventCollector implements EventCollector {

	private final BufferedReader br;
	private final A10DefaultPOSContextGenerator cg;

	/**
	 * Initializes the current instance.
	 * 
	 * @param data
	 * @param gen
	 */
	public A10POSEventCollector(final Reader data,
			final A10DefaultPOSContextGenerator gen) {
		this.br = new BufferedReader(data);
		this.cg = gen;
	}

	private static Pair split(final String s) {
		int split = s.lastIndexOf("_");
		if (split == -1) {
			System.out.println("There is a problem in your training data: " + s
					+ " does not conform to the format WORD_TAG.");
			return new Pair(s, "UNKNOWN");
		}
		return new Pair(s.substring(0, split), s.substring(split + 1));
	}

	public static Pair convertAnnotatedString(final String s) {
		ArrayList<Object> tokens = new ArrayList<Object>();
		ArrayList<Object> outcomes = new ArrayList<Object>();
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			Pair p = split(st.nextToken());

			tokens.add(p.a);
			outcomes.add(p.b);
		}
		return new Pair(tokens, outcomes);
	}

	/**
	 * A10 modification: Call getDoubleEvents (the A10 way) instead of
	 * getEvents.
	 * 
	 */
	@Override
	public Event[] getEvents() {
		// return getEvents(false);
		return getDoubleEvents(false);
	}

	/**
	 * Builds up the list of features using the Reader as input. For now, this
	 * should only be used to create training data.
	 * 
	 * A10 modification: inverse order of POS tag and word because of input of
	 * Hal Daumé's format
	 */
	@Override
	public Event[] getEvents(final boolean evalMode) {
		ArrayList<Event> elist = new ArrayList<Event>();
		try {
			String s = this.br.readLine();

			while (s != null) {
				Pair p = convertAnnotatedString(s);
				ArrayList<String> tokens = (ArrayList<String>) p.b;// p.a;
				ArrayList<String> outcomes = (ArrayList<String>) p.a; // p.b;
				ArrayList<String> tags = new ArrayList<String>();

				for (int i = 0; i < tokens.size(); i++) {
					String[] context = this.cg.getContext(i, tokens.toArray(),
							tags.toArray(new String[tags.size()]));

					Event e = new Event(outcomes.get(i), context);
					tags.add(outcomes.get(i));
					elist.add(e);
				}
				s = this.br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Event[] events = new Event[elist.size()];
		for (int i = 0; i < events.length; i++)
			events[i] = elist.get(i);

		return events;
	}

	/**
	 * A10 method to do a double split for the Hal Daumé-duplication of words.
	 * The two versions of the word (*word, 0word respectively 1word) are
	 * temporally concatenated into the string *word#0word.
	 * 
	 * @param s
	 * @return
	 */
	private static Pair doubleSplit(final String s) {
		int split1 = s.indexOf("_");
		if (split1 == -1) {
			System.out.println("There is a problem in your training data: " + s
					+ " does not conform to the format TAG_*WORD_#WORD.");
			return new Pair(s, "UNKNOWN");
		}
		int split2 = s.lastIndexOf("_");
		if (split1 == split2) {
			System.out
					.println("There is a problem in your training data: "
							+ s
							+ " does not conform to the format TAG_*WORD_0WORD, you only have format WORD_TAG.");
			return new Pair(s, "UNKNOWN");
		}
		return new Pair(s.substring(0, split1), s.substring(split1 + 1, split2)
				.concat("#").concat(s.substring(split2 + 1)));
	}

	/**
	 * A10 method to return a pair of converted annotated strings, containing
	 * two token arrays
	 * 
	 * @param s
	 * @return
	 */
	public static Pair convertDoubleAnnotatedString(final String s) {
		ArrayList<String> tokensGen = new ArrayList<String>();
		ArrayList<String> tokensDom = new ArrayList<String>();
		ArrayList<Object> outcomes = new ArrayList<Object>();
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			// Pair p = split(st.nextToken());
			Pair p = doubleSplit(st.nextToken());
			String bothWords = p.b.toString();
			String genWord = bothWords.split("#")[0]; // the *general words
			String domWord = bothWords.split("#")[1]; // the 0domain 1specific
			// words
			// System.out.println("genWord: " +genWord + ", domWord: " +
			// domWord);

			// modified: originally tokens.add(p.a), outcomes.add(p.b); inverse
			// order because of NN_*word_1word
			tokensGen.add(genWord);
			tokensDom.add(domWord);
			outcomes.add(p.a); // the POS tags
		}
		List<ArrayList<String>> tokensAll = new LinkedList<ArrayList<String>>();
		tokensAll.add(tokensGen);
		tokensAll.add(tokensDom);
		return new Pair(tokensAll, outcomes);
	}

	/**
	 * <p>
	 * A10 method to build up the list of features using the Reader as input.
	 * </p>
	 * <p>
	 * The Pair returned from AnnotatedString conversion consists of a List and
	 * an ArrayList; the List contains exactly *two* different Token ArrayLists
	 * and is split into its two ArrayList before further use.
	 * </p>
	 * 
	 * @return a single Array for *all* Events, containing *first* the general
	 *         Events, *then* the domain Events
	 */
	public Event[] getDoubleEvents(final boolean evalMode) {
		ArrayList<Event> elistAll = new ArrayList<Event>();
		try {
			String s = this.br.readLine(); // one sentence

			while (s != null) {
				Pair p = convertDoubleAnnotatedString(s);
				List<?> tokensAll = (LinkedList<?>) p.a;
				ArrayList<String> tokensGen = (ArrayList<String>) tokensAll
						.get(0);
				ArrayList<String> tokensDom = (ArrayList<String>) tokensAll
						.get(1);

				ArrayList<String> outcomes = (ArrayList<String>) p.b;
				ArrayList<String> tags = new ArrayList<String>();

				if (tokensDom.size() == tokensGen.size()) {
					for (int i = 0; i < tokensGen.size(); i++) {
						String[] context = this.cg.getContext(i,
								tokensGen.toArray(), tokensDom.toArray(),
								tags.toArray(new String[tags.size()]));

						Event e = new Event(outcomes.get(i), context);
						tags.add(outcomes.get(i));

						elistAll.add(e);
					}
				} else {
					System.err
							.println("Attention! There has not been generated the same number of general and domain specific words!");
					System.exit(1);

				}
				s = this.br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Event[] allEvents = new Event[elistAll.size()];
		for (int i = 0; i < elistAll.size(); i++) {
			allEvents[i] = elistAll.get(i);
		}

		return allEvents;
	}

	/*
	 * public static void main(String[] args) { String data =
	 * "the_DT stories_NNS about_IN well-heeled_JJ communities_NNS and_CC developers_NNS"
	 * ; EventCollector ec = new A10POSEventCollector(new StringReader(data),
	 * new DefaultPOSContextGenerator()); Event[] events = ec.getEvents();
	 * for(int i=0; i<events.length; i++)
	 * System.out.println(events[i].getOutcome()); }
	 */

}
