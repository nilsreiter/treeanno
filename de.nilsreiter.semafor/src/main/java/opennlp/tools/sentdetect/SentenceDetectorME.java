///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2004 Jason Baldridge, Gann Bierner and Tom Morton
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

package opennlp.tools.sentdetect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import opennlp.maxent.DataStream;
import opennlp.maxent.EventStream;
import opennlp.maxent.GIS;
import opennlp.maxent.GISModel;
import opennlp.maxent.IntegerPool;
import opennlp.maxent.MaxentModel;
import opennlp.maxent.PlainTextByLineDataStream;
import opennlp.maxent.io.SuffixSensitiveGISModelWriter;
import opennlp.tools.lang.thai.SentenceContextGenerator;

/**
 * A sentence detector for splitting up raw text into sentences.  A maximum
 * entropy model is used to evaluate the characters ".", "!", and "?" in a
 * string to determine if they signify the end of a sentence.
 *
 * @author      Jason Baldridge and Tom Morton
 * @version     $Revision: 1.17.2.1 $, $Date: 2008/08/22 00:34:34 $
 */

public class SentenceDetectorME implements SentenceDetector {

  /** The maximum entropy model to use to evaluate contexts. */
  private MaxentModel model;

  /** The feature context generator. */
  private final SDContextGenerator cgen;

  /** The EndOfSentenceScanner to use when scanning for end of sentence offsets. */
  private final EndOfSentenceScanner scanner;

  /** A pool of read-only java.lang.Integer objects in the range 0..100 */
  private static final IntegerPool INT_POOL = new IntegerPool(100);
  
  /** The list of probabilities associated with each decision. */
  private List sentProbs;
  
  protected boolean useTokenEnd;

  /**
   * Constructor which takes a MaxentModel and calls the three-arg
   * constructor with that model, an SDContextGenerator, and the
   * default end of sentence scanner.
   *
   * @param m The MaxentModel which this SentenceDetectorME will use to
   *          evaluate end-of-sentence decisions.
   */
  public SentenceDetectorME(MaxentModel m) {
    this(m, new DefaultSDContextGenerator(opennlp.tools.lang.english.EndOfSentenceScanner.eosCharacters), new opennlp.tools.lang.english.EndOfSentenceScanner());
  }

  /**
   * Constructor which takes a MaxentModel and a ContextGenerator.
   * calls the three-arg constructor with a default ed of sentence scanner.
   *
   * @param m The MaxentModel which this SentenceDetectorME will use to
   *          evaluate end-of-sentence decisions.
   * @param cg The ContextGenerator object which this SentenceDetectorME
   *           will use to turn Strings into contexts for the model to
   *           evaluate.
   */
  public SentenceDetectorME(MaxentModel m, SDContextGenerator cg) {
    this(m, cg, new opennlp.tools.lang.english.EndOfSentenceScanner());
  }

  /**
   * Creates a new <code>SentenceDetectorME</code> instance.
   *
   * @param m The MaxentModel which this SentenceDetectorME will use to
   *          evaluate end-of-sentence decisions.
   * @param cg The ContextGenerator object which this SentenceDetectorME
   *           will use to turn Strings into contexts for the model to
   *           evaluate.
   * @param s the EndOfSentenceScanner which this SentenceDetectorME
   *          will use to locate end of sentence indexes.
   */
  public SentenceDetectorME(MaxentModel m, SDContextGenerator cg, EndOfSentenceScanner s) {
    model = m;
    cgen = cg;
    scanner = s;
    sentProbs = new ArrayList(50);
    useTokenEnd = true;
  }
  
  

  /**
   * Detect sentences in a String.
   *
   * @param s  The string to be processed.
   * @return   A string array containing individual sentences as elements.
   *           
   */
  public String[] sentDetect(String s) {
    int[] starts = sentPosDetect(s);
    if (starts.length == 0) {
      return new String[] {s};
    }

    boolean leftover = starts[starts.length - 1] != s.length() && useTokenEnd;
    //System.err.println("sentDetect leftover="+leftover+" length="+s.length());
    String[] sents = new String[leftover? starts.length + 1 : starts.length];
    sents[0] = s.substring(0,starts[0]);
    //System.err.println("sentDetect:0 "+starts[0]);
    for (int si = 1; si < starts.length; si++) {
      sents[si] = s.substring(starts[si - 1], starts[si]);
      //System.err.println("sentDetect:"+si+" "+starts[si]);
    }

    if (leftover) {
        sents[sents.length - 1] = s.substring(starts[starts.length - 1]);
    }
    
    return sents;
  }
  
  private int getFirstWS(String s, int pos) {
    while (pos < s.length() && !Character.isWhitespace(s.charAt(pos)))
      pos++;
    return pos;
  }

  private int getFirstNonWS(String s, int pos) {
    while (pos < s.length() && Character.isWhitespace(s.charAt(pos)))
      pos++;
    return pos;
  }

