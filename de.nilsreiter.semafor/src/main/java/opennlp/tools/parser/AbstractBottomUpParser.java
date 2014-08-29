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
//GNU General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with this program; if not, write to the Free Software
//Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////   
package opennlp.tools.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import opennlp.tools.util.Heap;
import opennlp.tools.util.ListHeap;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.Span;

/**
 * Abstract class which contains code to tag and chunk parses for bottom up parsing and
 * leaves implmentation of advancing parses and completing parses to extend class. 
 * <b>Note:</b> The nodes within
 * the returned parses are shared with other parses and therefore their parent node references will not be consistent
 * with their child node reference.  {@link #setParents setParents} can be used to make the parents consistent
 * with a partuicular parse, but subsequent calls to <code>setParents</code> can invalidate the results of earlier
 * calls.<br>  
 * 
 */
public abstract class AbstractBottomUpParser implements Parser {

  /** The maximum number of parses advanced from all preceeding parses at each derivation step. */
  protected int M;
  /** The maximum number of parses to advance from a single preceeding parse. */
  protected int K;
  /** The minimum total probability mass of advanced outcomes.*/
  protected double Q;
  /** The default beam size used if no beam size is given. */
  public static final int defaultBeamSize = 20;
  /** The default amount of probability mass required of advanced outcomes. */
  public static final double defaultAdvancePercentage = 0.95;
  /** Completed parses. */
  protected Heap completeParses;
  /** Incomplete parses which will be advanced. */
  protected Heap odh;
  /** Incomplete parses which have been advanced. */
  protected Heap ndh;
  
  /** The head rules for the parser. */
  protected HeadRules headRules;
  /** The set strings which are considered punctuation for the parser. 
   * Punctuation is not attached, but floats to the top of the parse as attachment
   * decisions are made about its non-punctuation sister nodes. 
   */
  protected Set punctSet;
  
  /** The label for the top node. */
  public static final String TOP_NODE = "TOP";
  /** The label for the top if an incomple node. */
  public static final String INC_NODE = "INC";
  /** The label for a token node. */
  public static final String TOK_NODE = "TK";
  /** The integer 0. */
  public static final Integer ZERO = new Integer(0);
  
  /** Prefix for outcomes starting a constituent. */
  public static final String START = "S-";
  /** Prefix for outcomes continuing a constituent. */
  public static final String CONT = "C-";
  /** Outcome for token which is not contained in a basal constituent. */
  public static final String OTHER = "O";
  /** Outcome used when a constituent is complete. */
  public static final String COMPLETE = "c";
  /** Outcome used when a constituent is incomplete. */
  public static final String INCOMPLETE = "i";
 
  /** The pos-tagger that the parser uses. */
  protected ParserTagger tagger;
  /** The chunker that the parser uses to chunk non-recursive structures. */
  protected ParserChunker chunker;
  
  /** Specifies whether failed parses should be reported to standard error. */
  protected boolean reportFailedParse;

  /** Specifies whether a derivation string should be created during parsing. This is useful for debuging. */
  protected boolean createDerivationString = false;
  
  /** Turns debug print on or off. */
  protected boolean debugOn = false;
  
  public AbstractBottomUpParser(ParserTagger tagger, ParserChunker chunker, HeadRules headRules, int beamSize, double advancePercentage) {
    this.tagger = tagger; 
    this.chunker = chunker;
    this.M = beamSize;
    this.K = beamSize;
    this.Q = advancePercentage;
    reportFailedParse = true;
    this.headRules = headRules;
    this.punctSet = headRules.getPunctuationTags();
    odh = new ListHeap(K);
    ndh = new ListHeap(K);
    completeParses = new ListHeap(K);
  }
  
  /**
   * Specifies whether the parser should report when it was unable to find a parse for
   * a particular sentence.
   * @param errorReporting If true then un-parsed sentences are reported, false otherwise.
   */
  public void setErrorReporting(boolean errorReporting) {
    this.reportFailedParse = errorReporting;
  }

  /**
   * Assigns parent references for the specified parse so that they
   * are consistent with the children references.
   * @param p The parse whose parent references need to be assigned.  
   */
  public static void setParents(Parse p) {
    Parse[] children = p.getChildren();
    for (int ci = 0; ci < children.length; ci++) {
      children[ci].setParent(p);
      setParents(children[ci]);
    }
  }

