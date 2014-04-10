package de.saar.coli.salsa.reiter.framenet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * This class imports FrameNet data from a SalsaTigerXML file. In Salto, frames
 * and frame elements can be defined ad-hoc. This class can be used to extract
 * them from the file and merge them with another (original) FrameNet database.
 * 
 * @author Nils Reiter
 * @since 0.4
 * 
 */
public class STXDatabaseReader extends DatabaseReader {

    /**
     * The XML document with the SalsaTigerXML information.
     */
    Document document;

    /**
     * The used namespaces.
     */
    Map<String, String> namespaces = null;

    /**
     * Creates the object by parsing the given file.
     * 
     * @param salsaTigerFile
     *            The file containing the frame definitions
     */
    public STXDatabaseReader(final File salsaTigerFile) {
	super();
	try {
	    SAXReader reader = new SAXReader();
	    document = reader.read(salsaTigerFile);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
	init();
    }

    /**
     * If the file has already been parsed, this constructor can be used.
     * 
     * @param document
     *            The parsed document
     */
    public STXDatabaseReader(final Document document) {
	super();
	this.document = document;

	init();
    }

    private void init() {
	namespaces = new HashMap<String, String>();
	namespaces.put("clt", "http://www.clt-st.de/framenet/frame-database");
    }

    @Override
    public boolean read(final FrameNet fn) {

	List<?> frames =
		document.getRootElement().element("head").element("frames")
			.elements("frame");

	for (Object frameNode : frames) {
	    if (frameNode != null) {
		Element frameX = (Element) frameNode;
		Frame frame = new STXFrame(fn, frameX, namespaces);
		try {
		    fn.getFrame(frame.getName());
		} catch (FrameNotFoundException e) {
		    frame.linkFrameNet(fn);
		}

	    }
	}
	return true;
    }

    @Override
    protected void cleanup() {

    };

}
