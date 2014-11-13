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
package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.Date;

import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.IHasCreationDate;
import de.saar.coli.salsa.reiter.framenet.IHasID;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;

/**
 * This class represents an annotation set from the FrameNet annotation. An
 * annotation set is connected to a single realized frame and a sentence.
 * 
 * @author Nils Reiter
 * @since 0.2
 */
public abstract class AnnotationSet implements IHasID, IHasCreationDate {
    /**
     * The identifier of the annotation set.
     */
    protected String id;

    /**
     * The status of the annotation set.
     */
    protected String status;

    /**
     * The frame occurring in this annotation set.
     */
    protected Frame frame;

    /**
     * The lexical unit evoking the frame.
     */
    LexicalUnit lexicalUnit;

    /**
     * The XML element "frameRef".
     */
    String frameRef;

    /**
     * The XML element "luRef".
     */
    String luRef;

    /**
     * The realized frame.
     */
    protected RealizedFrame realizedFrame;

    /**
     * The sentence which is annotated by this AnnotationSet.
     */
    protected Sentence sentence;

    /**
     * 
     */
    Date creationDate;

    @Override
    public String getIdString() {
	return id;
    }

    /**
     * Returns the RealizedFrame object of this annotation set.
     * 
     * @return the realized frame
     */
    public RealizedFrame getRealizedFrame() {
	return realizedFrame;
    }

    /**
     * @return the frame
     */
    public Frame getFrame() {
	return frame;
    }

    protected void init(final Element element) {
	id = element.attributeValue("ID");
	status = element.attributeValue("status");
    }

    /**
     * @return the creationDate
     */
    @Override
    public Date getCreationDate() {
	return creationDate;
    }

    @Override
    public String getCDate() {
	return frame.getFramenet().getDateFormat().format(creationDate);
    }

    /**
     * @param creationDate
     *            the creationDate to set
     */
    public void setCreationDate(final Date creationDate) {
	this.creationDate = creationDate;
    }

    /**
     * @return the lexicalUnit
     */
    public LexicalUnit getLexicalUnit() {
	return lexicalUnit;
    }

}
