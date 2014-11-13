/*
 * ScoringMatrix.java
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

import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.IOException;

/**
 * This class implements a scoring scheme based on a substitution matrix. It is useful
 * to represent PAM and BLOSUM family of amino acids scoring matrices. Its constructor
 * loads such matrices from a file (or any other character stream). The following is an
 * extract of a BLOSUM62 scoring matrix file:
 * <CODE><BLOCKQUOTE><PRE>
 *       A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X  *
 *    A  4 -1 -2 -2  0 -1 -1  0 -2 -1 -1 -1 -1 -2 -1  1  0 -3 -2  0 -2 -1  0 -4
 *    R -1  5  0 -2 -3  1  0 -2  0 -3 -2  2 -1 -3 -2 -1 -1 -3 -2 -3 -1  0 -1 -4
 *    ...
 *    B -2 -1  3  4 -3  0  1 -1  0 -3 -4  0 -3 -3 -2  0 -1 -4 -3 -3  4  1 -1 -4
 *    Z -1  0  0  1 -3  3  4 -2  0 -3 -3  1 -1 -3 -1  0 -1 -3 -2 -2  1  4 -1 -4
 *    X  0 -1 -1 -1 -2 -1 -1 -1 -1 -1 -1 -1 -1 -1 -2  0  0 -2 -1 -1 -1 -1 -1 -4
 *    * -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4 -4  1
 * </PRE></BLOCKQUOTE></CODE>
 *
 * <P>Matrices are expected to follow this format. They must have one row an one column
 * for each defined character (not necessarily in the same order). Each row and column
 * must start with a distinct character (no repetition) and all row characters must have a
 * correspondent column, and vice versa.</P>
 *
 * <P>Value at position (i,j) represent the score of substituting character of row i for
 * character of column j. Insertion penalties are specified by the last row while deletion
 * penalties must be located at the last column (both represented by the special character
 * defined by the <CODE>INDEL_CHAR</CODE> constant). Note that it only supports an
 * additive gap cost function. In case any of this rules are not followed, an
 * {@linkplain InvalidScoringMatrixException} exception is raised by the constructor.</P>
 *
 * <P>If a scoring operation (substitution, insertion or deletion) involves a character
 * not found in the matrix, an exception is raised.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 * @see InvalidScoringMatrixException
 */
public class ScoringMatrix extends ScoringScheme
{
	/**
	 * The character that indicates the row and column for insertion and deletion
	 * penalties in the matrix.
	 */
	protected static final char INDEL_CHAR = '*';

	/**
	 * The character used to start a comment line in the scoring matrix file.
	 */
	protected static final char COMMENT_CHAR = '#';

	/**
	 * Stores matrix column headers in the order they were found.
	 */
	protected String col_codes;

	/**
	 * Stores matrix row headers in the order they were found.
	 */
	protected String row_codes;

	/**
	 * Stores values for each operation (substitution, insertion or deletion) defined by
	 * this matrix.
	 */
	protected int matrix[][];

	/**
	 * Dimension of the (squared) matrix.
	 */
	protected int dimension;

	/**
	 * The maximum absolute score that this matrix can return for any substitution,
	 * deletion or insertion.
	 */
	protected int max_absolute_score;

	/**
	 * Creates a new instance of a substitution matrix loaded from the character stream.
	 * The case of characters is significant when subsequently computing their score.
	 *
	 * @param input character stream from where the matrix is read
	 * @throws IOException if an I/O operation fails when reading from input
	 * @throws InvalidScoringMatrixException if the matrix does not comply with the
	 * specification
	 */
	public ScoringMatrix (Reader input)
		throws IOException, InvalidScoringMatrixException
	{
		this (input, true);
	}

