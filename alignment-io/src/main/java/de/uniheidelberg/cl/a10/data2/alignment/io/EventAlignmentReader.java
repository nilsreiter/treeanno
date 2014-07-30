package de.uniheidelberg.cl.a10.data2.alignment.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import nu.xom.Element;
import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.io.AbstractLinkedXMLReader;
import de.uniheidelberg.cl.a10.io.XMLConstants;

/**
 * This class implements methods for reading Frame and FrameElement alignments
 * from an xml file into a AlignmentDocument object.
 * 
 * @author hartmann
 * 
 */

@Deprecated
public class EventAlignmentReader extends
AbstractLinkedXMLReader<Alignment<Event>> {

	public EventAlignmentReader(DataStreamProvider dsProvider) {
		super(dsProvider);
	}

	public EventAlignmentReader(final File dDirectory) {
		super(dDirectory);
	}

	@Override
	protected Alignment<Event> read(final Element rootElement)
			throws IOException {
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		Alignment<Event> ad =
				new Alignment_impl<Event>(rootElement.getAttributeValue("id"));

		for (Element documentElement : getElements(rootElement, "document")) {
			String id = documentElement.getAttributeValue("id");
			ad.getDocuments().add(this.getRitualDocument(id));
		}
		for (Element alignmentElement : getElements(rootElement, "alignment")) {
			String id;
			if (alignmentElement.getAttribute(XMLConstants.ID) != null)
				id = alignmentElement.getAttributeValue("id");
			else
				id = idp.getNextAlignmentId();
			Collection<Event> aligned = new HashSet<Event>();
			for (Element partElement : getElements(alignmentElement, "part")) {
				String docId = partElement.getAttributeValue("doc");
				String frameId = partElement.getAttributeValue("frame");
				Event token =
						(Event) this.getRitualDocument(docId).getById(frameId);
				if (token == null) {
					System.err.println("Token " + frameId + " not found in "
							+ docId + ".");
				}
				aligned.add(token);
			}
			Link<Event> l = ad.addAlignment(id, aligned);
			if (alignmentElement.getAttribute(XMLConstants.SCORE) != null)
				l.setScore(Double.valueOf(alignmentElement
						.getAttributeValue(XMLConstants.SCORE)));
			if (alignmentElement.getAttribute(XMLConstants.DESCRIPTION) != null)
				l.setDescription(alignmentElement
						.getAttributeValue(XMLConstants.DESCRIPTION));
		}

		return ad;
	}
}
