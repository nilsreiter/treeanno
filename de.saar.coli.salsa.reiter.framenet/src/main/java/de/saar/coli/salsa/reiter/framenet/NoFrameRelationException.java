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

/**
 * This exception is thrown when two frames are not in relation, but are thought
 * to be.
 * 
 * @author Nils Reiter
 * 
 */
public class NoFrameRelationException extends FrameNetException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The supposed to be sub frame.
     */
    private Frame subFrame = null;

    /**
     * The supposed to be super frame.
     */
    private Frame superFrame = null;

    /**
     * Stores the two frames and creates the exception.
     * 
     * @param arg0
     *            The frame that is supposed to be the super frame.
     * @param arg1
     *            The frame that is supposed to be the sub frame.
     */
    public NoFrameRelationException(final Frame arg0, final Frame arg1) {
	super(arg1.getName() + " does not inherit from " + arg0.getName());
	this.subFrame = arg1;
	this.superFrame = arg0;
    }

    /**
     * The frame used as sub frame of the not existing relation.
     * 
     * @return the subFrame
     */
    public Frame getSubFrame() {
	return subFrame;
    }

    /**
     * The frame used as super frame of the not existing relation.
     * 
     * @return the superFrame
     */
    public Frame getSuperFrame() {
	return superFrame;
    };

}
