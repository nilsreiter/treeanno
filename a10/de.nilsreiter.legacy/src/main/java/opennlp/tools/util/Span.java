///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2002 Tom Morton
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

package opennlp.tools.util;

/** 
 * Class for storing start and end integer offsets.  
**/
public class Span implements Comparable {

  private int start;
  private int end;

  /** 
   * Initializes a new Span Object.
   * 
   * @param s start of span.
   * @param e end of span.
   */
  public Span(int s,int e) {
    start=s;
    end=e;
  }
  
  /** 
   * Return the start of a span.
   * 
   * @return the start of a span.
   **/
  public int getStart() {
    return start;
  }
  
  /** 
   * Return the end of a span.
   * 
   * @return the end of a span.
   **/
  public int getEnd() {
    return end;
  }

  /** 
   * Returns the length of this span.
   * 
   * @return the length of the span.
   */
  public int length() {
    return end-start;
  }
  
  /**
   * Returns true if the specified span is contained by this span.  
   * Identical spans are considered to contain each other. 
   * 
   * @param s The span to compare with this span.
   * 
   * @return true is the specified span is contained by this span; 
   * false otherwise.
   */
  public boolean contains(Span s) {
    return start <= s.getStart() && s.getEnd() <= end;
  }
  
  public boolean contains(int index) {
    return start <= index && index <= end;
  }
  
  /**
   * Returns true if the specified span is the begin of this span and the
   * specified span is contained in this span.
   * 
   * @param s The span to compare with this span.
   * 
   * @return true if the specified span starts with this span and is
   * contained in this span; false otherwise
   */
  public boolean startsWith(Span s) {
    return getStart() == s.getStart() && contains(s);
  }
  
  /**
   * Returns true if the specified span intersects with this span.
   * 
   * @param s The span to compare with this span. 
   * 
   * @return true is the spans overlap; false otherwise. 
   */
  public boolean intersects(Span s) {
    int sstart = s.getStart();
    //either s's start is in this or this' start is in s
    return this.contains(s) || s.contains(this) || 
	   getStart() <= sstart && sstart < getEnd() ||
	   sstart <= getStart() && getStart() < s.getEnd();
  }
  
  /**
   * Returns true is the specified span crosses this span.
   * 
   * @param s The span to compare with this span.
   * 
   * @return true is the specified span overlaps this span and contains a 
   * non-overlapping section; false otherwise.
   */
  public boolean crosses(Span s) {
    int sstart = s.getStart();
    //either s's start is in this or this' start is in s
    return !this.contains(s) && !s.contains(this) && 
	   (getStart() <= sstart && sstart < getEnd() ||
	   sstart <= getStart() && getStart() < s.getEnd());
  }
  
  /**
   * Retrieves the string covered by the current span of the specified text.
   * 
   * @param text
   * 
   * @return the substring covered by the current span
   */
  public String getCoveredText(String text) {
    if (getEnd() > text.length()) {
      throw new IllegalArgumentException("The span " + toString() + 
          " is outside the given text!");
    }
    
    return text.substring(getStart(), getEnd());
  }
  
  /**
   * Compares the specified span to the current span.
   */
  public int compareTo(Object o) { 
    Span s = (Span) o;
    if (getStart() < s.getStart()) {
      return -1;
    }
    else if (getStart() == s.getStart()) {
      if (getEnd() > s.getEnd()) {
        return -1;
      }
      else if (getEnd() < s.getEnd()) {
        return 1;
      }
      else {
        return 0;
      }
    }
    else {
      return 1;
    }
  }

  /**
   * Generates a hash code of the current span.
   */
  public int hashCode() {
    return this.start << 16 | 0x0000FFFF | this.end;
  }
  
  /**
   * Checks if the specified span is equal to the current span.
   */
  public boolean equals(Object o) {

    boolean result;
    
    if (o == this) {
      result = true;
    }
    else if (o instanceof Span) {
      Span s = (Span) o;
      
      result = getStart() == s.getStart() && getEnd() == s.getEnd();
    }
    else {
      result = false;
    }
    
    return result;
  }
  
  /**
   * Generates a human readable string.
   */
  public String toString() {
    StringBuffer toStringBuffer = new StringBuffer(15);
    toStringBuffer.append(getStart());
    toStringBuffer.append("..");
    toStringBuffer.append(getEnd());
    
    return toStringBuffer.toString();
  }
  
  /**
   * Converts an array of {@link Span}s to an array of {@link String}s. 
   * 
   * @param spans
   * @param s
   * @return the strings
   */
  public static String[] spansToStrings(Span[] spans, String s) {
    String[] tokens = new String[spans.length];
    
    for (int si = 0, sl = spans.length; si < sl; si++) {
      tokens[si] = spans[si].getCoveredText(s);
    }
    
    return tokens;
  }
  
  public static String[] spansToStrings(Span[] spans, String[] tokens) {
    String[] chunks = new String[spans.length];
    StringBuffer cb = new StringBuffer();
    for (int si = 0, sl = spans.length; si < sl; si++) {
      cb.setLength(0);
      for (int ti=spans[si].getStart();ti<spans[si].getEnd();ti++) {
        cb.append(tokens[ti]).append(" ");
      }
      chunks[si]=cb.substring(0, cb.length()-1);
    }
    return chunks;
  }
}