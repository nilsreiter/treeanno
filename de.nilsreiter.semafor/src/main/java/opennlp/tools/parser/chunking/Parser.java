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
package opennlp.tools.parser.chunking;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import opennlp.maxent.DataStream;
import opennlp.maxent.GISModel;
import opennlp.maxent.MaxentModel;
import opennlp.maxent.TwoPassDataIndexer;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.ngram.NGramModel;
import opennlp.tools.ngram.Token;
import opennlp.tools.parser.AbstractBottomUpParser;
import opennlp.tools.parser.HeadRules;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserChunker;
import opennlp.tools.parser.ParserEventTypeEnum;
import opennlp.tools.parser.ParserTagger;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

/**
 * Class for a shift reduce style parser based on Adwait Ratnaparkhi's 1998 thesis. 
 */
public class Parser extends AbstractBottomUpParser {

  private MaxentModel buildModel;
  private MaxentModel checkModel;

  private BuildContextGenerator buildContextGenerator;
  private CheckContextGenerator checkContextGenerator;

  private double[] bprobs;
  private double[] cprobs;

  private static final String TOP_START = START + TOP_NODE;
  private int topStartIndex;
  private Map startTypeMap;
  private Map contTypeMap;
  
  private int completeIndex;
  private int incompleteIndex;
  

  /**
   * Creates a new parser using the specified models and head rules.
   * @param buildModel The model to assign constituent labels.
   * @param checkModel The model to determine a constituent is complete.
   * @param tagger The model to assign pos-tags.
   * @param chunker The model to assign flat constituent labels.
   * @param headRules The head rules for head word perculation.
   */
  public Parser(MaxentModel buildModel, MaxentModel checkModel, ParserTagger tagger, ParserChunker chunker, HeadRules headRules) {
  	this(buildModel,checkModel,tagger,chunker,headRules,defaultBeamSize,defaultAdvancePercentage);
  }

  /**
   * Creates a new parser using the specified models and head rules using the specified beam size and advance percentage.
   * @param buildModel The model to assign constituent labels.
   * @param checkModel The model to determine a constituent is complete.
   * @param tagger The model to assign pos-tags.
   * @param chunker The model to assign flat constituent labels.
   * @param headRules The head rules for head word perculation.
   * @param beamSize The number of different parses kept during parsing. 
   * @param advancePercentage The minimal amount of probability mass which advanced outcomes must represent.  
   * Only outcomes which contribute to the top "advancePercentage" will be explored.    
   */
  public Parser(MaxentModel buildModel, MaxentModel checkModel, ParserTagger tagger, ParserChunker chunker, HeadRules headRules, int beamSize, double advancePercentage) {
    super(tagger,chunker,headRules,beamSize,advancePercentage);
    this.buildModel = buildModel;
    this.checkModel = checkModel;
    bprobs = new double[buildModel.getNumOutcomes()];
    cprobs = new double[checkModel.getNumOutcomes()];
    this.buildContextGenerator = new BuildContextGenerator();
    this.checkContextGenerator = new CheckContextGenerator();
    startTypeMap = new HashMap();
    contTypeMap = new HashMap();
    for (int boi = 0, bon = buildModel.getNumOutcomes(); boi < bon; boi++) {
      String outcome = buildModel.getOutcome(boi);
      if (outcome.startsWith(START)) {
        //System.err.println("startMap "+outcome+"->"+outcome.substring(START.length()));
        startTypeMap.put(outcome, outcome.substring(START.length()));
      }
      else if (outcome.startsWith(CONT)) {
        //System.err.println("contMap "+outcome+"->"+outcome.substring(CONT.length()));
        contTypeMap.put(outcome, outcome.substring(CONT.length()));
      }
    }
    topStartIndex = buildModel.getIndex(TOP_START);
    completeIndex = checkModel.getIndex(COMPLETE);
    incompleteIndex = checkModel.getIndex(INCOMPLETE);
  }
  
