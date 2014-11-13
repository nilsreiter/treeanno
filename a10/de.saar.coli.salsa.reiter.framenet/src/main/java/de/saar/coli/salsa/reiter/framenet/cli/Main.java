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

import java.io.File;
import java.io.FileNotFoundException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader;
import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNetVersion;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IFrameNetObject;
import de.saar.coli.salsa.reiter.framenet.ParsingException;

public class Main {

	public static void main(String[] args) {
		Options options = new Options();
		CmdLineParser parser = new CmdLineParser(options);
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getLocalizedMessage());
			parser.printUsage(System.err);
			System.exit(1);
		}

		if (options.getAction() != null) {
			String action = options.getAction();
			if (action.equals("display")) {
				FrameNet fn = new FrameNet();
				try {
					fn.readData(FNDatabaseReader.createInstance(new File(
							options.getFnhome()), FrameNetVersion
							.fromString("1.3")));
				} catch (FileNotFoundException e) {
					System.err.println(e.getLocalizedMessage());
					System.exit(1);
				} catch (SecurityException e) {
					System.err.println(e.getLocalizedMessage());
					System.exit(1);
				}
				for (String obj : options.getArguments()) {
					IFrameNetObject fnobj = null;
					try {
						if (obj.contains(".")) {
							fnobj = fn.getFrameElement(obj);
						} else {
							fnobj = fn.getFrame(obj);
						}
						System.out.println("------------------------");
						System.out.println("Name: " + fnobj.getName());
						if (options.isDefinition()) {
							System.out.println("Definition: "
									+ fnobj.getDefinition());
						}
						if (options.isCreationDate()) {
							System.out.println("Creation Date: "
									+ fnobj.getCDate());
						}
						if (Frame.class.isAssignableFrom(fnobj.getClass())
								&& options.isFrameElements()) {
							System.out.println("Frame Elements: "
									+ ((Frame) fnobj).frameElements());
						}
						if (Frame.class.isAssignableFrom(fnobj.getClass())
								&& options.isLexicalUnits()) {
							System.out.println("Lexical Units: "
									+ ((Frame) fnobj).getLexicalUnits());
						}
						if (options.isTreeDown()) {
							System.out.println(fnobj.treeDownInfo());
							System.out.println(fnobj.treeDown());
						}
						if (options.isTreeUp()) {
							System.out.println(fnobj.treeUpInfo());
							System.out.println(fnobj.treeUp());
						}
					} catch (ParsingException e) {
						System.err.println(e.getLocalizedMessage());
					} catch (FrameElementNotFoundException e) {
						System.err.println(e.getLocalizedMessage());
					} catch (FrameNotFoundException e) {
						System.err.println(e.getLocalizedMessage());
					}
				}
			}
		} else {
			parser.printUsage(System.err);
			System.exit(0);
		}
	}
}
