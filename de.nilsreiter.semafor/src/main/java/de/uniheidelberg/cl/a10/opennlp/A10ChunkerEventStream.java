///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2003 Thomas Morton
// 
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
// 
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
// 
//You should have received a copy of the GNU Lesser General Public
//License along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////
package de.uniheidelberg.cl.a10.opennlp;

import java.util.ArrayList;
import java.util.List;

import opennlp.maxent.DataStream;
import opennlp.maxent.Event;
import opennlp.maxent.EventStream;

/**
 * Class for creating an event stream out of data files for training a chunker.
 */
public class A10ChunkerEventStream implements EventStream {

	private final A10ChunkerContextGenerator cg;
	private final DataStream data;
	private Event[] events;
	private int ei;

	/**
	 * Creates a new event stream based on the specified data stream.
	 * 
	 * @param d
	 *            The data stream for this event stream.
	 */
	public A10ChunkerEventStream(final DataStream d) {
		this(d, new A10DefaultChunkerContextGenerator());
	}

	/**
	 * Creates a new event stream based on the specified data stream using the
	 * specified context generator.
	 * 
	 * @param d
	 *            The data stream for this event stream.
	 * @param cg
	 *            The context generator which should be used in the creation of
	 *            events for this event stream.
	 */
	public A10ChunkerEventStream(final DataStream d,
			final A10ChunkerContextGenerator cg) {
		this.cg = cg;
		this.data = d;
		this.ei = 0;
		if (d.hasNext()) {
			addNewEvents();
		} else {
			this.events = new Event[0];
		}
	}

	/* inherieted javadoc */
	@Override
	public Event nextEvent() {
		if (this.ei == this.events.length) {
			addNewEvents();
			this.ei = 0;
		}
		return this.events[this.ei++];
	}

	/* inherieted javadoc */
	@Override
	public boolean hasNext() {
		return this.ei < this.events.length || this.data.hasNext();
	}

	private void addNewEvents() {
		// List preds = new ArrayList();
		List<List<String>> features = new ArrayList<List<String>>();
		boolean inited = false;
		for (String line = (String) this.data.nextToken(); line != null
				&& !line.equals(""); line = (String) this.data.nextToken()) {
			String[] parts = line.split(" ");
			if (parts.length < 5) {
				// System.err
				// .println("The following line does not have the correct format: "
				// + line);

			} else {
				if (!inited) {
					for (int i = 0; i < parts.length; i++) {
						features.add(new ArrayList<String>());
					}
					inited = true;
				}

				for (int i = 0; i < parts.length && i < features.size(); i++) {
					features.get(i).add(parts[i]);
				}
				// preds.add(parts[2]);
			}
		}
		this.events = new Event[features.get(0).size()];

		String[][] f_array = new String[features.size()][];
		for (int i = 0; i < features.size(); i++) {
			f_array[i] = new String[features.get(i).size()];
			for (int j = 0; j < features.get(i).size(); j++) {
				f_array[i][j] = features.get(i).get(j);
			}
		}

		// preds.toArray(new String[preds.size()]);
		for (int ei = 0, el = this.events.length; ei < el; ei++) {
			// System.err.println(features.get(0).get(ei));
			this.events[ei] = new Event(features.get(0).get(ei),
					this.cg.getContext(ei, f_array));
		}
	}
}