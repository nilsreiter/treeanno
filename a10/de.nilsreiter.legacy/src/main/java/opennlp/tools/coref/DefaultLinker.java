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

import opennlp.tools.coref.mention.MentionContext;
import opennlp.tools.coref.mention.PTBHeadFinder;
import opennlp.tools.coref.mention.ShallowParseMentionFinder;
import opennlp.tools.coref.resolver.AbstractResolver;
import opennlp.tools.coref.resolver.CommonNounResolver;
import opennlp.tools.coref.resolver.DefiniteNounResolver;
import opennlp.tools.coref.resolver.FixedNonReferentialResolver;
import opennlp.tools.coref.resolver.IsAResolver;
import opennlp.tools.coref.resolver.MaxentResolver;
import opennlp.tools.coref.resolver.NonReferentialResolver;
import opennlp.tools.coref.resolver.PerfectResolver;
import opennlp.tools.coref.resolver.PluralNounResolver;
import opennlp.tools.coref.resolver.PluralPronounResolver;
import opennlp.tools.coref.resolver.ProperNounResolver;
import opennlp.tools.coref.resolver.ResolverMode;
import opennlp.tools.coref.resolver.SingularPronounResolver;
import opennlp.tools.coref.resolver.SpeechPronounResolver;
import opennlp.tools.coref.sim.Gender;
import opennlp.tools.coref.sim.MaxentCompatibilityModel;
import opennlp.tools.coref.sim.Number;
import opennlp.tools.coref.sim.SimilarityModel;

/**
 * This class perform coreference for treebank style parses or for noun-phrase chunked data.    
 * Non-constituent entites such as pre-nominal named-entities and sub entities in simple coordinated
 * noun phases will be created.  This linker requires that named-entity information also be provided.   
 * This information can be added to the parse using the -parse option with EnglishNameFinder. 
 */
public class DefaultLinker extends AbstractLinker {
  
  protected MaxentCompatibilityModel mcm;
  
  /**
   * Creates a new linker with the specified model directory, running in the specified mode.
   * @param modelDirectory The directory where the models for this linker are kept.
   * @param mode The mode that this linker is running in.
   * @throws IOException when the models can not be read or written to based on the mode.
   */
  public DefaultLinker(String modelDirectory, LinkerMode mode) throws IOException {
    this(modelDirectory,mode,true,-1);
  }
  
  /**
   * Creates a new linker with the specified model directory, running in the specified mode which uses a discourse model
   * based on the specified parameter.
   * @param modelDirectory The directory where the models for this linker are kept.
   * @param mode The mode that this linker is running in.
   * @param useDiscourseModel Whether the model should use a discourse model or not.
   * @throws IOException when the models can not be read or written to based on the mode.
   */
  public DefaultLinker(String modelDirectory, LinkerMode mode, boolean useDiscourseModel) throws IOException {
    this(modelDirectory,mode,useDiscourseModel,-1);
  }
  
  /**
   * Creates a new linker with the specified model directory, running in the specified mode which uses a discourse model
   * based on the specified parameter and uses the specified fixed non-referential probability.
   * @param modelDirectory The directory where the models for this linker are kept.
   * @param mode The mode that this linker is running in.
   * @param useDiscourseModel Whether the model should use a discourse model or not.
   * @param fixedNonReferentialProbability The probability which resolvers are required to exceed to positi a coreference relationship.
   * @throws IOException when the models can not be read or written to based on the mode.
   */
  public DefaultLinker(String modelDirectory, LinkerMode mode, boolean useDiscourseModel, double fixedNonReferentialProbability) throws IOException {
    super(modelDirectory, mode, useDiscourseModel);
    if (mode != LinkerMode.SIM) {
      mcm = new MaxentCompatibilityModel(corefProject);
    }
    initHeadFinder();
    initMentionFinder();
    if (mode != LinkerMode.SIM) {
      initResolvers(mode, fixedNonReferentialProbability);
      entities = new DiscourseEntity[resolvers.length];
    }
  }
  
