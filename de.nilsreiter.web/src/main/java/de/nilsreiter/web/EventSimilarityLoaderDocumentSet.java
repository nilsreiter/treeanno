package de.nilsreiter.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import de.nilsreiter.web.Location.Area;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.alignment.io.EventAlignmentReader;

public class EventSimilarityLoaderDocumentSet extends
		AbstractEventSimilarityLoader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventAlignmentReader alignmentReader;

	@Override
	public void init() throws ServletException {
		super.init();
		alignmentReader = new EventAlignmentReader(docMan);

	}

	@Override
	public String getSelectorJSP() {
		return "documentset/select.jsp";
	}

	@Override
	public String getViewerJSP() {
		return "documentset/event-similarities.jsp";
	}

	@Override
	public List<Document> getDocuments(HttpServletRequest request)
			throws IOException {
		List<Document> documents = new ArrayList<Document>();

		documents.addAll(alignmentReader.read(
				docMan.findStreamFor(request.getParameter("doc")))
				.getDocuments());
		return documents;

	}

	@Override
	protected Location getLocation() {
		return new Location("Rituals", Area.Alignment);
	}

}
