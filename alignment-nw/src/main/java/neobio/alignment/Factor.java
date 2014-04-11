/*
 * Factor.java
 *
 * Copyright 2003 Sergio Anibal de Carvalho Junior
 *
 * This file is part of NeoBio.
 *
 * NeoBio is free software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * NeoBio is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with NeoBio;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * Proper attribution of the author as the source of the software would be appreciated.
 *
 * Sergio Anibal de Carvalho Junior		mailto:sergioanibaljr@users.sourceforge.net
 * Department of Computer Science		http://www.dcs.kcl.ac.uk
 * King's College London, UK			http://www.kcl.ac.uk
 *
 * Please visit http://neobio.sourceforge.net
 *
 * This project was supervised by Professor Maxime Crochemore.
 *
 */

package neobio.alignment;

/**
 * This class is used by {@linkplain FactorSequence} to create a linked list of factors of
 * a text as induced by its Lempel-Ziv (LZ78) factorisation.
 *
 * <P>Each instance of this class represent a string composed of its an ancestor factor's
 * string plus one character, and contains:
 *
 * <UL>
 * <LI>a pointer to its ancestor factor (the longest factor previously seen in the text
 * during its LZ78 factorisation);
 * <LI>the new character;
 * <LI>a serial number (which represents its order in the text)
 * <LI>a pointer to the next factor of the text
 * <LI>its length (number of characters, which is equal to its ancestor's length plus one)
 * </UL>
 *
 * @author Sergio A. de Carvalho Jr.
 * @see FactorSequence
 */
public class Factor
{
	/**
	 * A pointer to this factor's ancestor, which represents a prefix of this factor's
	 * text.
	 */
	protected Factor ancestor;

	/**
	 * A pointer to the next factor.
	 */
	protected Factor next;

	/**
	 * This factor's serial number, which indicates the order of this factor inside the
	 * linked list of factors of a text.
	 */
	protected int serial_number;

	/**
	 * The number of characters of the text represented by this factor.
	 */
	protected int length;

	/**
	 * The new character of this factor.
	 */
	protected char new_char;

	/**
	 * Creates a new empty <CODE>Factor</CODE>. It has no ancestor and no character (both
	 * are set to <CODE>null</CODE>). Its serial number is set to zero as well as its
	 * length.
	 *
	 * <P>This constructor is used to initiate the a linked list of factors of a text. Its
	 * <CODE>next</CODE> pointer is initially <CODE>null</CODE>, but it is typically set
	 * to point to the first factor afterwards (with the <CODE>setNext</CODE> method).
	 *
	 * @see #setNext
	 */
	public Factor ()
	{
		this.ancestor = null;
		this.next = null;
		this.serial_number = 0;
		this.length = 0;
		this.new_char = 0;
	}

	/**
	 * Creates a new <CODE>Factor</CODE> instance with the specified serial number and
	 * new character, and pointing to the given ancestor. Its length is set to its
	 * ancestor's length plus 1.
	 *
	 * <P>Its <CODE>next</CODE> pointer is initially <CODE>null</CODE>, but it is
	 * typically set to point to the next factor afterwards (with the <CODE>setNext</CODE>
	 * method).
	 *
	 * @param ancestor this factor's ancestor
	 * @param serial_number this factor's serial number
	 * @param new_char this factor's new character
	 * @see #setNext
	 */
	public Factor (Factor ancestor, int serial_number, char new_char)
	{
		this.ancestor = ancestor;
		this.serial_number = serial_number;
		this.new_char = new_char;
		if (ancestor != null)
			this.length = ancestor.length() + 1;
		else
			throw new IllegalArgumentException ("Ancestor factor cannot be null.");
	}

	/**
	 * Sets this factor's <CODE>next</CODE> pointer to point to the specified factor.
	 * Although the next factor has typically a serial number equal to this factor's
	 * serial number plus 1, no attempt is made to guarantee this rule. This allows
	 * special constructs or a different order in the factorisation.
	 *
	 * @param next the factor that will be pointed to
	 * @see #getNext
	 */
	public void setNext (Factor next)
	{
		this.next = next;
	}

	/**
	 * Returns this factor's ancestor factor.
	 *
	 * @return this factor's ancestor factor
	 */
	public Factor getAncestor ()
	{
		return ancestor;
	}

	/**
	 * This method is a shorthand to return the serial number of this factor's ancestor.
	 * Note that it does not check if this factor has an ancestor or not, therefore, if
	 * it is called on the root factor, a NullPointerException is raised.
	 *
	 * @return the serial number of this factor's ancestor
	 */
	public int getAncestorSerialNumber ()
	{
		return ancestor.getSerialNumber();
	}

	/**
	 * Returns this factor's next factor.
	 *
	 * @return this factor's next factor
	 * @see #setNext
	 */
	public Factor getNext ()
	{
		return next;
	}

	/**
	 * Returns this factor's serial number.
	 *
	 * @return this factor's serial number
	 */
	public int getSerialNumber ()
	{
		return serial_number;
	}

	/**
	 * Returns this factor's length.
	 *
	 * @return this factor's length
	 */
	public int length ()
	{
		return length;
	}

	/**
	 * Returns this factor's new character.
	 *
	 * @return this factor's new character
	 */
	public char getNewChar ()
	{
		return new_char;
	}

	/**
	 * Returns a string representation of the text represented by this factor. It inspects
	 * its chain of ancestors up until as far as the root factor, spelling their new
	 * characters out.
	 *
	 * @return a string representation of the text denoted by this factor
	 */
	public String toString ()
	{
		StringBuffer buf = new StringBuffer();
		Factor ancestor = this;

		while (ancestor.getAncestor() != null)
		{
			buf.insert(0, ancestor.getNewChar());
			ancestor = ancestor.getAncestor();
		}

		return buf.toString();
	}
}