	/**
	 * Creates a new instance of a substitution matrix loaded from the character stream.
	 * If <CODE>case_sensitive</CODE> is <CODE>true</CODE>, the case of characters is
	 * significant when subsequently computing their score; otherwise the case is
	 * ignored.
	 *
	 * @param input character stream from where the matrix is read
	 * @param case_sensitive <CODE>true</CODE> if the case of characters must be
	 * @throws IOException if an I/O operation fails when reading from input
	 * @throws InvalidScoringMatrixException if the matrix does not comply with the
	 * specification
	 */
	public ScoringMatrix (Reader input, boolean case_sensitive)
		throws IOException, InvalidScoringMatrixException
	{
		super (case_sensitive);

		StreamTokenizer	in;
		StringBuffer	buf = new StringBuffer();
		int 			row, col, max_abs = 0;
		char			c;

		// create a stream tokenizer on top of the input
		// stream and set the COMMENT_CHAR as the comment character
		in = new StreamTokenizer(input);
		in.commentChar(COMMENT_CHAR);

		// consider ends of line when reading the first row
		in.eolIsSignificant(true);

		// skip blank lines (if any)
		for (in.nextToken(); in.ttype == StreamTokenizer.TT_EOL; in.nextToken());

		// read first row: column character codes
		while ((in.ttype != StreamTokenizer.TT_EOF) &&
				(in.ttype != StreamTokenizer.TT_EOL))
		{
			if (in.ttype == StreamTokenizer.TT_WORD)
			{
				if (in.sval.length() > 1)
					throw new InvalidScoringMatrixException
						("Column headers must have one-character only.");

					buf.append(in.sval.charAt(0));
			}
			else if (in.ttype == INDEL_CHAR)
			{
				buf.append(INDEL_CHAR);
			}
			else
			{
				throw new InvalidScoringMatrixException("Column headers must be " +
					"one-character codes or the special character '" + INDEL_CHAR + "'.");
			}

			in.nextToken();
		}

		// convert everything to upper case if it's not case sensitive
		if (case_sensitive)
			col_codes = buf.toString();
		else
			col_codes = buf.toString().toUpperCase();

		dimension = col_codes.length();

		// check if there's a column for deletion penalties
		if (col_codes.indexOf (INDEL_CHAR) == -1)
			throw new InvalidScoringMatrixException
				("Matrix have no column for deletion penalties.");

		// check if there is at least one character code (besides the INDEL char)
		if (dimension < 2)
			throw new InvalidScoringMatrixException
				("Matrix must have at least one column with a character code.");

		// check for repeated column codes
		for (int i = 0; i < dimension; i++)
			if (col_codes.indexOf(col_codes.charAt(i),i+1) > i)
				throw new InvalidScoringMatrixException
					("Columns must have distinct one-character codes.");

		// allocate matrix
		matrix = new int[dimension][dimension];

		// reset buffer
		buf.delete (0, dimension);

		// from now on, ignore ends of line
		in.eolIsSignificant(false);
		if (in.ttype == StreamTokenizer.TT_EOL) in.nextToken();

		// read rest of matrix (one line for each character, but
		// not necessarily in the same order as the columns)
		for (row = 0; row < dimension && in.ttype != StreamTokenizer.TT_EOF; row++)
		{
			// start reading the line: the character code must come first
			if (in.ttype == StreamTokenizer.TT_WORD)
			{
				if (in.sval.length() > 1)
					throw new InvalidScoringMatrixException
						("Codes must have one character only.");

				buf.append(in.sval.charAt(0));
			}
			else if (in.ttype == INDEL_CHAR)
			{
				buf.append(INDEL_CHAR);
			}
			else
			{
				throw new InvalidScoringMatrixException ("Rows must start with an" +
					" one-character code or the special character '" + INDEL_CHAR + "'.");
			}

			// now, the set of values
			for (col = 0; col < dimension; col++)
			{
				// start reading the values
				if (in.nextToken() != StreamTokenizer.TT_NUMBER)
					throw new InvalidScoringMatrixException
						("Invalid value at row " + (row+1) + ", column " + (col+1) + ".");

				matrix[row][col] = (int) in.nval;

				if (Math.abs(matrix[row][col]) > max_abs)
					max_abs = Math.abs(matrix[row][col]);
			}

			in.nextToken();
		}

		// convert everything to upper case if it's not case sensitive
		if (case_sensitive)
			row_codes = buf.toString();
		else
			row_codes = buf.toString().toUpperCase();

		// check if read as many rows as columns
		if (row_codes.length() != dimension)
			throw new InvalidScoringMatrixException
				("Matrix must have as many rows as columns.");

		// check if there's a row for insertion penalties
		if (row_codes.indexOf(INDEL_CHAR) == -1)
			throw new InvalidScoringMatrixException
				("Matrix have no row for insertion penalties.");

		// check for repeated row codes
		for (int i = 0; i < dimension; i++)
			if (row_codes.indexOf(row_codes.charAt(i),i+1) > i)
				throw new InvalidScoringMatrixException
					("Rows must have distinct one-character codes.");

		// check if all rows have a corresponding column
		for (int i = 0; i < dimension; i++)
			if (col_codes.indexOf(c = row_codes.charAt(i)) == -1)
				throw new InvalidScoringMatrixException
					("There is no corresponding column for row character '" + c + "'.");

		// store the maximum absolute value found
		this.max_absolute_score = max_abs;
	}

