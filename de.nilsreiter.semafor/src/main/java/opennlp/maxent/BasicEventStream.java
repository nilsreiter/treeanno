///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2001 Jason Baldridge
//
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
//
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////
package opennlp.maxent;

/**
 * A object which can deliver a stream of training events assuming
 * that each event is represented as a space separated list containing
 * all the contextual predicates, with the last item being the
 * outcome.
 * e.g.: 
 *
 * <p> cp_1 cp_2 ... cp_n outcome
 *
 * @author      Jason Baldridge
 * @version $Revision: 1.3 $, $Date: 2007/03/21 19:02:10 $ 
 */
public class BasicEventStream implements EventStream {
  ContextGenerator cg = new BasicContextGenerator();
  DataStream ds;
  Event next;
  
  public BasicEventStream (DataStream ds) {
    this.ds = ds;
    if (this.ds.hasNext())
      next = createEvent((String)this.ds.nextToken());
  }
  
  /**
   * Returns the next Event object held in this EventStream.  Each call to nextEvent advances the EventStream.
   *
   * @return the Event object which is next in this EventStream
   */
  public Event nextEvent () {
    while (next == null && this.ds.hasNext())
      next = createEvent((String)this.ds.nextToken());
    
    Event current = next;
    if (this.ds.hasNext()) {
      next = createEvent((String)this.ds.nextToken());
    }
    else {
      next = null;
    }
    return current;
  }
  
  /**
   * Test whether there are any Events remaining in this EventStream.
   *
   * @return true if this EventStream has more Events
   */
  public boolean hasNext () {
    while (next == null && ds.hasNext())
      next = createEvent((String)ds.nextToken());
    return next != null;
  }
  
  private Event createEvent(String obs) {
    int lastSpace = obs.lastIndexOf(' ');
    if (lastSpace == -1) 
      return null;
    else
      return new Event(obs.substring(lastSpace+1),
          cg.getContext(obs.substring(0, lastSpace)));
  }
  
  
}