  /**
   * Removes the punctuation from the specified set of chunks, adds it to the parses
   * adjacent to the punctuation is specified, and returns a new array of parses with the punctuation
   * removed.
   * @param chunks A set of parses.
   * @param punctSet The set of punctuation which is to be removed.
   * @return An array of parses which is a subset of chunks with punctuation removed.
   */
  public static Parse[] collapsePunctuation(Parse[] chunks, Set punctSet) {
    List collapsedParses = new ArrayList(chunks.length);
    int lastNonPunct = -1;
    int nextNonPunct = -1;
    for (int ci=0,cn=chunks.length;ci<cn;ci++) {
      if (punctSet.contains(chunks[ci].getType())) {
        if (lastNonPunct >= 0) {
          chunks[lastNonPunct].addNextPunctuation(chunks[ci]);
        }
        for (nextNonPunct=ci+1;nextNonPunct<cn;nextNonPunct++) {
          if (!punctSet.contains(chunks[nextNonPunct].getType())) {
            break;
          }
        }
        if (nextNonPunct < cn) {
          chunks[nextNonPunct].addPreviousPunctuation(chunks[ci]);
        }
      }
      else {
        collapsedParses.add(chunks[ci]);
        lastNonPunct = ci;
      }
    }
    if (collapsedParses.size() == chunks.length) {
      return chunks;
    }
    //System.err.println("collapsedPunctuation: collapsedParses"+collapsedParses);
    return (Parse[]) collapsedParses.toArray(new Parse[collapsedParses.size()]);
  }
  
  
  
  /** Advances the specified parse and returns the an array advanced parses whose probability accounts for
   * more than the speicficed amount of probability mass.
   * @param p The parse to advance.
   * @param probMass The amount of probability mass that should be accounted for by the advanced parses. 
   */
  protected abstract Parse[] advanceParses(final Parse p, double probMass);
  
  /**
   * Adds the "TOP" node to the specified parse.
   * @param p The complete parse.
   */
  protected abstract void advanceTop(Parse p);

  public Parse[] parse(Parse tokens, int numParses) {
    if (createDerivationString) tokens.setDerivation(new StringBuffer(100));
    odh.clear();
    ndh.clear();
    completeParses.clear();
    int derivationStage = 0; //derivation length
    int maxDerivationLength = 2 * tokens.getChildCount() + 3;
    odh.add(tokens);
    Parse guess = null;
    double minComplete = 2;
    double bestComplete = -100000; //approximating -infinity/0 in ln domain
    while (odh.size() > 0 && (completeParses.size() < M || ((Parse) odh.first()).getProb() < minComplete) && derivationStage < maxDerivationLength) {
      ndh = new ListHeap(K);
      
      int derivationRank = 0;
      for (Iterator pi = odh.iterator(); pi.hasNext() && derivationRank < K; derivationRank++) { // forearch derivation
        Parse tp = (Parse) pi.next();
        //TODO: Need to look at this for K-best parsing cases 
        /* 
         if (tp.getProb() < bestComplete) { //this parse and the ones which follow will never win, stop advancing.
         break;
         }
         */
        if (guess == null && derivationStage == 2) {
          guess = tp;
        }
        if (debugOn) {
          System.out.print(derivationStage + " " + derivationRank + " "+tp.getProb());
          tp.show();
          System.out.println();
        }
        Parse[] nd = null;
        if (0 == derivationStage) {
          nd = advanceTags(tp);
        }
        else if (1 == derivationStage) {
          if (ndh.size() < K) {
            //System.err.println("advancing ts "+j+" "+ndh.size()+" < "+K);
            nd = advanceChunks(tp,bestComplete);
          }
          else {
            //System.err.println("advancing ts "+j+" prob="+((Parse) ndh.last()).getProb());
            nd = advanceChunks(tp,((Parse) ndh.last()).getProb());
          }
        }
        else { // i > 1
          nd = advanceParses(tp, Q);
        }
        if (nd != null) {
          for (int k = 0, kl = nd.length; k < kl; k++) {
            if (nd[k].complete()) {
              advanceTop(nd[k]);
              if (nd[k].getProb() > bestComplete) {
                bestComplete = nd[k].getProb();
              }
              if (nd[k].getProb() < minComplete) {
                minComplete = nd[k].getProb();
              }
              completeParses.add(nd[k]);
            }
            else {
              ndh.add(nd[k]);
            }
          }
        }
        else {
          if (reportFailedParse) {
            System.err.println("Couldn't advance parse "+derivationStage+" stage "+derivationRank+"!\n");
          }
          advanceTop(tp);
          completeParses.add(tp);
        }
      }
      derivationStage++;
      odh = ndh;
    }
    if (completeParses.size() == 0) {
      if (reportFailedParse) System.err.println("Couldn't find parse for: " + tokens);
      //Parse r = (Parse) odh.first();
      //r.show();
      //System.out.println();
      return new Parse[] {guess};
    }
    else if (numParses == 1){
      return new Parse[] {(Parse) completeParses.first()};
    }
    else {
      List topParses = new ArrayList(numParses);
      while(!completeParses.isEmpty() && topParses.size() < numParses) {
        Parse tp = (Parse) completeParses.extract();
        topParses.add(tp);
        //parses.remove(tp); 
      }
      return (Parse[]) topParses.toArray(new Parse[topParses.size()]);
    }
  }
  
  public Parse parse(Parse tokens) {
    Parse p = parse(tokens,1)[0];
    setParents(p);
    return p;
  }

