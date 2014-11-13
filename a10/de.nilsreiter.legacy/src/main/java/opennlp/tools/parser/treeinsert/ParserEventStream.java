///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2006 Thomas Morton
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

package opennlp.tools.parser.treeinsert;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opennlp.maxent.DataStream;
import opennlp.maxent.Event;
import opennlp.maxent.GISModel;
import opennlp.maxent.io.SuffixSensitiveGISModelReader;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.parser.AbstractBottomUpParser;
import opennlp.tools.parser.AbstractParserEventStream;
import opennlp.tools.parser.HeadRules;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserEventTypeEnum;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

public class ParserEventStream extends AbstractParserEventStream {

  protected AttachContextGenerator attachContextGenerator;
  protected BuildContextGenerator buildContextGenerator;
  protected CheckContextGenerator checkContextGenerator;
  
  private static final boolean debug = false;
  
  public ParserEventStream(DataStream d, HeadRules rules, ParserEventTypeEnum etype, Dictionary dict) {
    super(d, rules, etype, dict);
  }
  
  public void init() {
    buildContextGenerator = new BuildContextGenerator();
    attachContextGenerator = new AttachContextGenerator(punctSet);
    checkContextGenerator = new CheckContextGenerator(punctSet);
  }

  public ParserEventStream(DataStream d, HeadRules rules, ParserEventTypeEnum etype) {
    super(d, rules, etype);
  }
  
  /**
   * Returns a set of parent nodes which consist of the immediate
   * parent of the specified node and any of its parent which 
   * share the same syntactic type.
   * @param node The node whose parents are to be returned.
   * @return a set of parent nodes.
   */
  private Map getNonAdjoinedParent(Parse node) {
    Map parents = new HashMap();
    Parse parent = node.getParent();
    int index = indexOf(node,parent);
    parents.put(parent,new Integer(index));
    while(parent.getType().equals(node.getType())) {
      node = parent;
      parent = parent.getParent();
      index = indexOf(node,parent);
      parents.put(parent,new Integer(index));
    }
    return parents;
  }

  private int indexOf(Parse child, Parse parent) {
    Parse[] kids = Parser.collapsePunctuation(parent.getChildren(),punctSet);
    for (int ki=0;ki<kids.length;ki++) {
      if (child == kids[ki]) {
        return ki;
      }
    }
    return -1;
  }
  
  private int nonPunctChildCount(Parse node) {
    return Parser.collapsePunctuation(node.getChildren(),punctSet).length;
  }
  /*  
  private Set getNonAdjoinedParent(Parse node) {
    Set parents = new HashSet();
    Parse parent = node.getParent();
    do {
      parents.add(parent);
      parent = parent.getParent();
    }
    while(parent.getType().equals(node.getType()));
    return parents;
  }
  */
  
  protected boolean lastChild(Parse child, Parse parent) {
    boolean lc = super.lastChild(child, parent);
    while(!lc) {
      Parse cp = child.getParent();
      if (cp != parent && cp.getType().equals(child.getType())) {
        lc = super.lastChild(cp,parent);
        child = cp;
      }
      else {
        break;
      }
    }
    return lc;
  }