  /**
   * Detect the position of the first words of sentences in a String.
   *
   * @param s  The string to be processed.
   * @return   A integer array containing the positions of the end index of
   *          every sentence
   *           
   */
  public int[] sentPosDetect(String s) {
    double sentProb = 1;
    sentProbs.clear();
    StringBuffer sb = new StringBuffer(s);
    List enders = scanner.getPositions(s);
    List positions = new ArrayList(enders.size());

    for (int i = 0, end = enders.size(), index = 0; i < end; i++) {
      Integer candidate = (Integer) enders.get(i);
      int cint = candidate.intValue();
      // skip over the leading parts of non-token final delimiters
      int fws = getFirstWS(s,cint + 1);
      if (i + 1 < end && ((Integer) enders.get(i + 1)).intValue() < fws) {
        continue;
      }

      double[] probs = model.eval(cgen.getContext(sb, candidate.intValue()));
      String bestOutcome = model.getBestOutcome(probs);
      sentProb *= probs[model.getIndex(bestOutcome)];
      //System.err.println("sentPosDetect: cand="+cint+" index="+index+" "+bestOutcome+" "+probs[model.getIndex(bestOutcome)]+" "+s.substring(0,cint));
      if (bestOutcome.equals("T") && isAcceptableBreak(s, index, cint)) {
        if (index != cint) {
          if (useTokenEnd) {
            positions.add(INT_POOL.get(getFirstNonWS(s, getFirstWS(s,cint + 1))));
          }
          else {
            positions.add(INT_POOL.get(getFirstNonWS(s,cint)));
          }
          sentProbs.add(new Double(probs[model.getIndex(bestOutcome)]));
        }
        index = cint + 1;
      }
    }

    int[] sentPositions = new int[positions.size()];
    for (int i = 0; i < sentPositions.length; i++) {
      sentPositions[i] = ((Integer) positions.get(i)).intValue();
    }
    return sentPositions;
  }

  /** Returns the probabilities associated with the most recent
   * calls to sentDetect().
   * @return probability for each sentence returned for the most recent
   * call to sentDetect.  If not applicable an empty array is
   * returned.
   */
  public double[] getSentenceProbabilities() {
    double[] sentProbArray = new double[sentProbs.size()];
    for (int i = 0; i < sentProbArray.length; i++) {
      sentProbArray[i] = ((Double) sentProbs.get(i)).doubleValue();
    }
    return sentProbArray;
  }

  /** 
   * Allows subclasses to check an overzealous (read: poorly
   * trained) model from flagging obvious non-breaks as breaks based
   * on some boolean determination of a break's acceptability.
   *
   * <p>The implementation here always returns true, which means
   * that the MaxentModel's outcome is taken as is.</p>
   * 
   * @param s the string in which the break occured. 
   * @param fromIndex the start of the segment currently being evaluated 
   * @param candidateIndex the index of the candidate sentence ending 
   * @return true if the break is acceptable 
   */
  protected boolean isAcceptableBreak(String s, int fromIndex, int candidateIndex) {
    return true;
  }

  /**
   * Trains a new model for the {@link SentenceDetectorME}.
   * 
   * @param es
   * @param iterations
   * @param cut
   * @return the new model
   * @throws IOException
   */
  public static GISModel train(EventStream es, int iterations, int cut) throws IOException {

    return GIS.trainModel(es, iterations, cut);
  }

  /**
   * Use this training method if you wish to supply an end of
   * sentence scanner which provides a different set of ending chars
   * other than the default ones.  They are "\\.|!|\\?|\\\"|\\)".
   * 
   * @param inFile 
   * @param iterations 
   * @param cut 
   * @param scanner 
   * @return the GISModel
   * @throws IOException 
   *
   */
  public static GISModel train(File inFile, int iterations, int cut, EndOfSentenceScanner scanner) throws IOException {
    EventStream es;
    DataStream ds;
    Reader reader;

    reader = new BufferedReader(new FileReader(inFile));
    ds = new PlainTextByLineDataStream(reader);
    es = new SDEventStream(ds, scanner);
    return GIS.trainModel(es, iterations, cut);
  }
  
  private static void usage() {
    System.err.println("Usage: SentenceDetectorME [-encoding charset] [-lang language] trainData modelName");
    System.err.println("-encoding charset specifies the encoding which should be used ");
    System.err.println("                  for reading and writing text.");
    System.err.println("-lang language    specifies the language (english|spanish|thai) which ");
    System.err.println("                  is being processed.");
    System.exit(1);    
  }

  /**
   * <p>Trains a new sentence detection model.</p>
   *
   * <p>Usage: opennlp.tools.sentdetect.SentenceDetectorME data_file new_model_name (iterations cutoff)?</p>
   * 
   * @param args 
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    int ai=0;
    String encoding = null;
    String lang = null;
    if (args.length == 0) {
      usage();
    }
    while (args[ai].startsWith("-")) {
      if (args[ai].equals("-encoding")) {
        ai++;
        if (ai < args.length) {
          encoding = args[ai];
          ai++;
        }
        else {
          usage();
        }
      }
      else if (args[ai].equals("-lang")) {
        ai++;
        if (ai < args.length) {
          lang = args[ai];
          ai++;
        }
        else {
          usage();
        }
      }
      else {
        usage();
      }
    }
    
    File inFile = new File(args[ai++]);
    File outFile = new File(args[ai++]);
    GISModel mod;
    
    try {
      EndOfSentenceScanner scanner = null;
      SDContextGenerator cg = null;
      if (lang == null || lang.equals("english") || lang.equals("spanish")) {
        scanner = new opennlp.tools.lang.english.EndOfSentenceScanner();
        cg = new DefaultSDContextGenerator(scanner.getEndOfSentenceCharacters());
      }
      else if (lang.equals("thai")) {
        scanner = new opennlp.tools.lang.thai.EndOfSentenceScanner();
        cg = new SentenceContextGenerator();
      }
      else {
        usage();
      }
      EventStream es = new SDEventStream(new PlainTextByLineDataStream(new InputStreamReader(new FileInputStream(inFile),encoding)),scanner,cg);

      if (args.length > ai)
        mod = train(es, Integer.parseInt(args[ai++]), Integer.parseInt(args[ai++]));
      else
        mod = train(es, 100, 5);

      System.out.println("Saving the model as: " + outFile);
      new SuffixSensitiveGISModelWriter(mod, outFile).persist();

    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }
}
