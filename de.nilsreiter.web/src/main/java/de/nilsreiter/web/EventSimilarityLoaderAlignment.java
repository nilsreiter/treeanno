package de.nilsreiter.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import nu.xom.ParsingException;
import nu.xom.ValidityException;
import de.nilsreiter.web.beans.menu.Location.Area;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentReader;

public class EventSimilarityLoaderAlignment extends
		AbstractEventSimilarityLoader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DBAlignmentReader<Event> alignmentReader;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			alignmentReader = docMan.getAlignmentReader();
		} catch (SQLException e) {
			throw new ServletException(e);
		}

	}

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

		try {
			documents.addAll(alignmentReader
					.read((request.getParameter("doc"))).getDocuments());
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
		return Area.Alignment;
	}

}
