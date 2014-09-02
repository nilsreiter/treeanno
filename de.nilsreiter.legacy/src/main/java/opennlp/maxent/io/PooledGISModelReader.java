///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2007 Thomas Morton
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////   
package opennlp.maxent.io;

import java.io.File;
import java.io.IOException;

/**
 * This class works exactly like the SuffisSensitiveGISModelReader except that it 
 * attempts to pool all context strings.  This is useful when loading models which
 * share many context strings.
 *
 */
public class PooledGISModelReader extends SuffixSensitiveGISModelReader {

  /**
   * A reader for GIS models which inspects the filename and invokes the
   * appropriate GISModelReader depending on the filename's suffixes.
   *
   * <p>The following assumption are made about suffixes:
   *    <li>.gz  --> the file is gzipped (must be the last suffix)
   *    <li>.txt --> the file is plain text
   *    <li>.bin --> the file is binary
   * @param f
   * @throws IOException
   */
  public PooledGISModelReader(File f) throws IOException {
    super(f);
  }

  protected String readUTF() throws IOException {
    return super.readUTF().intern();
  }
}
