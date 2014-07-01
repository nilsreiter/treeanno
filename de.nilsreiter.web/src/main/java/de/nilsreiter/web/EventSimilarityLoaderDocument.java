package de.nilsreiter.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import de.nilsreiter.web.Location.Area;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class EventSimilarityLoaderDocument extends
		AbstractEventSimilarityLoader {
	DataReader dataReader = new DataReader();

	private static final long serialVersionUID = 1L;

	@Override
	public String getSelectorJSP() {
		return "document/select.jsp";
	}

	@Override
	public String getViewerJSP() {
		return "document/event-similarities.jsp";
	}

	@Override
	public List<Document> getDocuments(HttpServletRequest request)
			throws IOException {
		List<Document> documents = new ArrayList<Document>();
		documents.add(dataReader.read(docMan.findStreamFor(request
				.getParameter("doc"))));

		return documents;
	}

	@Override
	protected Location getLocation() {
		return new Location("Rituals", Area.Document);
	}

}
