package de.saar.coli.salsa.reiter.framenet;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultElement;

/**
 * This class allows the export of the FrameNet data into (Salsa)TigerXML, such
 * that the files can be annotated with SalTo.
 * 
 * @author Nils Reiter
 * 
 */
public class STXExporter extends Exporter {

    Namespace namespace = null;

    /**
     * The constructor.
     */
    public STXExporter() {
	this.namespace =
		new Namespace("",
			"http://www.clt-st.de/framenet/frame-database");
    }

    /**
     * Adds the frame definitions to an existing Tiger XML document.
     * 
     * @param frameNet
     *            The FrameNet object
     * @param tigerXML
     *            The XML document
     * @return The new SalsaTigerXML document
     */
    public Document getDocument(final FrameNet frameNet, final Document tigerXML) {
	Element head = (Element) tigerXML.selectSingleNode("//head");
	head.add(this.getXMLElementFrames(frameNet));
	return tigerXML;
    }

    /**
     * Returns a new XML document with the frame definitions and the given
     * corpusId.
     * 
     * @param frameNet
     *            The FrameNet object
     * @param corpusId
     *            The corpus id
     * @return The new XML document
     */
    public Document getDocument(final FrameNet frameNet, final String corpusId) {
	return this.getDocument(frameNet, corpusId, null);
    }

    /**
     * Returns a new XML document with the frame definitions and the given meta
     * element (which contains meta data in the head of the document).
     * 
     * @param frameNet
     *            The FrameNet object
     * @param corpusId
     *            The corpus id
     * @param meta
     *            The meta element
     * @return The new XML document
     */
    public Document getDocument(final FrameNet frameNet, final String corpusId,
	    Element meta) {
	Document document = new DefaultDocument();

	Element corpus = new DefaultElement("corpus");
	Element head = new DefaultElement("head");
	Element body = new DefaultElement("body");
	if (meta == null) {
	    meta = new DefaultElement("meta");
	    Element m_name = new DefaultElement("name");
	    Element m_author = new DefaultElement("author");
	    Element m_date = new DefaultElement("date");
	    Element m_description = new DefaultElement("description");
	    Element m_format = new DefaultElement("format");
	    Element m_history = new DefaultElement("history");

	    m_format.setText("NeGra format, version 3");
	    meta.add(m_name);
	    meta.add(m_author);
	    meta.add(m_date);
	    meta.add(m_description);
	    meta.add(m_format);
	    meta.add(m_history);
	}
	head.add(meta);
	head.add(this.getXMLElementFrames(frameNet));
	corpus.addAttribute("id", corpusId);
	corpus.add(head);
	corpus.add(body);

	document.setRootElement(corpus);

	return document;
    }

    /**
     * Returns a partial XML document, containing just the frame definitions in
     * SalsaTigerXML-style.
     * 
     * @param frameNet
     *            The FrameNet object
     * @return The XML element "frames"
     */
    public Element getXMLElementFrames(final FrameNet frameNet) {
	return this.getXMLElementFrames(frameNet.getFrames());
    }

    /**
     * Returns a partial XML document containing selected frame definitions in
     * SalsaTigerXML-style.
     * 
     * @param frames
     *            The frames we want to export
     * @return The XML element frames
     */
    public Element getXMLElementFrames(final Collection<Frame> frames) {
	Element elem = new DefaultElement("frames");
	elem.add(namespace);
	for (Frame f : frames) {
	    elem.add(this.getFrame(f));
	}
	return elem;
    }

    protected Element getFrame(final Frame frame) {
	Element elem = new DefaultElement("frame");
	elem.addAttribute("name", frame.getName());
	for (FrameElement fe : frame.frameElements()) {
	    elem.add(this.getFrameElement(fe));
	}
	return elem;
    }

    protected Element getFrameElement(final FrameElement fe) {
	Element elem = new DefaultElement("element");
	elem.addAttribute("name", fe.getName());
	elem.addAttribute("optional", String.valueOf((!fe.isCore())));
	return elem;
    }
}