  protected void advanceTop(Parse p) {
    buildModel.eval(buildContextGenerator.getContext(p.getChildren(), 0), bprobs);
    p.addProb(Math.log(bprobs[topStartIndex]));
    checkModel.eval(checkContextGenerator.getContext(p.getChildren(), TOP_NODE, 0, 0), cprobs);
    p.addProb(Math.log(cprobs[completeIndex]));
    p.setType(TOP_NODE);
  }

  protected Parse[] advanceParses(final Parse p, double probMass) {
    double q = 1 - probMass;
    /** The closest previous node which has been labeled as a start node. */
    Parse lastStartNode = null;
    /** The index of the closest previous node which has been labeled as a start node. */
    int lastStartIndex = -1;
    /** The type of the closest previous node which has been labeled as a start node. */
    String lastStartType = null;
    /** The index of the node which will be labeled in this iteration of advancing the parse. */
    int advanceNodeIndex;
    /** The node which will be labeled in this iteration of advancing the parse. */
    Parse advanceNode=null;
    Parse[] originalChildren = p.getChildren();
    Parse[] children = collapsePunctuation(originalChildren,punctSet);
    int numNodes = children.length;
    if (numNodes == 0) {
      return null;
    }
    //determines which node needs to be labeled and prior labels.
    for (advanceNodeIndex = 0; advanceNodeIndex < numNodes; advanceNodeIndex++) {
      advanceNode = children[advanceNodeIndex];
      if (advanceNode.getLabel() == null) {
        break;
      }
      else if (startTypeMap.containsKey(advanceNode.getLabel())) {
        lastStartType = (String) startTypeMap.get(advanceNode.getLabel());
        lastStartNode = advanceNode;
        lastStartIndex = advanceNodeIndex;
        //System.err.println("lastStart "+i+" "+lastStart.label+" "+lastStart.prob);
      }
    }
    int originalAdvanceIndex = mapParseIndex(advanceNodeIndex,children,originalChildren);
    List newParsesList = new ArrayList(buildModel.getNumOutcomes());
    //call build
    buildModel.eval(buildContextGenerator.getContext(children, advanceNodeIndex), bprobs);
    double bprobSum = 0;
    while (bprobSum < probMass) {
      /** The largest unadvanced labeling. */ 
      int max = 0;
      for (int pi = 1; pi < bprobs.length; pi++) { //for each build outcome
        if (bprobs[pi] > bprobs[max]) {
          max = pi;
        }
      }
      if (bprobs[max] == 0) {
        break;
      }
      double bprob = bprobs[max];
      bprobs[max] = 0; //zero out so new max can be found
      bprobSum += bprob;
      String tag = buildModel.getOutcome(max);
      //System.out.println("trying "+tag+" "+bprobSum+" lst="+lst);
      if (max == topStartIndex) { // can't have top until complete
        continue;
      }
      //System.err.println(i+" "+tag+" "+bprob);
      if (startTypeMap.containsKey(tag)) { //update last start
        lastStartIndex = advanceNodeIndex;
        lastStartNode = advanceNode;
        lastStartType = (String) startTypeMap.get(tag);
      }
      else if (contTypeMap.containsKey(tag)) {
        if (lastStartNode == null || !lastStartType.equals(contTypeMap.get(tag))) {
          continue; //Cont must match previous start or continue
        }
      }
      Parse newParse1 = (Parse) p.clone(); //clone parse
      if (createDerivationString) newParse1.getDerivation().append(max).append("-");
      newParse1.setChild(originalAdvanceIndex,tag); //replace constituent being labeled to create new derivation
      newParse1.addProb(Math.log(bprob));
      //check
      //String[] context = checkContextGenerator.getContext(newParse1.getChildren(), lastStartType, lastStartIndex, advanceNodeIndex);
      checkModel.eval(checkContextGenerator.getContext(collapsePunctuation(newParse1.getChildren(),punctSet), lastStartType, lastStartIndex, advanceNodeIndex), cprobs);
      //System.out.println("check "+lastStartType+" "+cprobs[completeIndex]+" "+cprobs[incompleteIndex]+" "+tag+" "+java.util.Arrays.asList(context));
      Parse newParse2 = newParse1;
      if (cprobs[completeIndex] > q) { //make sure a reduce is likely
        newParse2 = (Parse) newParse1.clone();
        if (createDerivationString) newParse2.getDerivation().append(1).append(".");
        newParse2.addProb(Math.log(cprobs[completeIndex]));
        Parse[] cons = new Parse[advanceNodeIndex - lastStartIndex + 1];
        boolean flat = true;
        //first
        cons[0] = lastStartNode;
        flat &= cons[0].isPosTag();
        //last
        cons[advanceNodeIndex - lastStartIndex] = advanceNode;
        flat &= cons[advanceNodeIndex - lastStartIndex].isPosTag();
        //middle
        for (int ci = 1; ci < advanceNodeIndex - lastStartIndex; ci++) {
          cons[ci] = children[ci + lastStartIndex];
          flat &= cons[ci].isPosTag();
        }
        if (!flat) { //flat chunks are done by chunker
          if (lastStartIndex == 0 && advanceNodeIndex == numNodes-1) { //check for top node to include end and begining punctuation
            //System.err.println("ParserME.advanceParses: reducing entire span: "+new Span(lastStartNode.getSpan().getStart(), advanceNode.getSpan().getEnd())+" "+lastStartType+" "+java.util.Arrays.asList(children));
            newParse2.insert(new Parse(p.getText(), p.getSpan(), lastStartType, cprobs[1], headRules.getHead(cons, lastStartType)));
          }
          else {
            newParse2.insert(new Parse(p.getText(), new Span(lastStartNode.getSpan().getStart(), advanceNode.getSpan().getEnd()), lastStartType, cprobs[1], headRules.getHead(cons, lastStartType)));
          }
          newParsesList.add(newParse2);
        }
      }
      if (cprobs[incompleteIndex] > q) { //make sure a shift is likely
        if (createDerivationString) newParse1.getDerivation().append(0).append(".");
        if (advanceNodeIndex != numNodes - 1) { //can't shift last element
          newParse1.addProb(Math.log(cprobs[incompleteIndex]));
          newParsesList.add(newParse1);
        }
      }
    }
    Parse[] newParses = new Parse[newParsesList.size()];
    newParsesList.toArray(newParses);
    return newParses;
  }

