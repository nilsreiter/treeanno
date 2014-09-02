/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreemnets.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0 
 * (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opennlp.tools.lang.english;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import opennlp.maxent.Event;
import opennlp.maxent.EventStream;
import opennlp.tools.tokenize.DefaultTokenContextGenerator;
import opennlp.tools.tokenize.TokSpanEventStream;
import opennlp.tools.tokenize.TokenContextGenerator;
import opennlp.tools.util.Span;

/**
 * Class which produces a TokenEventStream from a file of space delimited token.
 * This class uses a number of English-specific hueristics to un-separate tokens which
 * are typically found together in text.
 */
public class TokenStream implements EventStream {

  private BufferedReader in;
  private String line;
  private Pattern alphaNumeric = Pattern.compile("[A-Za-z0-9]");
  private boolean evenq = true;
  private List events;
  private TokenContextGenerator cg;
  private boolean skipAlphaNumerics;

  public TokenStream(InputStream is) throws IOException {
    this(is,new DefaultTokenContextGenerator(),false);
  }
  
  public TokenStream(InputStream is, TokenContextGenerator cg, boolean skipAlphaNumerics) throws IOException{
    this.in = new BufferedReader(new InputStreamReader(is));
    line = in.readLine();
    events = new LinkedList();
    this.cg = cg;
    this.skipAlphaNumerics = skipAlphaNumerics;
  }
  
  public boolean hasNext() {
    return events.size() != 0 || line != null;
  }

  public Event nextEvent() {
    while (events.size() == 0) {
      String[] tokens = line.split("\\s+");
      if (tokens.length == 0) {
        evenq =true;
      }
      StringBuffer sb = new StringBuffer(line.length());
      List spans = new ArrayList();
      int length = 0;
      for (int ti=0;ti<tokens.length;ti++) {
        String token = tokens[ti];
        String lastToken = ti -1 >= 0 ? tokens[ti-1] : "";
        if (token.equals("-LRB-")) {
          token = "(";
        }
        else if (token.equals("-LCB-")) {
          token = "{";
        }
        else if (token.equals("-RRB-")) {
          token = ")";
        }
        else if (token.equals("-RCB-")) {
          token = "}";
        }
        if (sb.length() == 0) {

        }
        else if (!alphaNumeric.matcher(token).find() || token.startsWith("'") || token.equalsIgnoreCase("n't")) {
          if ((token.equals("``") || token.equals("--") || token.equals("$") ||
              token.equals("(")  || token.equals("&")  || token.equals("#") ||
              (token.equals("\"") && (evenq && ti != tokens.length-1))) 
              && (!lastToken.equals("(") || !lastToken.equals("{"))) {
            //System.out.print(" "+token);
            length++;
          }
          else {
            //System.out.print(token);
          }
        }
        else {
          if (lastToken.equals("``") || (lastToken.equals("\"") && !evenq) || lastToken.equals("(") || lastToken.equals("{") 
              || lastToken.equals("$") || lastToken.equals("#")) {
            //System.out.print(token);
          }
          else {
            //System.out.print(" "+token);
            length++;
          }
        }
        if (token.equals("\"")) {
          if (ti == tokens.length -1) {
            evenq=true;
          }
          else {
            evenq = !evenq;
          }
        }
        if (sb.length() < length) {
          sb.append(" ");
        }
        sb.append(token);
        spans.add(new Span(length,length+token.length()));
        length+=token.length();
      }
      Event[] levents = TokSpanEventStream.createEvents((Span[])spans.toArray(new Span[spans.size()]),sb.toString(),skipAlphaNumerics,cg);
      events.addAll(java.util.Arrays.asList(levents));
      //System.out.println();
      try {
        line = in.readLine();
      } catch (IOException e) {
        e.printStackTrace();
        line = null;
      }
    }
    return (Event) events.remove(0); 
    //new TokenSample(sb.toString(),spans.toArray(new Span[spans.size()]));
  }
  

  public void remove() {
    throw new UnsupportedOperationException();
  }
  
  private static void usage() {
    System.err.println("TokenStream < in");
    System.err.println("Where \"in\" is a space delimited list of tokens.");
  }

  public static void main(String[] args) throws IOException {
    int ai=0;
    while (ai < args.length) {
      System.err.println("Unknown option "+args[ai]);
      usage();
      System.exit(1);
    }
    TokenStream tss = new TokenStream(System.in);
    while(tss.hasNext()) {
      Event evt = tss.nextEvent();
      System.out.println(evt);
    }
  }
}
