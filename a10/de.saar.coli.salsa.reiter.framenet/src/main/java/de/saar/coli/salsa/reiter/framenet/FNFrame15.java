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

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

import org.dom4j.Element;

import de.uniheidelberg.cl.reiter.pos.IPartOfSpeech;

/**
 * This class implements frames from FrameNet 1.5.
 * 
 * @author reiter
 * @since 0.4.2
 * 
 */
public class FNFrame15 extends FNFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     * 
     * @param node
     *            The XML element representing the frame
     * @param reader
     *            The database reader
     */
    protected FNFrame15(final Element node, final FNDatabaseReader15 reader) {
	name = node.attributeValue("name");
	id = node.attributeValue("ID");
	try {
	    creationDate =
		    reader.getDateFormat().parse(node.attributeValue("cDate"));
	} catch (ParseException e) {
	    creationDate = null;
	    e.printStackTrace();
	}
	if (node.attributeValue("Source") != null) {
	    source = node.attributeValue("Source");
	}
	definition =
		node.element("definition").getText().replace('\n', ' ')
			.getBytes();

	linkFrameNet(reader.getFrameNet());

	// Lexical Units
	reader.getFrameNet().getLogger()
		.log(Level.FINE, "Loading lexical units ...");
	lexicalUnits = new HashSet<LexicalUnit>();
	lexicalUnitsPerPOS = new HashMap<IPartOfSpeech, Integer>();

	List<?> lexunitNodelist = node.elements("lexUnit");
	for (int i = 0; i < lexunitNodelist.size(); i++) {
	    Element lu = (Element) lexunitNodelist.get(i);

	    LexicalUnit luo = new FNLexicalUnit15(reader, this, lu);
	    lexicalUnits.add(luo);
	    if (!lexicalUnitsPerPOS.containsKey(luo.getPartOfSpeech())) {
		lexicalUnitsPerPOS.put(luo.getPartOfSpeech(), 1);
	    } else {
		Integer oldval = lexicalUnitsPerPOS.get(luo.getPartOfSpeech());
		lexicalUnitsPerPOS.put(luo.getPartOfSpeech(), (oldval + 1));
	    }
	}

	// Frame Elements
	reader.getFrameNet().getLogger()
		.log(Level.FINE, "Loading frame elements ...");
	frameElements = new HashMap<String, FrameElement>();

	List<?> feNodelist = node.elements("FE");
	for (int i = 0; i < feNodelist.size(); i++) {
	    Element fe = (Element) feNodelist.get(i);
	    frameElements.put(fe.attributeValue("name"), new FNFrameElement15(
		    reader, this, fe));
	}

	framenet.getLogger().log(Level.INFO,
		"Frame " + name + " newly created.");
    }
}
