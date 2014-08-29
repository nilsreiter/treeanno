///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2008 OpenNlp
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

package opennlp.tools.doccat;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Generates a feature for each word in a document.
 */
public class BagOfWordsFeatureGenerator implements FeatureGenerator {

  public Collection extractFeatures(String[] text) {
    
    Collection bagOfWords = new ArrayList(text.length);
    
    for (int i = 0; i < text.length; i++) {
      bagOfWords.add("bow=" + text[i]);
    }
    
    return bagOfWords;
  }
}