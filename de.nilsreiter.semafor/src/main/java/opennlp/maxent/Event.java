///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2001 Jason Baldridge and Gann Bierner
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////   
package opennlp.maxent;

/**
 * The context of a decision point during training. This includes contextual
 * predicates and an outcome.
 * 
 * @author Jason Baldridge
 * @version $Revision: 1.5 $, $Date: 2007/03/21 19:02:43 $
 */
public class Event extends gnu.trove.TLinkableAdapter {
	private final String outcome;
	private final String[] context;
	private final float[] values;

	public Event(final String outcome, final String[] context) {
		this(outcome, context, null);
	}

	public Event(final String outcome, final String[] context,
			final float[] values) {
		this.outcome = outcome;
		this.context = context;
		this.values = values;
	}

	public String getOutcome() {
		return outcome;
	}

	public String[] getContext() {
		return context;
	}

	public float[] getValues() {
		return values;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(outcome).append(" [");
		if (context.length > 0) {
			sb.append(context[0]);
			if (values != null) {
				sb.append("=" + values[0]);
			}
		}
		for (int ci = 1; ci < context.length; ci++) {
			sb.append(" ").append(context[ci]);
			if (values != null) {
				sb.append("=" + values[ci]);
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
