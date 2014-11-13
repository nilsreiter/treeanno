package de.saar.coli.salsa.reiter.framenet;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * This class is for reading FrameNet 1.5 data.
 * 
 * @author reiter
 * @since 0.4.2
 * 
 */
public class FNDatabaseReader15 extends FNDatabaseReader {

    public FNDatabaseReader15(final File fnhome, final boolean validate)
	    throws FileNotFoundException, SecurityException {
	framesFilename = "frameIndex.xml";
	frrelationFilename = "frRelation.xml";
	semtypesFilename = "semTypes.xml";
	dateFormat =
		new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z EEE", Locale.US);

	init(fnhome, validate, new HashSet<String>());

    }

    @Override
    protected void loadAllSemanticTypes(final FrameNet fn)
	    throws FrameNetFilesNotFoundException {
	try {
	    List<?> frames =
		    getSemTypesDocument().getRootElement().elements("semType");
	    for (Object frame : frames) {
		loadSemanticTypeNode(fn, (org.dom4j.Element) frame);
	    }
	} catch (ParsingException e) {
	    e.printStackTrace();
	}

    }

    @Override
    protected int loadAllFrames(final FrameNet fn) throws FileNotFoundException {
	try {

	    List<?> frames =
		    getFramesDocument().getRootElement().elements("frame");
	    for (Object frame : frames) {
		// this is just a part of the index
		Element el = (Element) frame;
		String frameName = el.attributeValue("name"); // corresponds to
							      // the file name
		File frameFile = initFile("frame/" + frameName + ".xml");
		Document frameDoc = parse(frameFile.toURI());
		loadFrameNode(fn, frameDoc.getRootElement());
	    }
	    return frames.size();
	} catch (ParsingException e) {
	    e.printStackTrace();
	}
	return 0;
    }

    @Override
    protected void loadAllFrameRelations(final FrameNet fn)
	    throws FileNotFoundException {
	try {
	    List<?> nodes =
		    getFrRelationDocument().getRootElement().elements(
			    "frameRelationType");
	    for (Object node : nodes) {
		loadFrameRelationNode(fn, (org.dom4j.Element) node);
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

	    Frame f = new FNFrame15(elem, this);

	    frameNet.getAllFrames().put(f.getName(), f);
	    frameNet.getLogger().log(Level.FINEST,
		    "Framenode loaded. Frame " + f.getName());
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
	SemanticType st = new FNSemanticType15(this, node);
	frameNet.semanticTypeIndex.add(st);
	frameNet.getLogger().log(Level.INFO,
		"SemanticTypeNode loaded. Semantic Type: " + st.getName());
    }

    @Override
    protected FrameNetRelation loadFrameRelationNode(final FrameNet frameNet,
	    final Element node) {
	FrameNetRelation frel = new FNFrameNetRelation15(this, node);
	frameNet.frameRelations.put(frel.getName(), frel);
	return frel;
    }

    @Override
    public void addDefaultFrameAliases() {

    }
}
