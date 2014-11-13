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

import java.util.SortedSet;

/**
 * This interface provides methods for all annotated FrameNet data, i.e., all
 * classes representing annotations are required to have these methods.
 * 
 * @author Nils Reiter
 * 
 */
public interface IHasTarget {
    /**
     * Returns the target of the annotation.
     * 
     * @return The target of the annotation
     */
    IToken getTarget();

    SortedSet<IToken> getTargetList();

    /**
     * Returns the ith target, if the target is splitted.
     * 
     * @param i
     *            The position
     * @return The target at position i
     */
    IToken getTarget(int i);

    /**
     * This method sets the target of the annotation. If the target list is
     * non-empty, it will be cleared before adding.
     * 
     * @param target
     *            The target of the annotation
     */
    void setTarget(IToken target);

    /**
     * This method adds a token to the target list.
     * 
     * @param target
     *            The target of the annotation
     */
    void addTarget(IToken target);

    /**
     * Sets a list of splitted targets at once.
     * 
     * @param targetList
     *            The list of IToken objects used as target.
     */
    void setTargetList(SortedSet<IToken> targetList);

    /**
     * Returns true, if the target does not exist, i.e., if the frame or frame
     * element is null instantiated.
     * 
     * @return true or false
     */
    boolean isNullInstantiated();

}
