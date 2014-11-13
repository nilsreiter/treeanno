package de.nilsreiter.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nu.xom.ParsingException;
import nu.xom.ValidityException;
import de.nilsreiter.web.beans.menu.Location.Area;
import de.uniheidelberg.cl.a10.data2.Document;

public class EventSimilarityLoaderDocument extends
		AbstractEventSimilarityLoader {

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
		try {
			documents.add(dataReader.read((request.getParameter("doc"))));
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return documents;
	}

	@Override
	protected Area getArea() {
		return Area.Document;
	}

}
