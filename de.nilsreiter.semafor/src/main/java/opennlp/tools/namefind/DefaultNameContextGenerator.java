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
package opennlp.tools.namefind;

import java.util.ArrayList;
import java.util.List;

/** 
 * Class for determining contextual features for a tag/chunk style 
 * named-entity recognizer.
 * 
 * @version $Revision$, $Date$
 */
public class DefaultNameContextGenerator implements NameContextGenerator {
  
  private AdaptiveFeatureGenerator featureGenerators[];

  /**
   * Creates a name context generator.
   */
  public DefaultNameContextGenerator() {
    this(null);
  }
  
  /**
   * Creates a name context generator with the specified cache size.
   */
  public DefaultNameContextGenerator(AdaptiveFeatureGenerator featureGenerators[]) {
    
    if (featureGenerators != null) {
      this.featureGenerators = featureGenerators;
    }
    else {
      // use defaults
      
      this.featureGenerators = new AdaptiveFeatureGenerator[]{
          StaticFeatureGenerator.instance().staticFeatures, 
          new PreviousMapFeatureGenerator()};
    }    
  }
  
  public void addFeatureGenerator(AdaptiveFeatureGenerator generator) {
      AdaptiveFeatureGenerator generators[] = featureGenerators;
      
      featureGenerators = new AdaptiveFeatureGenerator[featureGenerators.length + 1];
      
      System.arraycopy(generators, 0, featureGenerators, 0, generators.length);
      
      featureGenerators[featureGenerators.length - 1] = generator;
  }
  
  public void updateAdaptiveData(String[] tokens, String[] outcomes) {
    
    if (tokens != null && outcomes != null && tokens.length != outcomes.length) {
        throw new IllegalArgumentException(
            "The tokens and outcome arrays MUST have the same size!");
      }
    
    for (int i = 0; i < featureGenerators.length; i++) {
      featureGenerators[i].updateAdaptiveData(tokens, outcomes);
    }    
  }
  
  public void clearAdaptiveData() {
    for (int i = 0; i < featureGenerators.length; i++) {
      featureGenerators[i].clearAdaptiveData();
    }
  }
  
  public String[] getContext(int index, Object[] sequence, String[] priorDecisions, Object[] additionalContext) {
    return getContext(index,sequence,priorDecisions,(String[][]) additionalContext);
  }

  /**
   * Return the context for finding names at the specified index.
   * @param index The index of the token in the specified toks array for which the context should be constructed. 
   * @param toks The tokens of the sentence.  The <code>toString</code> methods of these objects should return the token text.
   * @param preds The previous decisions made in the tagging of this sequence.  Only indices less than i will be examined.
   * @param additionalContext Addition features which may be based on a context outside of the sentence. 
   * @return the context for finding names at the specified index.
   */
  public String[] getContext(int index, Object[] toks, String[] preds, String[][] additionalContext) {
    String[] tokens;
    //previous outcome features
    String po=NameFinderME.OTHER;
    String ppo=NameFinderME.OTHER;
    CachingNameFeatures cnf = StaticFeatureGenerator.instance();
    if (index > 1){
      ppo = preds[index-2];
    }

    if (index > 0) {
      po = preds[index-1];
    }
    //callNum++;  if (callNum % 100000 == 0) { cacheReport(); }
    if (cnf.prevTokens == toks) {
      tokens = cnf.prevStrings;
      if (index == cnf.cpi && po.equals(cnf.cpo) && ppo.equals(cnf.cppo)) {
        //pc++;
        //System.err.println(index+" h "+java.util.Arrays.asList(prevContext));
        return cnf.prevContext;
      }
    }
    else {
      tokens = new String[toks.length];  
      for (int i = 0; i < toks.length; i++) {
        tokens[i] = toks[i].toString();
      }
      cnf.prevTokens = toks;
      cnf.prevStrings = tokens;
    }
    List features = new ArrayList();
    features.add("def");
    if (index == 0) {
      features.add("fwis"); //first word in sentence
    }
    for (int i = 0; i < featureGenerators.length; i++) {
      featureGenerators[i].createFeatures(features, tokens, index, preds);
    }    
    if (additionalContext != null && additionalContext.length != 0) {
      for (int aci = 0; aci < additionalContext[index].length; aci++) {
        features.add(additionalContext[index][aci]);
      }
    }
    
    String wc = FeatureGeneratorUtil.tokenFeature(tokens[index]);
    features.add("po=" + po);
    features.add("pow=" + po + "," + tokens[index]);
    features.add("powf=" + po + "," + wc);
    features.add("ppo=" + ppo);
    
    cnf.prevContext = (String[]) features.toArray(new String[features.size()]);
    cnf.cpi=index;
    cnf.cpo = po;
    cnf.cppo = ppo;
    //System.err.println(index+" m "+java.util.Arrays.asList(prevContext));
    return cnf.prevContext;
  }
  
  private int callNum = 0;
  private int pc = 0;

  private void cacheReport() {
    System.err.println(callNum+" "+StaticFeatureGenerator.instance().staticFeatures.toString()+" pc="+pc+" "+(double) pc/callNum);
  }
}

class BigramNameFeatureGenerator extends FeatureGeneratorAdapter {

  public void createFeatures(List features, String[] tokens, int index, String[] previousOutcomes) {
    String wc = FeatureGeneratorUtil.tokenFeature(tokens[index]);
    //bi-gram features 
    if (index > 0) {
      features.add("pw,w="+tokens[index-1]+","+tokens[index]);
      String pwc = FeatureGeneratorUtil.tokenFeature(tokens[index-1]);
      features.add("pwc,wc="+pwc+","+wc);
    }
    if (index+1 < tokens.length) {
      features.add("w,nw="+tokens[index]+","+tokens[index+1]);
      String nwc = FeatureGeneratorUtil.tokenFeature(tokens[index+1]); 
      features.add("wc,nc="+wc+","+nwc);
    }
  } 
}

class CachingNameFeatures {
  public String[] prevContext;
  public String cpo;
  public String cppo;
  public int cpi;
  
  public Object[] prevTokens;
  public String[] prevStrings;
  public AdaptiveFeatureGenerator staticFeatures = new CachedFeatureGenerator(
      new AdaptiveFeatureGenerator[]{
          new WindowFeatureGenerator(new TokenFeatureGenerator(), 2, 2), 
          new WindowFeatureGenerator(new TokenClassFeatureGenerator(true), 2, 2),
          new BigramNameFeatureGenerator()
          }); 
}

class StaticFeatureGenerator {
  
  private static class ThreadLocalGenerator extends ThreadLocal {
    public Object initialValue() {
      return new CachingNameFeatures();
    }
  }
  
  private static ThreadLocalGenerator tlg = new ThreadLocalGenerator();
  public static CachingNameFeatures instance() {
    return (CachingNameFeatures) tlg.get();
  }
}
