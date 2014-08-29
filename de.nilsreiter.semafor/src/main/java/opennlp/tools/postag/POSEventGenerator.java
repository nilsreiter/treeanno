package opennlp.tools.postag;

import java.util.ArrayList;
import java.util.List;

import opennlp.maxent.Event;
import opennlp.maxent.EventStream;

/**
 * Allows individual pos-tag events to be created and then accessed via the event stream interface.
 * One should use this generator by adding a small number of events (perhaps a sentence worth) and 
 * then removing them or the storage of events will require a large amount of memory. 
 */
public class POSEventGenerator implements EventStream {

  private List events;
  private int eventIndex;
  private POSContextGenerator pcg;
  
  /**
   * Creates an event generator with the specified context generator.
   * @param pcg The context generator for this event stream.
   */
  public POSEventGenerator(POSContextGenerator pcg) {
    this.pcg = pcg;
    events = new ArrayList(50);
    eventIndex = 0;
  }
  
  /**
   * Creates an event generator with a default context generator.
   */
  public POSEventGenerator() {
    this(new DefaultPOSContextGenerator(null));
  }
  
  /**
   * Adds an event for the tag in the tags array and token in the token array at teh specified index. 
   * @param tokens The tokens of a sentence.
   * @param tags The tags of a sentence.
   * @param index The index of the tag for which this event is to be created.
   */
  public void addEvent(String[] tokens, String[] tags, int index) {
    String[] context = pcg.getContext(index,tokens,tags,null);
    Event e = new Event(tags[index], context);
    events.add(e);
  }
  
  /**
   * Adds events for each tag/token of the specified arrays.
   * @param tokens The tags for a sentence.
   * @param tags The tokens for a sentence.
   */
  public void addEvents(String[] tokens, String[] tags) {
    for (int ti=0;ti<tokens.length;ti++) {
      addEvent(tokens,tags,ti);
    }
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
