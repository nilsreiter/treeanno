package de.uniheidelberg.cl.a10.data2.alignment.io;

import java.io.OutputStream;

import nu.xom.Attribute;
import nu.xom.Element;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.io.AbstractXMLWriter;
import de.uniheidelberg.cl.a10.io.XMLConstants;

public class AlignmentWriter extends
AbstractXMLWriter<Alignment<? extends AnnotationObjectInDocument>> {

	public AlignmentWriter(final OutputStream os) {
		super(os);
	}

	@Override
	public Element getElement(
			final Alignment<? extends AnnotationObjectInDocument> a) {
		Element rootElement = new Element("nalignment");
		rootElement.addAttribute(new Attribute(XMLConstants.ID, a.getId()));
		if (a.getTitle() != null)
			rootElement.addAttribute(new Attribute(XMLConstants.TITLE, a
					.getTitle()));
		// rootElement
		// .addAttribute(new Attribute("xmlns",
		// "http://www.cl.uni-heidelberg.de/~reiter/NAryAlignmentDocument"));

		for (de.uniheidelberg.cl.a10.data2.Document rd : a.getDocuments()) {
			Element containerElement = new Element("document");
			containerElement.addAttribute(new Attribute(XMLConstants.ID, rd
					.getId()));
			rootElement.appendChild(containerElement);
		}

		// frame alignments
		for (Link<? extends AnnotationObjectInDocument> fa : a.getAlignments()) {
			Element alElement = new Element("alignment");
			if (fa.getId() != null)
				alElement.addAttribute(new Attribute(XMLConstants.ID, fa
						.getId()));
			if (fa.getStatus() != null)
				alElement.addAttribute(new Attribute("status", fa.getStatus()
						.toString()));
			if (fa.getDescription() != null)
				alElement.addAttribute(new Attribute(XMLConstants.DESCRIPTION,
						fa.getDescription()));
			if (!Double.isNaN(fa.getScore()))
				alElement.addAttribute(new Attribute(XMLConstants.SCORE, String
						.valueOf(fa.getScore())));

			for (AnnotationObjectInDocument f : fa.getElements()) {
				Element partElement = new Element("part");
				partElement.addAttribute(new Attribute("doc", f
						.getRitualDocument().getId()));
				partElement.addAttribute(new Attribute("frame", f.getId()));
				alElement.appendChild(partElement);
			}

			rootElement.appendChild(alElement);
		}

		return rootElement;
	}

}
