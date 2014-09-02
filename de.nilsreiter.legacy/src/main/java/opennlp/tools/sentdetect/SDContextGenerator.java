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
package opennlp.tools.sentdetect;


/**
 * Interface for {@link SentenceDetectorME} context generators.
 */
public interface SDContextGenerator {

  /**
   * Returns an array of contextual features for the potential sentence boundary at the
   * specified position within the specified string buffer. 
   * 
   * @param sb The string buffer for which sentences are being determined.
   * @param position An index into the specified string buffer when a sentence boundary may occur.
   * 
   * @return an array of contextual features for the potential sentence boundary at the
   * specified position within the specified string buffer.
   */
  public abstract String[] getContext(StringBuffer sb, int position);
}