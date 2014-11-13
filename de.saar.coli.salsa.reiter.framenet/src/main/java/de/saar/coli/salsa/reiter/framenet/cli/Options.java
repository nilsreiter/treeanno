/**
 * 
 * Copyright 2010 by Nils Reiter.
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

import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * 
 * 
 * We are using the args4j package: https://args4j.dev.java.net/.
 * 
 * @author reiter
 * 
 */
public class Options {
	@Option(name = "--fnhome")
	String fnhome = "/usr/local/framenet";

	@Option(name = "--action", usage = "The action to perform")
	String action = null;

	@Option(name = "--definition")
	boolean definition = false;

	@Option(name = "--creationDate")
	boolean creationDate = false;

	@Option(name = "--frameElements", usage = "Show a list of frame elements for each frame")
	boolean frameElements = false;

	@Option(name = "--lexicalUnits", usage = "Show a list of lexical units for each frame")
	boolean lexicalUnits = false;

	@Option(name = "--treeUp")
	boolean treeUp = false;

	@Option(name = "--treeDown")
	boolean treeDown = false;

	@Argument
	private List<String> arguments = new ArrayList<String>();

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public String getFnhome() {
		return fnhome;
	}

	public void setFnhome(String fnhome) {
		this.fnhome = fnhome;
	}

	public boolean isDefinition() {
		return definition;
	}

	public boolean isCreationDate() {
		return creationDate;
	}

	public boolean isFrameElements() {
		return frameElements;
	}

	public boolean isTreeUp() {
		return treeUp;
	}

	public boolean isTreeDown() {
		return treeDown;
	}

	public boolean isLexicalUnits() {
		return lexicalUnits;
	}

}
