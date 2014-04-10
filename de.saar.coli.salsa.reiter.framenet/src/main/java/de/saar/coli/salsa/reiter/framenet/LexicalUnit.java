/**
 * 
 * Copyright 2007-2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */
package de.saar.coli.salsa.reiter.framenet;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.reiter.pos.FN;

/**
 * Represents a lexical unit of FrameNet.
 * 
 * @author Nils Reiter
 * 
 */
public abstract class LexicalUnit implements Serializable, IHasCreationDate,
	IHasName, IHasDefinition, IHasID {
    /**
     * The XML id of the lexical unit.
     */
    Integer id;

    /**
     * The XML field "name".
     */
    String name;

    /**
     * The XML field "status".
     */
    String status;

    /**
     * The XML field "cDate".
     */
    Date creationDate;

    /**
     * The XML field "definition".
     */
    byte[] definition;

    /**
     * The XML field "lemmaID".
     */
    String lemmaID;

    /**
     * @deprecated The lemma of the lexical unit.
     */
    @Deprecated
    String lemma;

    /**
     * The part of speech.
     * 
     */
    FN partOfSpeech;

    /**
     * The frame to which this lexical unit belongs.
     */
    Frame frame;

    /**
	 * 
	 */
    List<Lexeme> lexemes = null;

    private static final long serialVersionUID = 8L;

    protected LexicalUnit() {
	lexemes = new LinkedList<Lexeme>();
    }

    public FN getPartOfSpeech() {
	return this.partOfSpeech;
	/*
	 * The idea was to return the pos tag of the head lexeme of the unit,
	 * but apparently (e.g., lu14743) the lexical unit can have a different
	 * pos tag than the head lexeme. if (lexemes.size() == 1) { return
	 * lexemes.get(0).getPartOfSpeech(); } for (Lexeme lexeme : lexemes) {
	 * if (lexeme.isHeadword()) { return lexeme.getPartOfSpeech(); } }
	 * return null;
	 */
    }

    /*
     * Returns the part of speech of this lemma using {@link PartOfSpeech} found
     * in the package edu.mit.jwi.item. The method returns null, if the part of
     * speech of the lexical unit can not be represented with {@link
     * edu.mit.jwi.item.PartOfSpeech}.
     * 
     * @return The part of speech, or null if it can not be represented.
     */
    /*
     * public PartOfSpeech getPartOfSpeech() { if (pos.equals("adv")) return
     * PartOfSpeech.ADVERB; if (pos.equals("v")) return PartOfSpeech.VERB; if
     * (pos.equals("n")) return PartOfSpeech.NOUN; if (pos.equals("a")) return
     * PartOfSpeech.ADJECTIVE; if (frameNet.isDebug())
     * System.err.println("LexicalUnit.getPartOfSpeech(): Part of Speech " + pos
     * + " convertable in object. Returning null."); return null; }
     */

    @Override
    @Deprecated
    public String getCDate() {
	return creationDate.toString();
    }

    @Override
    public String getIdString() {
	return String.valueOf(id);
    }

    public Integer getIntegerId() {
	return id;
    }

    @Override
    public String getName() {
	if (name == null && lexemes != null) {
	    StringBuffer buf = new StringBuffer();
	    for (Lexeme lexeme : lexemes) {
		buf.append(lexeme.getValue());
		buf.append(' ');
	    }
	    name =
		    buf.toString().trim() + "."
			    + this.getPartOfSpeech().toString();
	}
	return name;
    }

    @Override
    public String getDefinition() {
	return new String(definition);
    }

    /**
     * Returns the annotation status as string.
     * 
     * @return The status of the annotation.
     */
    public String getStatus() {
	return status;
    }

    /**
     * The lemma id is a ongoing integer value for the different lemmas.
     * 
     * @return the lemmaID
     */
    public String getLemmaID() {
	return lemmaID;
    }

    /**
     * The lemma is the word without its part of speech. The lemma of the
     * lexical unit "walk.v", for instance, is "walk".
     * 
     * This method is deprecated since 0.4 and 0.3.5.
     * 
     * @deprecated See
     *             http://www.cl.uni-heidelberg.de/trac/FrameNetAPI/ticket/31
     *             for details.
     * @return the lemma
     */
    @Deprecated
    public String getLemma() {
	return lemma;
    }

    @Override
    public String toString() {
	return name;
    }

    /**
     * Returns the frame in which this lexical unit occurs.
     * 
     * @return the frame
     */
    public Frame getFrame() {
	return frame;
    }

    /**
     * Returns a collection of lexemes in this lexical unit.
     * 
     * @return The lexemes in this lexical unit
     */
    public List<Lexeme> getLexemes() {
	return lexemes;
    }

    /**
     * Generates and returns a string representation of all the lexemes of this
     * lexical unit, concatenated by a space.
     * 
     * @return A string representation of this lexical unit
     */
    public String getLexemeString() {
	StringBuffer buf = new StringBuffer();
	Iterator<Lexeme> lexit = getLexemes().iterator();
	do {
	    buf.append(lexit.next().getValue());
	    if (lexit.hasNext()) {
		buf.append(" ");
	    }
	} while (lexit.hasNext());
	return buf.toString();
    }

    /**
     * Checks whether one of the lexemes is a multi word expression.
     * 
     * @return true if one of the lexemes is a multi word expression
     */
    public boolean containsMultiWordLexeme() {
	for (Lexeme lexeme : lexemes) {
	    if (lexeme.isMultiWord()) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Returns the creation date as a Date object.
     * 
     * @return A date object representing the creation date of this lexical unit
     */
    @Override
    public Date getCreationDate() {
	return creationDate;
    }

    /**
     * Sets the creation date of this lexical unit.
     * 
     * @param myCreationDate
     *            The creation date
     */
    protected void setCreationDate(final Date myCreationDate) {
	this.creationDate = myCreationDate;
    }

}