  /**
   * Reutrns the top chunk sequences for the specified parse.
   * @param p A pos-tag assigned parse.
   * @param minChunkScore A minimum score below whihc chunks should not be advanced.
   * @return The top chunk assignments to the specified parse.
   */
  protected Parse[] advanceChunks(final Parse p, double minChunkScore) {
    // chunk
    Parse[] children = p.getChildren();
    String words[] = new String[children.length];
    String ptags[] = new String[words.length];
    double probs[] = new double[words.length];
    Parse sp = null;
    for (int i = 0, il = children.length; i < il; i++) {
      sp = children[i];
      words[i] = sp.getHead().toString();
      ptags[i] = sp.getType();
    }
    //System.err.println("adjusted mcs = "+(minChunkScore-p.getProb()));
    Sequence[] cs = chunker.topKSequences(words, ptags,minChunkScore-p.getProb());
    Parse[] newParses = new Parse[cs.length];
    for (int si = 0, sl = cs.length; si < sl; si++) {
      newParses[si] = (Parse) p.clone(); //copies top level
      if (createDerivationString) newParses[si].getDerivation().append(si).append(".");
      String[] tags = (String[]) cs[si].getOutcomes().toArray(new String[words.length]);
      cs[si].getProbs(probs);
      int start = -1;
      int end = 0;
      String type = null;
      //System.err.print("sequence "+si+" ");
      for (int j = 0; j <= tags.length; j++) {
        //if (j != tags.length) {System.err.println(words[j]+" "+ptags[j]+" "+tags[j]+" "+probs.get(j));}
        if (j != tags.length) {
          newParses[si].addProb(Math.log(probs[j]));
        }
        if (j != tags.length && tags[j].startsWith(CONT)) { // if continue just update end chunking tag don't use contTypeMap
          end = j;
        }
        else { //make previous constituent if it exists
          if (type != null) {
            //System.err.println("inserting tag "+tags[j]);
            Parse p1 = p.getChildren()[start];
            Parse p2 = p.getChildren()[end];
            //System.err.println("Putting "+type+" at "+start+","+end+" for "+j+" "+newParses[si].getProb());
            Parse[] cons = new Parse[end - start + 1];
            cons[0] = p1;
            //cons[0].label="Start-"+type;
            if (end - start != 0) {
              cons[end - start] = p2;
              //cons[end-start].label="Cont-"+type;
              for (int ci = 1; ci < end - start; ci++) {
                cons[ci] = p.getChildren()[ci + start];
                //cons[ci].label="Cont-"+type;
              }
            }
            Parse chunk = new Parse(p1.getText(), new Span(p1.getSpan().getStart(), p2.getSpan().getEnd()), type, 1, headRules.getHead(cons, type));
            chunk.isChunk(true);
            newParses[si].insert(chunk);
          }
          if (j != tags.length) { //update for new constituent
            if (tags[j].startsWith(START)) { // don't use startTypeMap these are chunk tags
              type = tags[j].substring(START.length());
              start = j;
              end = j;
            }
            else { // other 
              type = null;
            }
          }
        }
      }
      //newParses[si].show();System.out.println();
    }
    return newParses;
  }

  /**
   * Advances the parse by assigning it POS tags and returns multiple tag sequences.
   * @param p The parse to be tagged.
   * @return Parses with different POS-tag sequence assignments.
   */
  protected Parse[] advanceTags(final Parse p) {
    Parse[] children = p.getChildren();
    String[] words = new String[children.length];
    double[] probs = new double[words.length];
    for (int i = 0,il = children.length; i < il; i++) {
      words[i] = children[i].toString();
    }
    Sequence[] ts = tagger.topKSequences(words);
    if (ts.length == 0) {
      System.err.println("no tag sequence");
    }
    Parse[] newParses = new Parse[ts.length];
    for (int i = 0; i < ts.length; i++) {
      String[] tags = (String[]) ts[i].getOutcomes().toArray(new String[words.length]);
      ts[i].getProbs(probs);
      newParses[i] = (Parse) p.clone(); //copies top level
      if (createDerivationString) newParses[i].getDerivation().append(i).append(".");
      for (int j = 0; j < words.length; j++) {
        Parse word = children[j];
        //System.err.println("inserting tag "+tags[j]);
        double prob = probs[j];
        newParses[i].insert(new Parse(word.getText(), word.getSpan(), tags[j], prob,j));
        newParses[i].addProb(Math.log(prob));
        //newParses[i].show();
      }
    }
    return newParses;
  }

  /**
   * Determines the mapping between the specified index into the specified parses without punctuation to 
   * the coresponding index into the specified parses.
   * @param index An index into the parses without punctuation.
   * @param nonPunctParses The parses without punctuation.
   * @param parses The parses wit punctuation.
   * @return An index into the specified parses which coresponds to the same node the specified index
   * into the parses with punctuation.
   */
  protected int mapParseIndex(int index, Parse[] nonPunctParses, Parse[] parses) {
    int parseIndex = index;
    while (parses[parseIndex] != nonPunctParses[index]) {
      parseIndex++;
    }
    return parseIndex;
  }
}