  protected void addParseEvents(List parseEvents, Parse[] chunks) {
    /** Frontier nodes built from node in a completed parse.  Specifically,
     * they have all their children regardless of the stage of parsing.*/
    List rightFrontier = new ArrayList();
    List builtNodes = new ArrayList();
    /** Nodes which characterize what the parse looks like to the parser as its being built.
     * Specifically, these nodes don't have all their chilren attached like the parents of
     * the chunk nodes do.*/
    Parse[] currentChunks = new Parse[chunks.length];
    for (int ci=0;ci<chunks.length;ci++) {
      currentChunks[ci] = (Parse) chunks[ci].clone();
      currentChunks[ci].setPrevPunctuation(chunks[ci].getPreviousPunctuationSet());
      currentChunks[ci].setNextPunctuation(chunks[ci].getNextPunctuationSet());
      currentChunks[ci].setLabel(Parser.COMPLETE);
      chunks[ci].setLabel(Parser.COMPLETE);
    }
    for (int ci=0;ci<chunks.length;ci++) {
      //System.err.println("parserEventStream.addParseEvents: chunks="+Arrays.asList(chunks));
      Parse parent = chunks[ci].getParent();
      Parse prevParent = chunks[ci];
      int off = 0;
      //build un-built parents
      if (!chunks[ci].isPosTag()) {
        builtNodes.add(off++,chunks[ci]);
      }
      //perform build stages
      while (!parent.getType().equals(AbstractBottomUpParser.TOP_NODE) && parent.getLabel() == null) {
        if (parent.getLabel() == null && !prevParent.getType().equals(parent.getType())) {
          //build level
          if (debug) System.err.println("Build: "+parent.getType()+" for: "+currentChunks[ci]);
          if (etype == ParserEventTypeEnum.BUILD) {
            parseEvents.add(new Event(parent.getType(), buildContextGenerator.getContext(currentChunks, ci)));            
          }
          builtNodes.add(off++,parent);
          Parse newParent = new Parse(currentChunks[ci].getText(),currentChunks[ci].getSpan(),parent.getType(),1,0);
          newParent.add(currentChunks[ci],rules);
          newParent.setPrevPunctuation(currentChunks[ci].getPreviousPunctuationSet());
          newParent.setNextPunctuation(currentChunks[ci].getNextPunctuationSet());
          currentChunks[ci].setParent(newParent);
          currentChunks[ci] = newParent;
          newParent.setLabel(Parser.BUILT);
          //see if chunk is complete
          if (lastChild(chunks[ci], parent)) {
            if (etype == ParserEventTypeEnum.CHECK) {
              parseEvents.add(new Event(Parser.COMPLETE, checkContextGenerator.getContext(currentChunks[ci],currentChunks, ci,false)));
            }
            currentChunks[ci].setLabel(Parser.COMPLETE);
            parent.setLabel(Parser.COMPLETE);
          }
          else {
            if (etype == ParserEventTypeEnum.CHECK) {
              parseEvents.add(new Event(Parser.INCOMPLETE, checkContextGenerator.getContext(currentChunks[ci],currentChunks,ci,false)));
            }
            currentChunks[ci].setLabel(Parser.INCOMPLETE);
            parent.setLabel(Parser.COMPLETE);
          }
          
          chunks[ci] = parent;
          //System.err.println("build: "+newParent+" for "+parent);
        }
        //TODO: Consider whether we need to set this label or train parses at all.
        parent.setLabel(Parser.BUILT);
        prevParent = parent;
        parent = parent.getParent();
      }
      //decide to attach
      if (etype == ParserEventTypeEnum.BUILD) {
        parseEvents.add(new Event(Parser.DONE, buildContextGenerator.getContext(currentChunks, ci)));
      }
      //attach node
      String attachType = null;
      /** Node selected for attachment. */
      Parse attachNode = null;
      int attachNodeIndex = -1;
      if (ci == 0){
        Parse top = new Parse(currentChunks[ci].getText(),new Span(0,currentChunks[ci].getText().length()),AbstractBottomUpParser.TOP_NODE,1,0);
        top.insert(currentChunks[ci]);
      }
      else {
        /** Right frontier consisting of partially-built nodes based on current state of the parse.*/
        List currentRightFrontier = Parser.getRightFrontier(currentChunks[0],punctSet);
        if (currentRightFrontier.size() != rightFrontier.size()) {
          System.err.println("fontiers mis-aligned: "+currentRightFrontier.size()+" != "+rightFrontier.size()+" "+currentRightFrontier+" "+rightFrontier);
          System.exit(1);
        }
        Map parents = getNonAdjoinedParent(chunks[ci]);
        //try daughters first.
        for (int cfi=0;cfi<currentRightFrontier.size();cfi++) {
          Parse frontierNode = (Parse) rightFrontier.get(cfi);
          Parse cfn = (Parse) currentRightFrontier.get(cfi);
          if (!Parser.checkComplete || !Parser.COMPLETE.equals(cfn.getLabel())) {
            Integer i = (Integer) parents.get(frontierNode);
            if (debug) System.err.println("Looking at attachment site ("+cfi+"): "+cfn.getType()+" ci="+i+" cs="+nonPunctChildCount(cfn)+", "+cfn+" :for "+currentChunks[ci].getType()+" "+currentChunks[ci]+" -> "+parents);
            if (attachNode == null &&  i != null && i.intValue() == nonPunctChildCount(cfn)) {
              attachType = Parser.ATTACH_DAUGHTER;
              attachNodeIndex = cfi;
              attachNode = cfn;
              if (etype == ParserEventTypeEnum.ATTACH) {
                parseEvents.add(new Event(attachType, attachContextGenerator.getContext(currentChunks, ci, currentRightFrontier, attachNodeIndex)));
              }
              //System.err.println("daughter attach "+attachNode+" at "+fi);
            }
          }
          else {
            if (debug) System.err.println("Skipping ("+cfi+"): "+cfn.getType()+","+cfn.getPreviousPunctuationSet()+" "+cfn+" :for "+currentChunks[ci].getType()+" "+currentChunks[ci]+" -> "+parents);
          }
          // Can't attach past first incomplete node.
          if (Parser.checkComplete && cfn.getLabel().equals(Parser.INCOMPLETE)) {
            if (debug) System.err.println("breaking on incomplete:"+cfn.getType()+" "+cfn);
            break;
          }
        }
        //try sisters, and generate non-attach events.
        for (int cfi=0;cfi<currentRightFrontier.size();cfi++) {
          Parse frontierNode = (Parse) rightFrontier.get(cfi);
          Parse cfn = (Parse) currentRightFrontier.get(cfi);
          if (attachNode == null && parents.containsKey(frontierNode.getParent()) 
              && frontierNode.getType().equals(frontierNode.getParent().getType()) 
              ){ //&& frontierNode.getParent().getLabel() == null) {
            attachType = Parser.ATTACH_SISTER;
            attachNode = cfn;
            attachNodeIndex = cfi;
            if (etype == ParserEventTypeEnum.ATTACH) {
              parseEvents.add(new Event(Parser.ATTACH_SISTER, attachContextGenerator.getContext(currentChunks, ci, currentRightFrontier, cfi)));
            }
            chunks[ci].getParent().setLabel(Parser.BUILT);
            //System.err.println("in search sister attach "+attachNode+" at "+cfi);
          }
          else if (cfi == attachNodeIndex) {
            //skip over previously attached daughter.
          }
          else {
            if (etype == ParserEventTypeEnum.ATTACH) {
              parseEvents.add(new Event(Parser.NON_ATTACH, attachContextGenerator.getContext(currentChunks, ci, currentRightFrontier, cfi)));
            }
          }
          //Can't attach past first incomplete node.
          if (Parser.checkComplete && cfn.getLabel().equals(Parser.INCOMPLETE)) {
            if (debug) System.err.println("breaking on incomplete:"+cfn.getType()+" "+cfn);
            break;
          }
        }
        //attach Node
        if (attachNode != null) {
          if (attachType == Parser.ATTACH_DAUGHTER) {
            Parse daughter = currentChunks[ci];
            if (debug) System.err.println("daughter attach a="+attachNode.getType()+":"+attachNode+" d="+daughter+" com="+lastChild(chunks[ci], (Parse) rightFrontier.get(attachNodeIndex)));
            attachNode.add(daughter,rules);
            daughter.setParent(attachNode);
            if (lastChild(chunks[ci], (Parse) rightFrontier.get(attachNodeIndex))) {
              if (etype == ParserEventTypeEnum.CHECK) {
                parseEvents.add(new Event(Parser.COMPLETE, checkContextGenerator.getContext(attachNode,currentChunks,ci,true)));
              }
              attachNode.setLabel(Parser.COMPLETE);
            }
            else {
              if (etype == ParserEventTypeEnum.CHECK) {
                parseEvents.add(new Event(Parser.INCOMPLETE, checkContextGenerator.getContext(attachNode,currentChunks,ci,true)));
              }
            }
          }
          else if (attachType == Parser.ATTACH_SISTER) {
            Parse frontierNode = (Parse) rightFrontier.get(attachNodeIndex);
            rightFrontier.set(attachNodeIndex,frontierNode.getParent());
            Parse sister = currentChunks[ci];
            if (debug) System.err.println("sister attach a="+attachNode.getType()+":"+attachNode+" s="+sister+" ap="+attachNode.getParent()+" com="+lastChild(chunks[ci], (Parse) rightFrontier.get(attachNodeIndex)));
            Parse newParent = attachNode.getParent().adjoin(sister,rules);
            
            newParent.setParent(attachNode.getParent());
            attachNode.setParent(newParent);
            sister.setParent(newParent);
            if (attachNode == currentChunks[0]) {
              currentChunks[0]= newParent;
            }
            if (lastChild(chunks[ci], (Parse) rightFrontier.get(attachNodeIndex))) {
              if (etype == ParserEventTypeEnum.CHECK) {
                parseEvents.add(new Event(Parser.COMPLETE, checkContextGenerator.getContext(newParent,currentChunks,ci,true)));
              }
              newParent.setLabel(Parser.COMPLETE);
            }
            else {
              if (etype == ParserEventTypeEnum.CHECK) {
                parseEvents.add(new Event(Parser.INCOMPLETE, checkContextGenerator.getContext(newParent,currentChunks,ci,true)));
              }
              newParent.setLabel(Parser.INCOMPLETE);
            }

          }
          //update right frontier
          for (int ni=0;ni<attachNodeIndex;ni++) {
            //System.err.println("removing: "+rightFrontier.get(0));
            rightFrontier.remove(0);
          }
        }
        else {
          //System.err.println("No attachment!");
          throw new RuntimeException("No Attachment: "+chunks[ci]);
        }
      }
      rightFrontier.addAll(0,builtNodes);
      builtNodes.clear();
    }
  }
  
