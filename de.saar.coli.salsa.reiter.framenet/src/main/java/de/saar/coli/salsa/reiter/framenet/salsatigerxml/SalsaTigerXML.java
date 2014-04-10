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
package de.saar.coli.salsa.reiter.framenet.salsatigerxml;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.saar.coli.salsa.reiter.framenet.CorpusReader;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.ISentence;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;
import de.saar.coli.salsa.reiter.framenet.STXDatabaseReader;

/**
 * This class can be used to "parse" SalsaTigerXML, a format describing
 * FN-annotated text (see Erk and Pado, 2004). SalsaTigerXML contains both
 * syntactic and semantic features. This class only extracts the semantic
 * features using shallow XPath techniques. It does not try to do a complete
 * parse.
 * 
 * The contents of the file is converted to a set of RealizedFrame objects.
 * 
 * This class assumes, that the sentence ids are unique, i.e. that each sentence
 * id occurs only once.
 * 
 * @author Nils Reiter
 * @version 0.3
 * @since 0.2
 * 
 */
public class SalsaTigerXML extends CorpusReader {

    /**
     * The string printed when some targets remain.
     */
    private static final String UNANNOTATED_CROSS_SENTENCE_LINKS =
	    "Some cross-sentence frame element links couldn't be annotated: ";
    /**
     * Used to store open targets, i.e., frame targets that are not found in one
     * sentence.
     */
    private Map<String, Set<RealizedFrameElement>> openTargets = null;

    /**
     * Creates a new SalsaTigerXML object.
     * 
     * @param frameNet
     *            The FrameNet object for this annotation. For now, it should be
     *            of the same FrameNet version. Theoretically, there should be
     *            some mapping possible using framesDiff.xml, but this has not
     *            been tested.
     * @param logger
     *            The logger
     */
    public SalsaTigerXML(final FrameNet frameNet, final Logger logger) {
	super(frameNet, logger);
	openTargets = new HashMap<String, Set<RealizedFrameElement>>();

    }

    /**
     * This method is used to put realized frame elements on the pile, that need
     * to be linked to a target later.
     * 
     * @param target
     *            The target, i.e., the id of the (non)?terminal
     * @param rfe
     *            The realized frame element
     */
    protected final void storeOpenTarget(final String target,
	    final RealizedFrameElement rfe) {
	if (!openTargets.containsKey(target)) {
	    openTargets.put(target, new HashSet<RealizedFrameElement>());
	}
	openTargets.get(target).add(rfe);

    }

    @Override
    public void parse(final File file) throws FrameNotFoundException,
	    FrameElementNotFoundException {
	if (file.isDirectory()) {
	    for (File f : file.listFiles(new FilenameFilter() {
		@Override
		public boolean accept(final File arg0, final String arg1) {
		    return arg1.endsWith(".xml");
		}
	    })) {
		this.parseFile(f);

	    }
	} else if (file.isFile()) {
	    this.parseFile(file);
	}
    }

    /**
     * This method takes a File argument, parses the file and appends the found
     * sentences to the list of sentences found after the creation of the
     * object.
     * 
     * In version 0.2, no validation is done.
     * 
     * @param file
     *            The file containing SalsaTigerXML
     * @throws FrameNotFoundException
     *             This exception is thrown when a frame appears in the file
     *             that has not been declared
     * @throws FrameElementNotFoundException
     *             This exception is thrown when a frame element appears in the
     *             file that has not been declared
     */
    public final void parseFile(final File file) throws FrameNotFoundException,
	    FrameElementNotFoundException {

	// Make document
	Document document = null;
	try {
	    SAXReader reader = new SAXReader();
	    document = reader.read(file);
	} catch (DocumentException e) {
	    getLogger().severe(e.getMessage());
	}
	if (getLogger() != null) {
	    getLogger().info(
		    "XML Document (" + file.getAbsolutePath()
			    + ") has been read.");
	}

	// interpret frames found in the file
	frameNet.readData(new STXDatabaseReader(document));

	List<?> sentences = null;
	if (document.getRootElement().getName().equals("corpus")) {
	    if (getLogger() != null) {
		getLogger().finer(file.getName() + " is a corpus.");
	    }
	    sentences = document.getRootElement().element("body").elements("s");
	} else {
	    if (getLogger() != null) {
		getLogger().finer(file.getName() + " is a subcorpus.");
	    }
	    Element root = document.getRootElement();
	    Element body = root.element("body");
	    if (body == null) {
		sentences = root.elements("s");
	    } else {
		sentences = body.elements("s");
	    }
	}
	if (sentences != null) {
	    for (Object sent : sentences) {
		if (sent != null) {
		    Element sentence = (Element) sent;
		    Sentence s = new Sentence(this, getFrameNet(), sentence);
		    getSentences().add(s);
		    getSentenceIndex().put(s.getIdString(), s);
		}
	    }
	}

	// If there are still some open targets, we re-iterate over the
	// sentences
	if (!openTargets.isEmpty()) {
	    for (ISentence asentence : this.getSentences()) {
		Sentence sentence = (Sentence) asentence;
		sentence.linkOpenTargets();
	    }
	}

	// ... and if there are still some open targets, we issue a warning
	if (!openTargets.isEmpty() && getLogger() != null) {
	    getLogger().warning(
		    UNANNOTATED_CROSS_SENTENCE_LINKS + openTargets.toString());
	}
    }

    /**
     * Returns the map containing (non)?terminal-ids and realized frame
     * elements.
     * 
     * @return A hashmap
     */
    protected final Map<String, Set<RealizedFrameElement>> getOpenTargets() {
	return openTargets;
    }

}
