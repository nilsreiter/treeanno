package opennlp.tools.namefind;

import java.util.ArrayList;
import java.util.List;

import opennlp.maxent.DataStream;
import opennlp.tools.ngram.Token;
import opennlp.tools.util.Span;

/**
 * Creates name stream from annotated data which is one-sentence per line and tokenized
 * with names identified by <code>&lt;START&gt;</code> and <code>&lt;END&gt;</code> tags. 
 */
public class NameSampleDataStream implements NameSampleStream {

  public static final String START_TAG = "<START>";

  public static final String END_TAG = "<END>";
  
  private final DataStream in;

  public NameSampleDataStream(DataStream in) {
    this.in = in;
  }

  /* (non-Javadoc)
   * @see opennlp.tools.namefind.NameSampleStream#hasNext()
   */
  public boolean hasNext() {
    return in.hasNext();
  }

  /* (non-Javadoc)
   * @see opennlp.tools.namefind.NameSampleStream#nextNameSample()
   */
  public NameSample next() {
    String token = (String) in.nextToken();
    // clear adaptive data for every empty line
    return createNameSample(token);
  }
  
  private NameSample createNameSample(String taggedTokens) {
    String[] parts = taggedTokens.split(" ");
    List tokenList = new ArrayList(parts.length);
    List nameList = new ArrayList();

    int startIndex = -1;
    int wordIndex = 0;
    for (int pi = 0; pi < parts.length; pi++) {
      if (parts[pi].equals(START_TAG)) {
        startIndex = wordIndex;
      } 
      else if (parts[pi].equals(END_TAG)) {
        // create name
        if (startIndex == -1) {
          throw new RuntimeException("Invalid name near "+pi+" in: "+taggedTokens);
        }
        nameList.add(new Span(startIndex, wordIndex));
        startIndex = -1;
      } 
      else {
        if (parts[pi].length() > 0) {
          tokenList.add(Token.create(parts[pi]));
          wordIndex++;
        }
      }
    }
    if (startIndex != -1) {
      throw new RuntimeException("Invalid name near "+startIndex+" in: "+taggedTokens);
    }    
    Token[] sentence = (Token[]) tokenList.toArray(new Token[tokenList.size()]);
    Span[] names = (Span[]) nameList.toArray(new Span[nameList.size()]);
    return new NameSample(sentence,names,sentence.length==0);
  }
}