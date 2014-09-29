package de.uniheidelberg.cl.a10.data2.alignment.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import nu.xom.Element;
import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.HasDocument;
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

public class AlignmentReader<T extends HasDocument> extends
		AbstractLinkedXMLReader<Alignment<T>> {

	public AlignmentReader(DataStreamProvider dsProvider) {
		super(dsProvider);
	}

	public AlignmentReader(final File dDirectory) {
		super(dDirectory);
	}

	@Override
	protected Alignment_impl<T> read(final Element rootElement)
			throws IOException {
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		String did = "";
		if (rootElement.getAttributeValue("id") != null)
			did = rootElement.getAttributeValue("id");
		Alignment_impl<T> ad = new Alignment_impl<T>(did);

		if (rootElement.getAttribute(XMLConstants.TITLE) != null) {
			ad.setTitle(rootElement.getAttributeValue(XMLConstants.TITLE));
		}
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
			Collection<T> aligned = new HashSet<T>();
			for (Element partElement : getElements(alignmentElement, "part")) {
				String docId = partElement.getAttributeValue("doc");
				String frameId = partElement.getAttributeValue("frame");
				T token = (T) this.getRitualDocument(docId).getById(frameId);
				if (token == null) {
					System.err.println("Token " + frameId + " not found in "
							+ docId + ".");
				}
				aligned.add(token);
			}
			Link<T> l = ad.addAlignment(id, aligned);
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
