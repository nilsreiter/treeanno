package de.nilsreiter.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import de.nilsreiter.web.beans.menu.Location.Area;
import de.uniheidelberg.cl.a10.data2.Document;

public class EventSimilarityLoaderAlignment extends
		AbstractEventSimilarityLoader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getSelectorJSP() {
		return "alignment/select.jsp";
	}

	@Override
	public String getViewerJSP() {
		return "alignment/event-similarities.jsp";
	}

	@Override
	public List<Document> getDocuments(HttpServletRequest request)
			throws IOException {
		List<Document> documents = new ArrayList<Document>();

		documents.addAll(this.getAlignment(request).getDocuments());

		return documents;

	}

	@Override
	protected Area getArea() {
		return Area.Alignment;
	}

}
