package de.uniheidelberg.cl.a10.data2.alignment.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import nu.xom.Element;
import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.AlignmentIdProvider;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.impl.AlignmentIdProvider_impl;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
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
public class TokenAlignmentReader extends
AbstractLinkedXMLReader<Alignment<Token>> {

	public TokenAlignmentReader(DataStreamProvider dsProvider) {
		super(dsProvider);
	}

	public TokenAlignmentReader(final File dDirectory) {
		super(dDirectory);
	}

	@Deprecated
	public TokenAlignmentReader(final DataReader dr, final File dDirectory) {
		super(dr, dDirectory);
	}

	@Override
	protected Alignment<Token> read(final Element rootElement)
			throws IOException {
		AlignmentIdProvider idp = new AlignmentIdProvider_impl();
		Alignment<Token> ad =
				new Alignment_impl<Token>(
						rootElement.getAttributeValue(XMLConstants.ID));

		for (Element documentElement : getElements(rootElement, "document")) {
			String id = documentElement.getAttributeValue(XMLConstants.ID);
			ad.getDocuments().add(this.getRitualDocument(id));
		}
		for (Element alignmentElement : getElements(rootElement, "alignment")) {
			String id;
			if (alignmentElement.getAttribute(XMLConstants.ID) != null)
				id = alignmentElement.getAttributeValue(XMLConstants.ID);
			else
				id = idp.getNextAlignmentId();
			Collection<Token> aligned = new HashSet<Token>();
			for (Element partElement : getElements(alignmentElement, "part")) {
				String docId = partElement.getAttributeValue("doc");
				String frameId = partElement.getAttributeValue("frame");
				Token token =
						(Token) this.getRitualDocument(docId).getById(frameId);
				if (token == null) {
					System.err.println("Token " + frameId + " not found in "
							+ docId + ".");
				}
				aligned.add(token);
			}
			Link<Token> l = ad.addAlignment(id, aligned);
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