  public static GISModel train(opennlp.maxent.EventStream es, int iterations, int cut) throws java.io.IOException {
    return opennlp.maxent.GIS.trainModel(iterations, new TwoPassDataIndexer(es, cut));
  }
  
  private static boolean lastChild(Parse child, Parse parent, Set punctSet) {
    Parse[] kids = collapsePunctuation(parent.getChildren(),punctSet);
    return (kids[kids.length - 1] == child);
  }
  
  private static void usage() {
    System.err.println("Usage: Parser -[dict|tag|chunk|build|check|fun] trainingFile parserModelDirectory [iterations cutoff]");
    System.err.println();
    System.err.println("Training file should be one sentence per line where each line consists of a Penn Treebank Style parse");
    System.err.println("-dict Just build the dictionaries.");
    System.err.println("-tag Just build the tagging model.");
    System.err.println("-chunk Just build the chunking model.");
    System.err.println("-build Just build the build model");
    System.err.println("-check Just build the check model");
    System.err.println("-fun Predict function tags");
  }

  /**
   * Creates a n-gram dictionary from the specified data stream using the specified head rule and specified cut-off.
   * @param data The data stream of parses.
   * @param rules The head rules for the parses.
   * @param cutoff The minimum number of entries required for the n-gram to be saved as part of the dictionary. 
   * @return A dictionary object.
   */
  private static Dictionary buildDictionary(DataStream data, HeadRules rules, int cutoff) {
    NGramModel mdict = new NGramModel();
    while(data.hasNext()) {
      String parseStr = (String) data.nextToken();
      Parse p = Parse.parseParse(parseStr);
      p.updateHeads(rules);
      Parse[] pwords = p.getTagNodes();
      String[] words = new String[pwords.length];
      //add all uni-grams
      for (int wi=0;wi<words.length;wi++) {
        words[wi] = pwords[wi].toString();
      }
      
      mdict.add(Token.create(words), 1, 1);
      //add tri-grams and bi-grams for inital sequence
      Parse[] chunks = collapsePunctuation(ParserEventStream.getInitialChunks(p),rules.getPunctuationTags());
      String[] cwords = new String[chunks.length];
      for (int wi=0;wi<cwords.length;wi++) {
        cwords[wi] = chunks[wi].getHead().toString();
      }
      mdict.add(Token.create(cwords), 2, 3);
      
      //emulate reductions to produce additional n-grams 
      int ci = 0;
      while (ci < chunks.length) {
        //System.err.println("chunks["+ci+"]="+chunks[ci].getHead().toString()+" chunks.length="+chunks.length);
        if (lastChild(chunks[ci], chunks[ci].getParent(),rules.getPunctuationTags())) {
          //perform reduce
          int reduceStart = ci;
          while (reduceStart >=0 && chunks[reduceStart].getParent() == chunks[ci].getParent()) {
            reduceStart--;
          }
          reduceStart++;
          chunks = ParserEventStream.reduceChunks(chunks,ci,chunks[ci].getParent());
          ci = reduceStart;
          if (chunks.length != 0) {
            String[] window = new String[5];
            int wi = 0;
            if (ci-2 >= 0) window[wi++] = chunks[ci-2].getHead().toString();
            if (ci-1 >= 0) window[wi++] = chunks[ci-1].getHead().toString();
            window[wi++] = chunks[ci].getHead().toString();
            if (ci+1 < chunks.length) window[wi++] = chunks[ci+1].getHead().toString();
            if (ci+2 < chunks.length) window[wi++] = chunks[ci+2].getHead().toString();
            if (wi < 5) {
              String[] subWindow = new String[wi];
              for (int swi=0;swi<wi;swi++) {
                subWindow[swi]=window[swi];
              }
              window = subWindow;
            }
            if (window.length >=3) {
              mdict.add(Token.create(window), 2, 3);
            }
            else if (window.length == 2) {
              mdict.add(Token.create(window), 2, 2);
            }
          }
          ci=reduceStart-1; //ci will be incremented at end of loop
        }
        ci++;
      }
    }
    //System.err.println("gas,and="+mdict.getCount((new TokenList(new String[] {"gas","and"}))));
    mdict.cutoff(cutoff, Integer.MAX_VALUE);
    return mdict.toDictionary(true);
  }

