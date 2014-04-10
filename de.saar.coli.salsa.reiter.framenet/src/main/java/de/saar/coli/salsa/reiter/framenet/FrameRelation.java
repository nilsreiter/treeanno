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
import java.util.Collection;

/**
 * This class represents a single relation between two individual frames.
 * 
 * 
 * 
 * @author Nils Reiter
 * 
 */
public abstract class FrameRelation implements IHasNameAndID, Serializable {

	/**
	 * 
	 */
	FrameNetRelation frameNetRelation;

	/**
	 * The XML id
	 */
	String id;

	/**
	 * The frame marked as super in the XML document
	 */
	Frame superFrame = null;

	/**
	 * The frame marked as sub in the XML document
	 */
	Frame subFrame = null;

	/**
	 * 
	 */
	Collection<FrameElementRelation> frameElementRelations = null;

	private final static long serialVersionUID = 1L;

	/**
	 * Returns the XML id.
	 * 
	 * @return the id
	 */
	public String getIdString() {
		return id;
	}

	/**
	 * The frame marked as sub frame in the XML files.
	 * 
	 * @return the subFrame
	 */
	public Frame getSubFrame() {
		return subFrame;
	}

	/**
	 * The frame marked as superframe in the XML files.
	 * 
	 * @return the superFrame
	 */
	public Frame getSuperFrame() {
		return superFrame;
	}

	/**
	 * @return the frameNetRelation
	 */
	protected FrameNetRelation getFrameNetRelation() {
		return frameNetRelation;
	}

	/**
	 * Returns the name of the framenet relation this frame relation belongs to.
	 */
	@Override
	public String toString() {
		return getFrameNetRelation().getName();
	}

	public String getName() {
		return toString();
	}

	protected void setFrameNetRelation(FrameNetRelation frameNetRelation) {
		this.frameNetRelation = frameNetRelation;
	}

	protected Collection<FrameElementRelation> getFrameElementRelations() {
		return frameElementRelations;
	}
}
