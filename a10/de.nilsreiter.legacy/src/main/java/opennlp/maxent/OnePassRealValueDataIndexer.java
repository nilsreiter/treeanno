package opennlp.maxent;

import gnu.trove.TIntArrayList;
import gnu.trove.TLinkedList;
import gnu.trove.TObjectIntHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An indexer for maxent model data which handles cutoffs for uncommon
 * contextual predicates and provides a unique integer index for each of the
 * predicates and maintains event values.  
 * @author Tom Morton
 */
public class OnePassRealValueDataIndexer extends OnePassDataIndexer {

  float[][] values;
  
  /**
   * Two argument constructor for DataIndexer.
   * @param eventStream An Event[] which contains the a list of all the Events
   *               seen in the training data.
   * @param cutoff The minimum number of times a predicate must have been
   *               observed in order to be included in the model.
   */
  public OnePassRealValueDataIndexer(EventStream eventStream, int cutoff) {
    super(eventStream,cutoff);
  }
  
  public float[][] getValues() {
    return values;
  }

  protected int sortAndMerge(List eventsToCompare) {
    int numUniqueEvents = super.sortAndMerge(eventsToCompare);
    values = new float[numUniqueEvents][];
    int numEvents = eventsToCompare.size();
    for (int i = 0, j = 0; i < numEvents; i++) {
      ComparableEvent evt = (ComparableEvent) eventsToCompare.get(i);
      if (null == evt) {
        continue; // this was a dupe, skip over it.
      }
      values[j++] = evt.values;
    }
    return numUniqueEvents;
  }
  
  protected List index(TLinkedList events,
      TObjectIntHashMap predicateIndex) {
    TObjectIntHashMap omap = new TObjectIntHashMap();
    
    int numEvents = events.size();
    int outcomeCount = 0;
    List eventsToCompare = new ArrayList(numEvents);
    TIntArrayList indexedContext = new TIntArrayList();
    
    for (int eventIndex=0; eventIndex<numEvents; eventIndex++) {
      Event ev = (Event)events.removeFirst();
      String[] econtext = ev.getContext();
      ComparableEvent ce;
      
      int ocID;
      String oc = ev.getOutcome();
      
      if (omap.containsKey(oc)) {
        ocID = omap.get(oc);
      } else {
        ocID = outcomeCount++;
        omap.put(oc, ocID);
      }
      
      for (int i=0; i<econtext.length; i++) {
        String pred = econtext[i];
        if (predicateIndex.containsKey(pred)) {
          indexedContext.add(predicateIndex.get(pred));
        }
      }
      
//    drop events with no active features
      if (indexedContext.size() > 0) {
        ce = new ComparableEvent(ocID, indexedContext.toNativeArray(), ev.getValues());
        eventsToCompare.add(ce);
      }
      else {
        System.err.println("Dropped event "+ev.getOutcome()+":"+Arrays.asList(ev.getContext()));
      }
//    recycle the TIntArrayList
      indexedContext.resetQuick();
    }
    outcomeLabels = toIndexedStringArray(omap);
    predLabels = toIndexedStringArray(predicateIndex);
    return eventsToCompare;
  }

}
