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
package opennlp.tools.coref;

import java.io.IOException;

import opennlp.tools.coref.mention.HeadFinder;
import opennlp.tools.coref.mention.Mention;
import opennlp.tools.coref.mention.MentionContext;
import opennlp.tools.coref.mention.MentionFinder;
import opennlp.tools.coref.mention.Parse;
import opennlp.tools.coref.resolver.AbstractResolver;
import opennlp.tools.coref.sim.Gender;
import opennlp.tools.coref.sim.Number;

/** 
 * Provides a default implementation of many of the methods in {@link Linker} that
 * most implementations of {@link Linker} will want to extend.  
 */
public abstract class AbstractLinker implements Linker {

  /** The mention finder used to find mentions. */ 
  protected MentionFinder mentionFinder;

  /** Specifies whether debug print is generated. */
  protected boolean debug = true;

  /** The mode in which this linker is running. */
  protected LinkerMode mode;

  /** Instance used for for returning the same linker for subsequent getInstance requests. */
  protected static Linker linker;
  
  /** The resolvers used by this Linker. */
  protected AbstractResolver[] resolvers;
  /** The names of the resolvers used by this Linker. */
  protected String[] resolverNames;
  
  /** Array used to store the results of each call made to the linker. */  
  protected DiscourseEntity[] entities;
  
  /** The index of resolver which is used for singular pronouns. */
  protected int SINGULAR_PRONOUN;

  /** The name of the project where the coreference models are stored. */ 
  protected String corefProject;
  
  /** The head finder used in this linker. */
  protected HeadFinder headFinder;
  
  /** Specifies whether coreferent mentions should be combined into a single entity. 
   * Set this to true to combine them, false otherwise.  */
  protected boolean useDiscourseModel;
  
  /** Specifies whether mentions for which no resolver can be used should be added to the
   * discourse model.
   */ 
  protected boolean removeUnresolvedMentions;

  /** 
   * Creates a new linker using the models in the specified project directory and using the specified mode.
   * @param project The location of the models or other data needed by this linker.
   * @param mode The mode the linker should be run in: testing, training, or evaluation.
   */
  public AbstractLinker(String project, LinkerMode mode) {
    this(project,mode,true);
  }

  /**
   * Creates a new linker using the models in the specified project directory, using the specified mode, 
   * and combining coreferent entities based on the specified value.
   * @param project The location of the models or other data needed by this linker.
   * @param mode The mode the linker should be run in: testing, training, or evaluation.
   * @param useDiscourseModel Specifies whether coreferent mention should be combined or not.
   */
  public AbstractLinker(String project, LinkerMode mode,boolean useDiscourseModel) {
    this.corefProject = project;
    this.mode = mode;
    SINGULAR_PRONOUN = -1;
    this.useDiscourseModel = useDiscourseModel;
    removeUnresolvedMentions = true;
  }

  /**
   * Removes the specified mention to an entity in the specified discourse model or creates a new entity for the mention.
   * @param mention The mention to resolve.
   * @param discourseModel The discource model of existing entities.
   */
  protected void resolve(MentionContext mention, DiscourseModel discourseModel) {
    //System.err.println("AbstractLinker.resolve: "+mode+"("+econtext.id+") "+econtext.toText());
    boolean validEntity = true; // true if we should add this entity to the dm
    boolean canResolve = false;
    
    for (int ri = 0; ri < resolvers.length; ri++) {
      if (resolvers[ri].canResolve(mention)) {
        if (mode == LinkerMode.TEST) {
          entities[ri] = resolvers[ri].resolve(mention, discourseModel);
          canResolve = true;
        }
        else if (mode == LinkerMode.TRAIN) {
          entities[ri] = resolvers[ri].retain(mention, discourseModel);
          if (ri+1 != resolvers.length) {
            canResolve = true;
          }
        }
        else if (mode == LinkerMode.EVAL) {
          entities[ri] = resolvers[ri].retain(mention, discourseModel);
          //DiscourseEntity rde = resolvers[ri].resolve(mention, discourseModel);
          //eval.update(rde == entities[ri], ri, entities[ri], rde);
        }
        else {
          System.err.println("AbstractLinker.Unknown mode: " + mode);
        }
        if (ri == SINGULAR_PRONOUN && entities[ri] == null) {
          validEntity = false;
        }
      }
      else {
        entities[ri] = null;
      }
    }
    if (!canResolve && removeUnresolvedMentions) {
      //System.err.println("No resolver for: "+econtext.toText()+ " head="+econtext.headTokenText+" "+econtext.headTokenTag);
      validEntity = false;
    }
    DiscourseEntity de = checkForMerges(discourseModel, entities);
    if (validEntity) {
      updateExtent(discourseModel, mention, de,useDiscourseModel);
    }
  }
  
  public HeadFinder getHeadFinder() {
    return headFinder;
  }
  
