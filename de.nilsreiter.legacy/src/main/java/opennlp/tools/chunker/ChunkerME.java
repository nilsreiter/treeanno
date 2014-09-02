///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2003 Thomas Morton
// 
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
// 
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
// 
//You should have received a copy of the GNU Lesser General Public
//License along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////
package opennlp.tools.chunker;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import opennlp.maxent.GISModel;
import opennlp.maxent.MaxentModel;
import opennlp.maxent.TwoPassDataIndexer;
import opennlp.tools.util.BeamSearch;
import opennlp.tools.util.Sequence;

/**
 * The class represents a maximum-entropy-based chunker.  Such a chunker can be used to
 * find flat structures based on sequence inputs such as noun phrases or named entities.
 *
 */
public class ChunkerME implements Chunker {

  private static final int DEFAULT_BEAM_SIZE = 10;
  
  /** The beam used to search for sequences of chunk tag assignments. */
  protected BeamSearch beam;
  
  private Sequence bestSequence;
  
  /** The model used to assign chunk tags to a sequence of tokens. */
  protected MaxentModel model;

  /**
   * Creates a chunker using the specified model.
   * @param mod The maximum entropy model for this chunker.
   */
  public ChunkerME(MaxentModel mod) {
    this(mod, new DefaultChunkerContextGenerator(), DEFAULT_BEAM_SIZE);
  }

  /**
   * Creates a chunker using the specified model and context generator.
   * @param mod The maximum entropy model for this chunker.
   * @param cg The context generator to be used by the specified model.
   */
  public ChunkerME(MaxentModel mod, ChunkerContextGenerator cg) {
    this(mod, cg, DEFAULT_BEAM_SIZE);
  }

  /**
   * Creates a chunker using the specified model and context generator and decodes the
   * model using a beam search of the specified size.
   * @param mod The maximum entropy model for this chunker.
   * @param cg The context generator to be used by the specified model.
   * @param beamSize The size of the beam that should be used when decoding sequences.
   */
  public ChunkerME(MaxentModel mod, ChunkerContextGenerator cg, int beamSize) {
    beam = new ChunkBeamSearch(beamSize, cg, mod);
    this.model = mod;
  }

  /* inherieted javadoc */
  public List chunk(List toks, List tags) {
    bestSequence =
        beam.bestSequence(toks, new Object[] { (String[]) tags.toArray(new String[tags.size()]) });
    return bestSequence.getOutcomes();
  }
  
  /* inherieted javadoc */
  public String[] chunk(Object[] toks, String[] tags) {
    bestSequence = beam.bestSequence(Arrays.asList(toks), new Object[] {tags});
    List c = bestSequence.getOutcomes();
    return (String[]) c.toArray(new String[c.size()]);
  }
 
  /** 
    * This method determines wheter the outcome is valid for the preceeding sequence.  
    * This can be used to implement constraints on what sequences are valid.  
    * @param outcome The outcome.
    * @param sequence The precceding sequence of outcome assignments. 
    * @return true is the outcome is valid for the sequence, false otherwise.
    */
  protected boolean validOutcome(String outcome, String[] sequence) {
    return true;
  }

  /**
   * This class implements the abstract BeamSearch class to allow for the chunker to use
   * the common beam search code. 
   *
   */
  class ChunkBeamSearch extends BeamSearch {
    
    ChunkBeamSearch(int size, ChunkerContextGenerator cg, MaxentModel model) {
      super(size, cg, model);
    }
    
    protected boolean validSequence(int i, Object[] sequence, String[] s, String outcome) {
      return validOutcome(outcome, s);
    }
  }

  /**
   * Populates the specified array with the probabilities of the last decoded sequence.  The
   * sequence was determined based on the previous call to <code>chunk</code>.  The 
   * specified array should be at least as large as the numbe of tokens in the previous call to <code>chunk</code>.
   * @param probs An array used to hold the probabilities of the last decoded sequence.
   */
  public void probs(double[] probs) {
    bestSequence.getProbs(probs);
  }

    /**
     * Returns an array with the probabilities of the last decoded sequence.  The
     * sequence was determined based on the previous call to <code>chunk</code>.
     * @return An array with the same number of probabilities as tokens were sent to <code>chunk</code>
     * when it was last called.   
     */
  public double[] probs() {
    return bestSequence.getProbs();
  }

  /**
   * Trains a new model for the {@link ChunkerME}.
   * 
   * @param es
   * @param iterations
   * @param cut
   * @return the new model
   * @throws java.io.IOException
   */
  public static GISModel train(opennlp.maxent.EventStream es, int iterations, int cut) throws java.io.IOException {
    return opennlp.maxent.GIS.trainModel(iterations, new TwoPassDataIndexer(es, cut));
  }
  
  private static void usage() {
    System.err.println("Usage: ChunkerME [-encoding charset] trainingFile modelFile");
    System.err.println();
    System.err.println("Training file should be one word per line where each line consists of a ");
    System.err.println("space-delimited triple of \"word pos outcome\".  Sentence breaks are indicated by blank lines.");
    System.exit(1);
  }

  /**
   * Trains the chunker using the specified parameters. <br>
   * Usage: ChunkerME trainingFile modelFile. <br>
   * Training file should be one word per line where each line consists of a 
   * space-delimited triple of "word pos outcome".  Sentence breaks are indicated by blank lines.
   * @param args The training file and the model file.
   * @throws java.io.IOException When the specifed files can not be read.
   */
  public static void main(String[] args) throws java.io.IOException {
    if (args.length == 0) {
      usage();
    }
    int ai = 0;
    String encoding = null;
    while (args[ai].startsWith("-")) {
      if (args[ai].equals("-encoding") && ai+1 < args.length) {
        ai++;
        encoding = args[ai];
      }
      else {
        System.err.println("Unknown option: "+args[ai]);
        usage();
      }
      ai++;
    }
    java.io.File inFile = null;
    java.io.File outFile = null;
    if (ai < args.length) {
      inFile = new java.io.File(args[ai++]);
    }
    else {
      usage();
    }
    if (ai < args.length) {
      outFile = new java.io.File(args[ai++]);
    }
    else {
      usage();
    }
    int iterations = 100;
    int cutoff = 5;
    if (args.length > ai) {
      iterations = Integer.parseInt(args[ai++]);
    }
    if (args.length > ai) {
      cutoff = Integer.parseInt(args[ai++]); 
    }
    GISModel mod;
    opennlp.maxent.EventStream es;
    if (encoding != null) {
       es = new ChunkerEventStream(new opennlp.maxent.PlainTextByLineDataStream(new InputStreamReader(new FileInputStream(inFile),encoding)));
    }
    else {
      es = new ChunkerEventStream(new opennlp.maxent.PlainTextByLineDataStream(new java.io.FileReader(inFile)));
    }
    mod = train(es, iterations, cutoff);
    System.out.println("Saving the model as: " + args[1]);
    new opennlp.maxent.io.SuffixSensitiveGISModelWriter(mod, outFile).persist();
  }
}
