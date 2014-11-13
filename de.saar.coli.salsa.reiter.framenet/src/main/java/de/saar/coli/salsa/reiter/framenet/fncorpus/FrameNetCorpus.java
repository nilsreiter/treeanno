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

import java.io.File;
import java.util.logging.Logger;

import de.saar.coli.salsa.reiter.framenet.CorpusReader;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.ParsingException;

/**
 * This class reads in and organized the FrameNet corpus - or, to be precise,
 * parts of it. It can retrieve information about the frame and frame element
 * assignment in the different sentences.
 * 
 * @since 0.3
 * @author Nils Reiter
 * 
 */
public abstract class FrameNetCorpus extends CorpusReader {

    protected FrameNetCorpus(final FrameNet frameNet, final Logger logger) {
	super(frameNet, logger);
    }

    @Override
    public abstract void parse(final File file) throws FrameNotFoundException,
	    FrameElementNotFoundException, ParsingException;

}
