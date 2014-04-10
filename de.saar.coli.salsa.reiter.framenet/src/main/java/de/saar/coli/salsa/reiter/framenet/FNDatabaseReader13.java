package de.saar.coli.salsa.reiter.framenet;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.dom4j.Element;
import org.dom4j.Node;

/**
 * This class is for reading FrameNet 1.3 data.
 * 
 * @author reiter
 * @since 0.4.2
 * 
 */

public class FNDatabaseReader13 extends FNDatabaseReader {

    /**
     * 
     * @param fnhome
     *            The FNHOME directory
     * @param validate
     *            Whether we want to validate the directory
     * @throws FileNotFoundException
     *             If the FrameNet files are not found
     */
    public FNDatabaseReader13(final File fnhome, final boolean validate)
	    throws FileNotFoundException {
	framesFilename = "frames.xml";
	frrelationFilename = "frRelation.xml";
	semtypesFilename = "semtypes.xml";
	dateFormat =
		new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
	init(fnhome, validate, new HashSet<String>());

    }

    /**
     * Called internally to load the frame relations.
     * 
     * @param fn
     *            The FrameNet object
     * @throws FileNotFoundException
     *             If the FrameNet database files can not be found.
     */
    @Override
    protected void loadAllFrameRelations(final FrameNet fn)
	    throws FileNotFoundException {
	try {
	    List<?> nodes =
		    this.getFrRelationDocument().getRootElement()
			    .elements("frame-relation-type"); // xpath.selectNodes(getFrRelationDocument());
	    for (Object node : nodes) {
		loadFrameRelationNode(fn, (org.dom4j.Element) node);
	    }
	} catch (ParsingException e) {
	    e.printStackTrace();
	}
    }

    /**
     * This method loads all frames in the frames.xml file. For each frame, a
     * complete Frame-object is created and stores all its data. It is *much*
     * faster to run this method before doing any framewise work. So it is
     * recommended to run this method directly after the start.
     * 
     * @return The number of frames loaded from the XML file.
     * @param fn
     *            The FrameNet object
     * @throws FileNotFoundException
     *             If the FrameNet files are not found
     */
    @Override
    protected int loadAllFrames(final FrameNet fn) throws FileNotFoundException {
	try {
	    List<?> frames =
		    this.getFramesDocument().getRootElement().elements("frame"); // xpath.selectNodes(getFramesDocument());
	    for (Object frame : frames) {
		loadFrameNode(fn, (org.dom4j.Element) frame);
	    }
	    return frames.size();
	} catch (ParsingException e) {
	    e.printStackTrace();
	}
	return 0;
    }

    @Override
    protected void loadAllSemanticTypes(final FrameNet fn)
	    throws FrameNetFilesNotFoundException {
	try {
	    List<?> frames =
		    getSemTypesDocument().getRootElement().elements("semType");// xpath.selectNodes(getSemTypesDocument());
	    for (Object frame : frames) {
		loadSemanticTypeNode(fn, (org.dom4j.Element) frame);
	    }
	} catch (ParsingException e) {
	    e.printStackTrace();
	}

    }

    @Override
    protected Frame loadFrameNode(final FrameNet frameNet, Node node) {
	if (node.getNodeType() != Node.ELEMENT_NODE) {
	    return null;
	}
	org.dom4j.Element elem = (org.dom4j.Element) node;
	if (!frameNet.getAllFrames().containsKey(elem.attributeValue("name"))) {

	    Frame f = new FNFrame13(elem, this);

	    frameNet.getAllFrames().put(f.getName(), f);
	    node = null;
	    return f;

	}
	return null;
    }

    @Override
    protected void loadSemanticTypeNode(final FrameNet frameNet,
	    final Element node) {
	if (frameNet.semanticTypeIndex != null) {
	    for (SemanticType st : frameNet.semanticTypeIndex) {
		if (st.getName().equalsIgnoreCase(node.attributeValue("name"))) {
		    // st.supplyNode(node);
		    return;
		}
	    }
	} else {
	    frameNet.semanticTypeIndex = new HashSet<SemanticType>();
	}
	if (frameNet.semanticTypeIndex == null) {
	    frameNet.semanticTypeIndex = new HashSet<SemanticType>();
	}
	SemanticType st = new FNSemanticType13(this, node);
	frameNet.semanticTypeIndex.add(st);
	frameNet.log(Level.INFO, "SemanticTypeNode loaded. Semantic Type: "
		+ st.getName());
    }

    @Override
    protected FrameNetRelation loadFrameRelationNode(final FrameNet frameNet,
	    final Element node) {
	FrameNetRelation frel = new FNFrameNetRelation13(this, node);
	frameNet.frameRelations.put(frel.getName(), frel);
	return frel;
    }

    @Override
    public void addDefaultFrameAliases() {
	try {
	    frameNet.addFrameAlias(frameNet.getFrame("Eventive_affecting"),
		    "Change_of_relation");
	} catch (FrameNotFoundException e) {
	    e.printStackTrace();
	}
    }
}
