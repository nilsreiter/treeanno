///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2001 Jason Baldridge
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
package opennlp.maxent;

import java.util.*;

/**
 * Generate contexts for maxent decisions, assuming that the input
 * given to the getContext() method is a String containing contextual
 * predicates separated by spaces. 
 * e.g:
 * <p>
 * cp_1 cp_2 ... cp_n
 * </p>
 * 
 * @author      Jason Baldridge
 * @version     $Revision: 1.4 $, $Date: 2007/04/13 16:12:28 $
 */
public class BasicContextGenerator implements ContextGenerator {

  /**
   * Builds up the list of contextual predicates given a String.
   */
  public String[] getContext(Object o) {
    String s = (String) o;
    return (String[]) s.split(" ");
  }
 
}