  /**
   * Initializes the resolvers used by this linker.
   * @param mode The mode in which this linker is being used.
   * @param fixedNonReferentialProbability 
   * @throws IOException
   */
  protected void initResolvers(LinkerMode mode, double fixedNonReferentialProbability) throws IOException {
    if (mode == LinkerMode.TRAIN) {
      mentionFinder.setPrenominalNamedEntityCollection(false);
      mentionFinder.setCoordinatedNounPhraseCollection(false);
    }
    SINGULAR_PRONOUN = 0;
    if (LinkerMode.TEST == mode || LinkerMode.EVAL == mode) {
      if (fixedNonReferentialProbability < 0) {
        resolvers = new MaxentResolver[] {
            new SingularPronounResolver(corefProject, ResolverMode.TEST),
            new ProperNounResolver(corefProject, ResolverMode.TEST),
            new DefiniteNounResolver(corefProject, ResolverMode.TEST),
            new IsAResolver(corefProject, ResolverMode.TEST),
            new PluralPronounResolver(corefProject, ResolverMode.TEST),
            new PluralNounResolver(corefProject, ResolverMode.TEST),
            new CommonNounResolver(corefProject, ResolverMode.TEST),
            new SpeechPronounResolver(corefProject, ResolverMode.TEST)
        };
      }
      else {
        NonReferentialResolver nrr = new FixedNonReferentialResolver(fixedNonReferentialProbability);        
        resolvers = new MaxentResolver[] {
            new SingularPronounResolver(corefProject, ResolverMode.TEST,nrr),
            new ProperNounResolver(corefProject, ResolverMode.TEST,nrr),
            new DefiniteNounResolver(corefProject, ResolverMode.TEST,nrr),
            new IsAResolver(corefProject, ResolverMode.TEST,nrr),
            new PluralPronounResolver(corefProject, ResolverMode.TEST,nrr),
            new PluralNounResolver(corefProject, ResolverMode.TEST,nrr),
            new CommonNounResolver(corefProject, ResolverMode.TEST,nrr),
            new SpeechPronounResolver(corefProject, ResolverMode.TEST,nrr)
        };
      }
      if (LinkerMode.EVAL == mode) {
        //String[] names = {"Pronoun", "Proper", "Def-NP", "Is-a", "Plural Pronoun"};
        //eval = new Evaluation(names);
      }
      MaxentResolver.setSimilarityModel(SimilarityModel.testModel(corefProject + "/sim"));
    }
    else if (LinkerMode.TRAIN == mode) {
      resolvers = new AbstractResolver[9];
      resolvers[0] = new SingularPronounResolver(corefProject, ResolverMode.TRAIN);
      resolvers[1] = new ProperNounResolver(corefProject, ResolverMode.TRAIN);
      resolvers[2] = new DefiniteNounResolver(corefProject, ResolverMode.TRAIN);
      resolvers[3] = new IsAResolver(corefProject, ResolverMode.TRAIN);
      resolvers[4] = new PluralPronounResolver(corefProject, ResolverMode.TRAIN);
      resolvers[5] = new PluralNounResolver(corefProject, ResolverMode.TRAIN);
      resolvers[6] = new CommonNounResolver(corefProject, ResolverMode.TRAIN);
      resolvers[7] = new SpeechPronounResolver(corefProject, ResolverMode.TRAIN);
      resolvers[8] = new PerfectResolver();
    }
    else {
      System.err.println("DefaultLinker: Invalid Mode");
    }
  }

  /**
   * Initializes the head finder for this linker.
   */
  protected void initHeadFinder() {
    headFinder = PTBHeadFinder.getInstance();
  }
  /**
   * Initializes the mention finder for this linker.  
   * This can be over-ridden to change the space of mentions used for coreference. 
   */
  protected void initMentionFinder() {
    mentionFinder = ShallowParseMentionFinder.getInstance(headFinder);
  }

  protected Gender computeGender(MentionContext mention) {
    return mcm.computeGender(mention);
  }

  protected Number computeNumber(MentionContext mention) {
    return mcm.computeNumber(mention);
  }
  
  
}