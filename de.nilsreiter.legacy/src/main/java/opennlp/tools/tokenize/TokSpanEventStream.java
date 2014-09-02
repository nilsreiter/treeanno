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

package opennlp.tools.tokenize;

import java.util.ArrayList;
import java.util.List;

import opennlp.maxent.Event;
import opennlp.maxent.EventStream;
import opennlp.tools.util.Span;


/** An implementation of EventStream which allows events to be added by 
 *  offset and returns events for these offset-based tokens.
 */
public class TokSpanEventStream implements EventStream {

  private TokenContextGenerator cg;
  private List events;
  private int eventIndex;
  private boolean skipAlphaNumerics;

  /**
   * Initializes the current instance.
   * 
   * @param skipAlphaNumerics
   * @param cg
   */
  public TokSpanEventStream(boolean skipAlphaNumerics, TokenContextGenerator cg) {
    this.skipAlphaNumerics = skipAlphaNumerics;
    events = new ArrayList(50);
    eventIndex = 0;
    this.cg = cg;
  }
  
  /**
   * Initializes the current instance.
   * 
   * @param skipAlphaNumerics
   */
  public TokSpanEventStream(boolean skipAlphaNumerics) {
    this(skipAlphaNumerics, new DefaultTokenContextGenerator());
  }
  

  public static Event[] createEvents(Span[] tokens, String text, boolean skipAlphaNumerics, TokenContextGenerator cg) {
    List events = new ArrayList();
    if (tokens.length > 0) {
      int start = tokens[0].getStart();
      int end = tokens[tokens.length - 1].getEnd();
      String sent = text.substring(start, end);
      Span[] candTokens = WhitespaceTokenizer.INSTANCE.tokenizePos(sent);
      int firstTrainingToken = -1;
      int lastTrainingToken = -1;
      for (int ci = 0; ci < candTokens.length; ci++) {
        Span cSpan = candTokens[ci];
        String ctok = sent.substring(cSpan.getStart(), cSpan.getEnd());
        //adjust cSpan to text offsets
        cSpan = new Span(cSpan.getStart() + start, cSpan.getEnd() + start);
        //should we skip this token
        if (ctok.length() > 1
          && (!skipAlphaNumerics || !TokenizerME.alphaNumeric.matcher(ctok).matches())) {

          //find offsets of annotated tokens inside of candidate tokens
          boolean foundTrainingTokens = false;
          for (int ti = lastTrainingToken + 1; ti < tokens.length; ti++) {
            if (cSpan.contains(tokens[ti])) {
              if (!foundTrainingTokens) {
                firstTrainingToken = ti;
                foundTrainingTokens = true;
              }
              lastTrainingToken = ti;
            }
            else if (cSpan.getEnd() < tokens[ti].getEnd()) {
              break;
            }
            else if (tokens[ti].getEnd() < cSpan.getStart()) {
              //keep looking
            }
            else {
              System.err.println(
                "Bad training token: " + tokens[ti] + " cand: " + cSpan+" token="+text.substring(tokens[ti].getStart(),tokens[ti].getEnd()));
            }
          }
          // create training data
          //System.err.println("astart="+astart+" valid="+valid);
          if (foundTrainingTokens) {
            for (int ti = firstTrainingToken; ti <= lastTrainingToken; ti++) {
              Span tSpan = tokens[ti];
              int cStart = cSpan.getStart();
              for (int i = tSpan.getStart() + 1; i < tSpan.getEnd(); i++) {
                String[] context = cg.getContext(ctok, i - cStart);
                events.add(new Event(DefaultTokenContextGenerator.NO_SPLIT, context));
              }
              if (tSpan.getEnd() != cSpan.getEnd()) {
                String[] context = cg.getContext(ctok, tSpan.getEnd() - cStart);
                events.add(new Event(DefaultTokenContextGenerator.SPLIT, context));
              }
            }
          }
        }
      }
    }
    return (Event[]) events.toArray(new Event[events.size()]);
  }
  
  /**
   * Adds training events to the event stream for each of the specified tokens.
   * @param tokens charachter offsets into the specified text.
   * @param text The text of the tokens.
   */
  public void addEvents(Span[] tokens, String text) {
    Event[] tevents = createEvents(tokens,text,this.skipAlphaNumerics,cg);
    events.addAll(java.util.Arrays.asList(tevents));
  }

  public boolean hasNext() {
    return eventIndex < events.size();
  }

  public Event nextEvent() {
    Event e = (Event) events.get(eventIndex);
    eventIndex++;
    if (eventIndex == events.size()) {
      events.clear();
      eventIndex = 0;
    }
    return e;
  }
}
