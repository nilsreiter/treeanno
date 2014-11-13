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
 * This enum is used to store the version of FrameNet. Currently, we support
 * FrameNet 1.2, 1.3, 1.4-alpha and 1.5
 * 
 * @author reiter
 * 
 */
public enum FrameNetVersion {
	V15, V14, V13, V12;

	/**
	 * This controls the default version, i.e., the version assumed when the
	 * given string can't be parsed.
	 */
	static FrameNetVersion defaultVersion = V13;

	/**
	 * Returns the FrameNetVersion object for a string.
	 * 
	 * @param s
	 *            The string
	 * @return The FrameNetVersion
	 */
	public static FrameNetVersion fromString(String s) {
		if (s.matches("1.2")) {
			return V12;
		} else if (s.matches("1.3")) {
			return V13;
		} else if (s.matches("1.4")) {
			return V14;
		} else if (s.matches("1.5")) {
			return V15;
		}
		return defaultVersion;
	}
}
