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

import java.util.*;

/**
 * A maxent event representation which we can use to sort based on the
 * predicates indexes contained in the events.
 *
 * @author      Jason Baldridge
 * @version $Revision: 1.6 $, $Date: 2008/09/01 18:03:21 $
 */
public class ComparableEvent implements Comparable {
    public int outcome;
    public int[] predIndexes;
    public int seen = 1;            // the number of times this event
                                    // has been seen.

    public float[] values;
    
    public ComparableEvent(int oc, int[] pids, float[] values) {
        outcome = oc;
        if (values == null) {
          Arrays.sort(pids);
        }
        else {
          sort(pids,values);
        }
        this.values = values; //needs to be sorted like pids
        predIndexes = pids;
    }
    
    public ComparableEvent(int oc, int[] pids) {
      this(oc,pids,null);
    }

    public int compareTo(Object o) {
        ComparableEvent ce = (ComparableEvent)o;
        if (outcome < ce.outcome) return -1;
        else if (outcome > ce.outcome) return 1;
	
        int smallerLength = (predIndexes.length > ce.predIndexes.length?
                             ce.predIndexes.length : predIndexes.length);

        for (int i=0; i<smallerLength; i++) {
            if (predIndexes[i] < ce.predIndexes[i]) return -1;
            else if (predIndexes[i] > ce.predIndexes[i]) return 1;
            if (values != null && ce.values != null) {
              if (values[i] < ce.values[i]) return -1;
              else if (values[i] > ce.values[i]) return 1;
            }
            else if (values != null) {
              if (values[i] < 1)  return -1;
              else if (values[i] > 1) return 1;
            }
            else if (ce.values != null) {
              if (1 < ce.values[i]) return -1;
              else if (1 > ce.values[i]) return 1;
            }
        }


        if (predIndexes.length < ce.predIndexes.length) return -1;
        else if (predIndexes.length > ce.predIndexes.length) return 1;

        return 0;
    }

    public String toString() {
        StringBuffer s = new StringBuffer().append(outcome).append(":");
        for (int i=0; i<predIndexes.length; i++) {
          s.append(" ").append(predIndexes[i]);
          if (values != null) {
            s.append("=").append(values[i]);
          }
        }
        return s.toString();
    }
    
    private void sort(int[] pids, float[] values) {
      for (int mi=0;mi<pids.length;mi++) {
        int min = mi;
        for (int pi=mi+1;pi<pids.length;pi++) {
          if (pids[min] > pids[pi]) {
            min = pi;
          }
        }
        int pid = pids[mi];
        pids[mi] = pids[min];
        pids[min] = pid;
        float val = values[mi];
        values[mi] = values[min];
        values[min] = val;
      }
    }
}
 
