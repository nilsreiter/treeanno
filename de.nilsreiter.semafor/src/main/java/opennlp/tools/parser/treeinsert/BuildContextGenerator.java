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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import opennlp.tools.parser.AbstractContextGenerator;
import opennlp.tools.parser.Cons;
import opennlp.tools.parser.Parse;

/**
 * Creates the features or contexts for the building phase of parsing. 
 * This phase builds constituents from the left-most node of these 
 * constituents.
 */
public class BuildContextGenerator extends AbstractContextGenerator {

  private Parse[] leftNodes;
  
  public BuildContextGenerator() {
    super();
    leftNodes = new Parse[2];
  }

  public String[] getContext(Object o) {
    Object[] parts = (Object[]) o;
    return getContext((Parse[]) parts[0],((Integer)parts[1]).intValue());
  }
    
  /**
   * Returns the contexts/features for the decision to build a new constituent for the specified parse 
   * at the specified index.
   * @param constituents The constituents of the parse so far.
   * @param index The index of the constituent where a build decision is being made.
   * @return the contexts/features for the decision to build a new constituent.
   */
  public String[] getContext(Parse[] constituents, int index) {
    Parse p_2 = null;
    Parse p_1 = null;
    Parse p0 = null;
    Parse p1 = null;
    Parse p2 = null;
    int ps = constituents.length;
    
    p0 = constituents[index];
    if (index + 1 < ps) {
      p1 = constituents[index + 1];
    }
    if (index +2 < ps) {
      p2 = constituents[index + 2];
    }
    
    Collection punct1s = null;
    Collection punct_1s = null;
    Collection punct2s = null;
    Collection punct_2s = null;
    
    punct_1s=p0.getPreviousPunctuationSet();
    punct1s=p0.getNextPunctuationSet();
    if (p1 != null) {
      punct2s=p1.getNextPunctuationSet();
    }
    if (p_2 != null) {
      punct_2s = p_1.getPreviousPunctuationSet();
    }
    
    List rf;
    if (index == 0) {
      rf = Collections.EMPTY_LIST;
    }
    else {
      //this isn't a root node so, punctSet won't be used and can be passed as empty.
      rf = Parser.getRightFrontier(constituents[0],Collections.EMPTY_SET);
    }
    getFrontierNodes(rf,leftNodes);
    p_1 = leftNodes[0];
    p_2 = leftNodes[1];
    
    String consp_2 = cons(p_2, -2);
    String consp_1 = cons(p_1, -1);
    String consp0 = cons(p0, 0);
    String consp1 = cons(p1, 1);
    String consp2 = cons(p2, 2);

    String consbop_2 = consbo(p_2, -2);
    String consbop_1 = consbo(p_1, -1);
    String consbop0 = consbo(p0, 0);
    String consbop1 = consbo(p1, 1);
    String consbop2 = consbo(p2, 2);
    
    Cons c_2 = new Cons(consp_2,consbop_2,-2,true);
    Cons c_1 = new Cons(consp_1,consbop_1,-1,true);
    Cons c0 = new Cons(consp0,consbop0,0,true);
    Cons c1 = new Cons(consp1,consbop1,1,true);
    Cons c2 = new Cons(consp2,consbop2,2,true);    

    List features = new ArrayList();
    features.add("default");
    
    //unigrams
    features.add(consp_2);
    features.add(consbop_2);
    features.add(consp_1);
    features.add(consbop_1);
    features.add(consp0);
    features.add(consbop0);
    features.add(consp1);
    features.add(consbop1);
    features.add(consp2);
    features.add(consbop2);

    //cons(0),cons(1)
    cons2(features,c0,c1,punct1s,true);
    //cons(-1),cons(0)
    cons2(features,c_1,c0,punct_1s,true);
    //features.add("stage=cons(0),cons(1),cons(2)");
    cons3(features,c0,c1,c2,punct1s,punct2s,true,true,true);
    cons3(features,c_2,c_1,c0,punct_2s,punct_1s,true,true,true);
    cons3(features,c_1,c0,c1,punct_1s,punct_1s,true,true,true);
    
    if (rf.isEmpty()) {
      features.add(EOS+","+consp0);
      features.add(EOS+","+consbop0);
    }
    
    return (String[]) features.toArray(new String[features.size()]);
  }

}
