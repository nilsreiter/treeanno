package opennlp.maxent;

public class RealBasicEventStream implements EventStream {
  ContextGenerator cg = new BasicContextGenerator();
  DataStream ds;
  Event next;
  
  public RealBasicEventStream(DataStream ds) {
    this.ds = ds;
    if (this.ds.hasNext())
      next = createEvent((String)this.ds.nextToken());
    
  }

  public Event nextEvent() {
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

  public boolean hasNext() {
    while (next == null && ds.hasNext())
      next = createEvent((String)ds.nextToken());
    return next != null;
  }
  
  private Event createEvent(String obs) {
    int lastSpace = obs.lastIndexOf(' ');
    if (lastSpace == -1) 
      return null;
    else {
      String[] contexts = obs.substring(0,lastSpace).split("\\s+");
      float[] values = RealValueFileEventStream.parseContexts(contexts);
      return new Event(obs.substring(lastSpace+1),contexts,values);
    }
  }

  public static void main(String[] args) throws java.io.IOException {
    EventStream es = new RealBasicEventStream(new PlainTextByLineDataStream(new java.io.FileReader(args[0])));
    while (es.hasNext()) {
      System.out.println(es.nextEvent());
    }
  }
}