  public static void main(String[] args) throws java.io.IOException, InvalidFormatException {
    if (args.length == 0) {
      System.err.println("Usage ParserEventStream -[tag|chunk|build|attach] [-fun] [-dict dictionary] [-model model] head_rules < parses");
      System.exit(1);
    }
    ParserEventTypeEnum etype = null;
    boolean fun = false;
    int ai = 0;
    Dictionary dict = null;
    GISModel model = null;

    while (ai < args.length && args[ai].startsWith("-")) {
      if (args[ai].equals("-build")) {
        etype = ParserEventTypeEnum.BUILD;
      }
      else if (args[ai].equals("-attach")) {
        etype = ParserEventTypeEnum.ATTACH;
      }
      else if (args[ai].equals("-chunk")) {
        etype = ParserEventTypeEnum.CHUNK;
      }
      else if (args[ai].equals("-check")) {
        etype = ParserEventTypeEnum.CHECK;
      }
      else if (args[ai].equals("-tag")) {
        etype = ParserEventTypeEnum.TAG;
      }
      else if (args[ai].equals("-fun")) {
        fun = true;
      }
      else if (args[ai].equals("-dict")) {
        ai++;
        dict = new Dictionary(new FileInputStream(args[ai]));
      }
      else if (args[ai].equals("-model")) {
        ai++;
        model = (new SuffixSensitiveGISModelReader(new File(args[ai]))).getModel();
      }
      else {
        System.err.println("Invalid option " + args[ai]);
        System.exit(1);
      }
      ai++;
    }
    HeadRules rules = new opennlp.tools.lang.english.HeadRules(args[ai++]);
    if (fun) {
      Parse.useFunctionTags(true);
    }
    opennlp.maxent.EventStream es = new ParserEventStream(new opennlp.maxent.PlainTextByLineDataStream(new java.io.InputStreamReader(System.in)), rules, etype, dict);
    while (es.hasNext()) {
      Event e = es.nextEvent();
      if (model != null) {
        System.out.print(model.eval(e.getContext())[model.getIndex(e.getOutcome())]+" ");
      }
      System.out.println(e);
    }
  }
}