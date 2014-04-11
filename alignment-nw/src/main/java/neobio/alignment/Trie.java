/*
 * Trie.java
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
 * This class implements a trie, or a digital search tree. A trie is a multiway tree
 * (each node can have multiple children) that represents a set of strings.
 *
 * <P>Each node contains data encapsulated in an object instance. Each edge spells out a
 * character and each path from the root represents a string described by the characters
 * labelling the traversed edges. Moreover, for each string represented, there is a unique
 * path from the root.</P>
 *
 * <P>The trie of the following example represents the strings 'a', 'd', 'b', 'ac', 'ba',
 * 'be', 'bd', 'bad' and 'bae'.</P>
 *
 * <CODE><BLOCKQUOTE><PRE>
 *      [0]
 *     --+--
 *    /  |  \
 *  a/  d|   \b
 * [1]  [2]  [4]
 *  |       --+--
 *  |      /  |  \
 * c|    a/  e|  d\
 * [3]  [5]  [6]  [9]
 *     --+--
 *    /     \
 *  d/      e\
 * [7]       [8]
 * </PRE></BLOCKQUOTE></CODE>
 *
 * <P>It is easy to see that strings with common prefixes will branch off from each other
 * at the first distinguishing character. This feature makes the trie a good data
 * structure to identify and represent phrases of a text such as the ones induced by the
 * Lempel-Ziv familiy of compression algorithms. For instance, the LZ78 version parses
 * the text into phrases, where each phrase is the longest matching phrase seen previously
 * plus one character.</P>
 *
 * <P>In this implementation, each node is actually an instance of this class. To build a
 * trie, one must first create the root using the public constructor:</P>
 *
 * <CODE><BLOCKQUOTE><PRE>
 * Trie root = new Trie (some_object);
 * </PRE></BLOCKQUOTE></CODE>
 *
 * <P>Here <CODE>some_object</CODE> contains any relevant information encapsulated in an
 * object instance. Typically, that's the only moment the public constructor is used. From
 * now on, all new nodes will be added as a new child of one existing node using the
 * <CODE>add</CODE> method:</P>
 *
 * <CODE><BLOCKQUOTE><PRE>
 * new_node = any_node.add (some_object, character);
 * </PRE></BLOCKQUOTE></CODE>
 *
 * <P>Here <CODE>character</CODE> is the character that will label the edge from
 * <CODE>any_node</CODE> to <CODE>new_node</CODE>. Note that this transition must not
 * already exist, otherwise an exception is raised.
 *
 * <P>To find the longest prefix of a given string, we follow a path from the root down
 * the tree, character by character, with the <CODE>spellDown</CODE> method:</P>
 *
 * <CODE><BLOCKQUOTE><PRE>
 * next_node = root;
 * while (next_node != null)
 * {
 *     current_node = next_node;
 *     char c = get next character from somewhere
 *     <B>next_node = current_node.spellDown (c);</B>
 * }
 * </PRE></BLOCKQUOTE></CODE>
 *
 * <P><CODE>spellDown</CODE> follows the edge out of <CODE>current_node</CODE> labelled by
 * the character <CODE>c</CODE> and returns the next node. If there is no such a path, it
 * returns null.</P>
 *
 * <P>To retrieve the information stored at any node, simply use the <CODE>getData</CODE>
 * method.</P>
 *
 * <P>In fact, there are many ways to implement a trie. To avoid wasting space with
 * multiple pointers at each node, this implementation uses an approach with a linked list
 * of siblings. Each node actually contains a pointer to one of its children and a pointer
 * to one of its siblings only. Together with the pointers, each node also stores the
 * character that labels the edge to the pointed node.<P>
 *
 * <CODE><BLOCKQUOTE><PRE>
 * [0]
 *  |
 * a|  d     b
 * [1]---[2]---[4]
 *  |           |
 * c|          a|  e     d
 * [3]         [5]---[6]---[9]
 *              |
 *             d|  e
 *             [7]---[8]
 * </PRE></BLOCKQUOTE></CODE>
 *
 * <P>In this way, a trie is similar to a binary tree. Although this implementation is
 * more efficient in terms of space, the search for a label with a given character leaving
 * a node <CODE>n</CODE> is no more constant but proportional to the number of children of
 * <CODE>n</CODE>. In the previous example, it is necessary to traverse three edges to
 * reach node 9 from node 4 with character d.</P>
 *
 * <P>This class is used by the {@linkplain FactorSequence} to build a linked list of
 * factors of a sequence in a LZ78 fashion, i.e. where each factor is the longest factor
 * previously seen plus one character.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 * @see FactorSequence
 */
