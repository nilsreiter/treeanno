/**
 * 
 * Copyright 2009-2010 by Nils Reiter.
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
package de.saar.coli.salsa.reiter.framenet.cli;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;

public class Util {
	static Collection<Frame> convert(FrameNet frameNet,
			Collection<String> framenames) {
		List<Frame> ret = new LinkedList<Frame>();
		for (String s : framenames) {
			try {
				ret.add(frameNet.getFrame(s));
			} catch (FrameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
}
