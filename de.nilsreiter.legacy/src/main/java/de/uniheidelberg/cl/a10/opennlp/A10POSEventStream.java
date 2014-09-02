///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2003 Tom Morton
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

import java.io.FileInputStream;
import java.io.StringReader;

import opennlp.maxent.DataStream;
import opennlp.maxent.Event;
import opennlp.maxent.EventCollector;
import opennlp.maxent.EventStream;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.util.InvalidFormatException;

/**
 * <p>
 * An implementation of EventStream which assumes the data stream gives a
 * sentence at a time with tokens as word_tag pairs.
 * </p>
 * <p>
 * A10 changes: addNewEvents(String) calls our EventCollector
 * </p>
 */

public class A10POSEventStream implements EventStream {

	private final A10DefaultPOSContextGenerator cg;
	private final DataStream data;
	private Event[] events;
	private int ei;

	/** The last line read in from the data file. */
	private String line;

	/**
	 * Initializes the current instance.
	 * 
	 * @param d
	 */
	public A10POSEventStream(final DataStream d) {
		this(d, new A10DefaultPOSContextGenerator(null));
	}

	/**
	 * Initializes the current instance.
	 * 
	 * @param d
	 * @param dict
	 */
	public A10POSEventStream(final DataStream d, final Dictionary dict) {
		this(d, new A10DefaultPOSContextGenerator(dict));
	}

	/**
	 * Initializes the current instance.
	 * 
	 * @param d
	 * @param cg
	 */
	public A10POSEventStream(final DataStream d,
			final A10DefaultPOSContextGenerator cg) {
		this.cg = cg;
		data = d;
		ei = 0;
		if (d.hasNext()) {
			addNewEvents((String) d.nextToken());
		} else {
			events = new Event[0];
		}
	}

	@Override
	public boolean hasNext() {
		if (ei < events.length) {
			return true;
		} else if (line != null) { // previous result has not been consumed
			return true;
		}
		// find next non-blank line
		while (data.hasNext()) {
			line = (String) data.nextToken();
			if (line.equals("")) {
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public Event nextEvent() {
		if (ei == events.length) {
			addNewEvents(line);
			ei = 0;
			line = null;
		}
		return events[ei++];
	}

	/**
	 * A10 modification: calls A10POSEventCollector instead of POSEventCollector
	 * 
	 * @param sentence
	 */
	private void addNewEvents(final String sentence) {
		// String sentence =
		// "the_DT stories_NNS about_IN well-heeled_JJ communities_NNS and_CC developers_NNS";

		EventCollector ec = new A10POSEventCollector(
				new StringReader(sentence), cg);
		events = ec.getEvents();
		// System.err.println("POSEventStream.addNewEvents: got "+events.length+" events");
	}

	public static void main(final String[] args) throws java.io.IOException,
			InvalidFormatException {
		EventStream es;
		if (args.length == 0) {
			es = new A10POSEventStream(
					new opennlp.maxent.PlainTextByLineDataStream(
							new java.io.InputStreamReader(System.in)));
		} else {
			es = new A10POSEventStream(
					new opennlp.maxent.PlainTextByLineDataStream(
							new java.io.InputStreamReader(System.in)),
					new Dictionary(new FileInputStream(args[0])));
		}
		/*
		 * while (es.hasNext()) { System.out.println(es.nextEvent()); }
		 */
	}
}