public class Trie
{
	/**
	 * A pointer to the first of this node's children.
	 */
	protected Trie son;

	/**
	 * The character that labels the edge from this node to the child node pointer by
	 * <CODE>son</CODE>.
	 */
	protected char to_son;

	/**
	 * A pointer to this node's next sibling.
	 */
	protected Trie sibling;

	/**
	 * The character that labels the edge from this node to the sibling pointer by
	 * <CODE>sibling</CODE>.
	 */
	protected char to_sibling;

	/**
	 * This node's stored data.
	 */
	protected Object	data;

	/**
	 * Creates a new trie node with the specified data. This constructor is typically used
	 * by the client only once to instantiate the root node. After that, all new nodes are
	 * implicitly instantiated by the <CODE>add</CODE> method.
	 *
	 * @param data the data that will be associated with the new node
	 */
	public Trie (Object data)
	{
		this.son = null;
		this.sibling = null;
		this.data = data;
	}

	/**
	 * Returns the data associated with this node.
	 *
	 * @return data associated with this node
	 */
	public Object getData ()
	{
		return data;
	}

	/**
	 * Adds a new child to this node. The new node will be implicitly instantiated with
	 * the <CODE>data</CODE> argument, and the edge from this node to the new node will be
	 * labelled by the character argument. If this node already have an edge labelled with
	 * this character, an exception is raised. Otherwise, the new node created and
	 * returned.
	 *
	 * <P>If this node have no child, a new node is created straight away. Otherwise, the
	 * task is assigned to its first child that will add the new node as a sibling.</P>
	 *
	 * @param data the data that will be associated with the new node
	 * @param c the character that will label the edge from this node to the new node
	 * @return the added node
	 * @throws IllegalStateException if this node already have an edge labelled by
	 * <CODE>c</CODE>
	 */
	public Trie add (Object data, char c)
	{
		if (son == null)
		{
			son = new Trie (data);
			to_son = c;
			return son;
		}
		else
		{
			if (to_son != c)
				return son.addSibling (data, c);
			else
				// duplicate char
				throw new IllegalStateException ("Failed to add character " + c +
																" already exists.");
		}
	}

	/**
	 * Adds a sibling to this node. The new node will be implicitly instantiated with
	 * the <CODE>data</CODE> argument, and the edge from this node to the new node will be
	 * labelled by the character argument. If this node already have a sibling with this
	 * character, an exception is raised. Otherwise, the new node is created and returned.
	 *
	 * <P>If this node have no direct sibling, a new node is created straight away.
	 * Otherwise, the task is assigned to its next sibling.</P>
	 *
	 * @param data the data that will be associated with the new node
	 * @param c the character that will label the edge from this node to the new node
	 * @return the added node
	 * @throws IllegalStateException if this node already have an edge labelled by
	 * <CODE>c</CODE>
	 */
	protected Trie addSibling (Object data, char c)
	{
		if (sibling == null)
		{
			sibling = new Trie (data);
			to_sibling = c;
			return sibling;
		}
		else
		{
			if (to_sibling != c)
				return sibling.addSibling (data, c);
			else
				// duplicate char
				throw new IllegalStateException ("Failed to add character: " + c +
																" already exists.");
		}
	}

	/**
	 * Follows a path from this node to one of its children by spelling the character
	 * supplied as an argument. If there is no such a path, <CODE>null</CODE> is returned.
	 * Otherwise, the reached child node is returned.
	 *
	 * <P>If this node's direct child is reached with an edge labelled by the character,
	 * it is returned straight away. Otherwise, it is assigned the task of finding another
	 * sibling labelled with that character.</P>
	 *
	 * @param c the character that labels the path to be followed to this node's child
	 * @return the child node reached by traversing the edge labelled by <CODE>c</CODE>
	 */
	public Trie spellDown (char c)
	{
		if (son == null) return null;

		if (to_son == c)
			return son;
		else
			return son.spellRight(c);
	}

	/**
	 * Follows a path from this node to one of its sibling by spelling the character
	 * supplied as an argument. If there is no such a path, <CODE>null</CODE> is returned.
	 * Otherwise, the reached sibling node is returned.
	 *
	 * <P>If this node's direct sibling is reached with an edge labelled by the character,
	 * it is returned straight away. Otherwise, it is assigned the task of finding another
	 * sibling labelled with that character.</P>
	 *
	 * @param c the character that labels the path to be followed to the sibling
	 * @return the sibling node reached by traversing the edge labelled by <CODE>c</CODE>
	 */
	protected Trie spellRight (char c)
	{
		if (sibling == null) return null;

		if (to_sibling == c)
			return sibling;
		else
			return sibling.spellRight(c);
	}
}