  /**
   * Updates the specified discourse model with the specified mention as coreferent with the specified entity. 
   * @param dm The discourse model
   * @param mention The mention to be added to the specified entity.
   * @param entity The entity which is mentioned by the specified mention.  
   * @param useDiscourseModel Whether the mentions should be kept as an entiy or simply co-indexed.
   */
  protected void updateExtent(DiscourseModel dm, MentionContext mention, DiscourseEntity entity, boolean useDiscourseModel) {
    if (useDiscourseModel) {
      if (entity != null) {
        //System.err.println("AbstractLinker.updateExtent: addingExtent:
        // "+econtext.toText());
        if (entity.getGenderProbability() < mention.getGenderProb()) {
          entity.setGender(mention.getGender());
          entity.setGenderProbability(mention.getGenderProb());
        }
        if (entity.getNumberProbability() < mention.getNumberProb()) {
          entity.setNumber(mention.getNumber());
          entity.setNumberProbability(mention.getNumberProb());
        }
        entity.addMention(mention);
        dm.mentionEntity(entity);
      }
      else {
        //System.err.println("AbstractLinker.updateExtent: creatingExtent:
        // "+econtext.toText()+" "+econtext.gender+" "+econtext.number);
        entity = new DiscourseEntity(mention, mention.getGender(), mention.getGenderProb(), mention.getNumber(), mention.getNumberProb());
        dm.addEntity(entity);
      }
    }
    else {
      if (entity != null) {
        DiscourseEntity newEntity = new DiscourseEntity(mention, mention.getGender(), mention.getGenderProb(), mention.getNumber(), mention.getNumberProb());
        dm.addEntity(newEntity);
        newEntity.setId(entity.getId());
      }
      else {
        DiscourseEntity newEntity = new DiscourseEntity(mention, mention.getGender(), mention.getGenderProb(), mention.getNumber(), mention.getNumberProb());
        dm.addEntity(newEntity);
      }
    }
    //System.err.println(de1);
  }

  protected DiscourseEntity checkForMerges(DiscourseModel dm, DiscourseEntity[] des) {
    DiscourseEntity de1; //tempory variable
    DiscourseEntity de2; //tempory variable
    de1 = des[0];
    for (int di = 1; di < des.length; di++) {
      de2 = des[di];
      if (de2 != null) {
        if (de1 != null && de1 != de2) {
          dm.mergeEntities(de1, de2, 1);
        }
        else {
          de1 = de2;
        }
      }
    }
    return (de1);
  }

  public DiscourseEntity[] getEntities(Mention[] mentions) {
    MentionContext[] extentContexts = this.constructMentionContexts(mentions);
    DiscourseModel dm = new DiscourseModel();
    for (int ei = 0; ei < extentContexts.length; ei++) {
      //System.err.println(ei+" "+extentContexts[ei].toText());
      resolve(extentContexts[ei], dm);
    }
    return (dm.getEntities());
  }

  public void setEntities(Mention[] mentions) {
    getEntities(mentions);
  }

  public void train() throws IOException {
    for (int ri = 0; ri < resolvers.length; ri++) {
      resolvers[ri].train();
    }
  }
    
  public MentionFinder getMentionFinder() {
    return mentionFinder;
  }
  
  public MentionContext[] constructMentionContexts(Mention[] mentions) {
    int mentionInSentenceIndex=-1;
    int numMentionsInSentence=-1;
    int prevSentenceIndex = -1;
    MentionContext[] contexts = new MentionContext[mentions.length];
    for (int mi=0,mn=mentions.length;mi<mn;mi++) {
      Parse mentionParse = mentions[mi].getParse();
      //System.err.println("AbstractLinker.constructMentionContexts: mentionParse="+mentionParse);
      if (mentionParse == null) {
        System.err.println("no parse for "+mentions[mi]);
      }
      int sentenceIndex = mentionParse.getSentenceNumber();
      if (sentenceIndex != prevSentenceIndex) {
        mentionInSentenceIndex=0;
        prevSentenceIndex = sentenceIndex;
        numMentionsInSentence = 0;
        for (int msi=mi;msi<mentions.length;msi++) {
          if (sentenceIndex != mentions[msi].getParse().getSentenceNumber()) {
            break;
          }
          numMentionsInSentence++;
        }
      }
      contexts[mi]=new MentionContext(mentions[mi], mentionInSentenceIndex, numMentionsInSentence, mi, sentenceIndex, getHeadFinder());
      //System.err.println("AbstractLinker.constructMentionContexts: mi="+mi+" sn="+mentionParse.getSentenceNumber()+" extent="+mentions[mi]+" parse="+mentionParse.getSpan()+" mc="+contexts[mi].toText());
      contexts[mi].setId(mentions[mi].getId());
      mentionInSentenceIndex++;
      if (mode != LinkerMode.SIM) {
        Gender g  = computeGender(contexts[mi]);
        contexts[mi].setGender(g.getType(),g.getConfidence());
        Number n = computeNumber(contexts[mi]);
        contexts[mi].setNumber(n.getType(),n.getConfidence());
      }
    }
    return (contexts);
  }
  
  protected abstract Gender computeGender(MentionContext mention);
  protected abstract Number computeNumber(MentionContext mention);
}
