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
package opennlp.tools.coref.resolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.coref.DiscourseEntity;
import opennlp.tools.coref.Linker;
import opennlp.tools.coref.mention.MentionContext;

/**
 * Resolves pronouns specific to quoted speech such as "you", "me", and "I".  
 */
public class SpeechPronounResolver extends MaxentResolver {

  public SpeechPronounResolver(String projectName, ResolverMode m) throws IOException {
    super(projectName,"fmodel", m, 30);
    this.numSentencesBack = 0;
    showExclusions = false;
    preferFirstReferent = true;
  }
  
  public SpeechPronounResolver(String projectName, ResolverMode m, NonReferentialResolver nrr) throws IOException {
    super(projectName,"fmodel", m, 30,nrr);
    showExclusions = false;
    preferFirstReferent = true;
  }


  protected List getFeatures(MentionContext mention, DiscourseEntity entity) {
    List features = new ArrayList();
    features.addAll(super.getFeatures(mention, entity));
    if (entity != null) {
      features.addAll(getPronounMatchFeatures(mention,entity));
      List contexts = getContextFeatures(mention);
      MentionContext cec = entity.getLastExtent();
      if (mention.getHeadTokenTag().startsWith("PRP") && cec.getHeadTokenTag().startsWith("PRP")) {
        features.add(mention.getHeadTokenText() + "," + cec.getHeadTokenText());
      }
      else if (mention.getHeadTokenText().startsWith("NNP")) {
        for (int ci = 0, cl = contexts.size(); ci < cl; ci++) {
          features.add(contexts.get(ci));
        }
        features.add(mention.getNameType() + "," + cec.getHeadTokenText());
      }
      else {
        List ccontexts = getContextFeatures(cec);
        for (int ci = 0, cl = ccontexts.size(); ci < cl; ci++) {
          features.add(ccontexts.get(ci));
        }
        features.add(cec.getNameType() + "," + mention.getHeadTokenText());
      }
    }
    return (features);
  }

  protected boolean outOfRange(MentionContext mention, DiscourseEntity entity) {
    MentionContext cec = entity.getLastExtent();
    return (mention.getSentenceNumber() - cec.getSentenceNumber() > numSentencesBack);
  }

  public boolean canResolve(MentionContext mention) {
    String tag = mention.getHeadTokenTag();
    boolean fpp = tag != null && tag.startsWith("PRP") && Linker.speechPronounPattern.matcher(mention.getHeadTokenText()).matches();
    boolean pn = tag != null && tag.startsWith("NNP");
    return (fpp || pn);
  }

  protected boolean excluded(MentionContext mention, DiscourseEntity entity) {
    if (super.excluded(mention, entity)) {
      return true;
    }
    MentionContext cec = entity.getLastExtent();
    if (!canResolve(cec)) {
      return true;
    }
    if (mention.getHeadTokenTag().startsWith("NNP")) { //mention is a propernoun
      if (cec.getHeadTokenTag().startsWith("NNP")) {
        return true; // both NNP
      }
      else {
        if (entity.getNumMentions() > 1) {
          return true;
        }
        return !canResolve(cec);
      }
    }
    else if (mention.getHeadTokenTag().startsWith("PRP")){ // mention is a speech pronoun
      // cec can be either a speech pronoun or a propernoun
      if (cec.getHeadTokenTag().startsWith("NNP")) {
        //exclude antecedents not in the same sentence when they are not pronoun 
        return (mention.getSentenceNumber() - cec.getSentenceNumber() != 0);
      }
      else if (cec.getHeadTokenTag().startsWith("PRP")){
        return false;
      }
      else {
        System.err.println("Unexpected candidate exluded: "+cec.toText());
        return true;
      }
    }
    else {
      System.err.println("Unexpected mention exluded: "+mention.toText());
      return true;
    }
  }

}