	/**
	 * Returns the score of a substitution of character <CODE>a</CODE> for character
	 * <CODE>b</CODE> according to this scoring matrix.
	 *
	 * @param a first character
	 * @param b second character
	 * @return score of a substitution of character <CODE>a</CODE> for <CODE>b</CODE>
	 * @throws IncompatibleScoringSchemeException if this substitution is not defined
	 */
	public int scoreSubstitution (char a, char b)
		throws IncompatibleScoringSchemeException
	{
		int r,c;

		if (case_sensitive)
		{
			r = row_codes.indexOf(a);
			c = col_codes.indexOf(b);
		}
		else
		{
			r = row_codes.indexOf(Character.toUpperCase(a));
			c = col_codes.indexOf(Character.toUpperCase(b));
		}

		if (r < 0 || c < 0)
			throw new IncompatibleScoringSchemeException ("Substitution of character " +
													a + " for " + b + " is not defined.");

		return matrix[r][c];
	}

	/**
	 * Returns the score of an insertion of character <CODE>a</CODE> according to this
	 * scoring matrix.
	 *
	 * @param a character to be inserted
	 * @return score of insertion of <CODE>a</CODE>
	 * @throws IncompatibleScoringSchemeException if this character is not recognised
	 */
	public int scoreInsertion (char a) throws IncompatibleScoringSchemeException
	{
		return scoreSubstitution (INDEL_CHAR, a);
	}

	/**
	 * Returns the score of a deletion of character <CODE>a</CODE> according to this
	 * scoring matrix.
	 *
	 * @param a character to be deleted
	 * @return score of deletion of <CODE>a</CODE>
	 * @throws IncompatibleScoringSchemeException if this character is not recognised
	 */
	public int scoreDeletion (char a) throws IncompatibleScoringSchemeException
	{
		return scoreSubstitution (a, INDEL_CHAR);
	}

	/**
	 * Tells whether this scoring scheme supports partial matches, which it does, although
	 * a particular scoring matrix loaded by this instace might not. A partial match is
	 * a situation when two characters are not equal but, for any reason, are regarded
	 * as similar by this scoring scheme, which then returns a positive score value. This
	 * is common for amino acid scoring matrices.
	 *
	 * @return always return <CODE>true</CODE>
	 */
	public boolean isPartialMatchSupported ()
	{
		return true;
	}

	/**
	 * Returns the maximum absolute score that this scoring scheme can return for any
	 * substitution, deletion or insertion.
	 *
	 * @return maximum absolute score that can be returned
	 */
	public int maxAbsoluteScore ()
	{
		return max_absolute_score;
	}

	/**
	 * Returns a String representation of this scoring matrix.
	 *
	 * @return a String representation of this scoring matrix
	 */
	public String toString ()
	{
		int row, col;

		StringBuffer buf = new StringBuffer();

		// column numbers
		buf.append("Scoring matrix:\n\t");
		for (col = 0; col < dimension; col++)
		{
			buf.append("\t" + col);
		}
		buf.append("\n\t");

		// column headers
		for (col = 0; col < dimension; col++)
		{
			buf.append('\t');
			buf.append(col_codes.charAt(col));
		}

		// rest of matrix
		for (row = 0; row < dimension; row++)
		{
			// row number and code
			buf.append("\n" + row + "\t" + row_codes.charAt(row));

			for (col = 0; col < dimension; col++)
			{
				buf.append('\t');
				buf.append(matrix[row][col]);
			}
		}

		return buf.toString();
	}
}