  public static void main(String[] args) throws java.io.IOException, InvalidFormatException {
    if (args.length < 3) {
      usage();
      System.exit(1);
    }
    boolean dict = false; 
    boolean tag = false;
    boolean chunk = false;
    boolean build = false;
    boolean check = false;
    boolean fun = false;
    boolean all = true;
    int argIndex = 0;
    while (args[argIndex].startsWith("-")) {
      all = false;
      if (args[argIndex].equals("-dict")) {
        dict = true;
      }
      else if (args[argIndex].equals("-tag")) {
        tag = true;
      }
      else if (args[argIndex].equals("-chunk")) {
        chunk = true;
      }
      else if (args[argIndex].equals("-build")) {
        build = true;
      }
      else if (args[argIndex].equals("-check")) {
        check = true;
      }
      else if (args[argIndex].equals("-fun")) {
        fun = true;
      }
      else if (args[argIndex].equals("--")) {
        argIndex++;
        break;
      }
      else {
        System.err.println("Invalid option " + args[argIndex]);
        usage();
        System.exit(1);
      }
      argIndex++;
    }
    java.io.File inFile = new java.io.File(args[argIndex++]);
    String modelDirectory = args[argIndex++];
    HeadRules rules = new opennlp.tools.lang.english.HeadRules(modelDirectory+"/head_rules");
    java.io.File dictFile = new java.io.File(modelDirectory+"/dict.bin.gz");
    java.io.File tagFile = new java.io.File(modelDirectory+"/tag.bin.gz");
    java.io.File chunkFile = new java.io.File(modelDirectory+"/chunk.bin.gz");
    java.io.File buildFile = new java.io.File(modelDirectory+"/build.bin.gz");
    java.io.File checkFile = new java.io.File(modelDirectory+"/check.bin.gz");
    int iterations = 100;
    int cutoff = 5;
    if (args.length > argIndex) {
      iterations = Integer.parseInt(args[argIndex++]);
      cutoff = Integer.parseInt(args[argIndex++]);
    }
    if (fun) {
      Parse.useFunctionTags(true);
    }
    if (dict || all) {
      System.err.println("Building dictionary");
      DataStream data = new opennlp.maxent.PlainTextByLineDataStream(new java.io.FileReader(inFile));
      Dictionary mdict = buildDictionary(data, rules, cutoff);
      System.out.println("Saving the dictionary");
      mdict.serialize(new FileOutputStream(dictFile));
    }
    if (tag || all) {
      System.err.println("Training tagger");
      //System.err.println("Loading Dictionary");
      //Dictionary tridict = new Dictionary(dictFile.toString());
      opennlp.maxent.EventStream tes = new ParserEventStream(new opennlp.maxent.PlainTextByLineDataStream(new java.io.FileReader(inFile)), rules, ParserEventTypeEnum.TAG);
      GISModel tagModel = train(tes, iterations, cutoff);
      System.out.println("Saving the tagger model as: " + tagFile);
      new opennlp.maxent.io.SuffixSensitiveGISModelWriter(tagModel, tagFile).persist();
    }

    if (chunk || all) {
      System.err.println("Training chunker");
      opennlp.maxent.EventStream ces = new ParserEventStream(new opennlp.maxent.PlainTextByLineDataStream(new java.io.FileReader(inFile)), rules, ParserEventTypeEnum.CHUNK);
      GISModel chunkModel = train(ces, iterations, cutoff);
      System.out.println("Saving the chunker model as: " + chunkFile);
      new opennlp.maxent.io.SuffixSensitiveGISModelWriter(chunkModel, chunkFile).persist();
    }

    if (build || all) {
      System.err.println("Loading Dictionary");
      Dictionary tridict = new Dictionary(new FileInputStream(dictFile.toString()),true);
      System.err.println("Training builder");
      opennlp.maxent.EventStream bes = new ParserEventStream(new opennlp.maxent.PlainTextByLineDataStream(new java.io.FileReader(inFile)), rules, ParserEventTypeEnum.BUILD,tridict);
      GISModel buildModel = train(bes, iterations, cutoff);
      System.out.println("Saving the build model as: " + buildFile);
      new opennlp.maxent.io.SuffixSensitiveGISModelWriter(buildModel, buildFile).persist();
    }

    if (check || all) {
      System.err.println("Training checker");
      opennlp.maxent.EventStream kes = new ParserEventStream(new opennlp.maxent.PlainTextByLineDataStream(new java.io.FileReader(inFile)), rules, ParserEventTypeEnum.CHECK);
      GISModel checkModel = train(kes, iterations, cutoff);
      System.out.println("Saving the check model as: " + checkFile);
      new opennlp.maxent.io.SuffixSensitiveGISModelWriter(checkModel, checkFile).persist();
    }
  }
}
