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
 * This interface is used to enable the FrameNet object to ping other threads
 * like progress bars etc. The implementing class implements the ping() method,
 * which is called whenever the frame loading makes some progress.
 * 
 * <i>This interface is not used in version 0.1, but expected to be used in
 * future versions.</i>
 * 
 * @author Nils Reiter
 * 
 */
public interface IPingable extends Runnable {
    /**
     * The method to be called when progress occurs.
     * 
     */
    void ping();

    /**
     * The frame loading procedure tries to set the maximal number of pings
     * required before starting the loading procedure.
     * 
     * @param n
     *            The number of pings required
     */
    void setMax(int n);
}
