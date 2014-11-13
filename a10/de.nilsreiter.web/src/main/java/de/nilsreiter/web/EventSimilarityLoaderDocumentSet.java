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
import de.uniheidelberg.cl.a10.data2.io.DBDocumentSetReader;

public class EventSimilarityLoaderDocumentSet extends
		AbstractEventSimilarityLoader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DBDocumentSetReader dbsr;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			dbsr = docMan.getDocumentSetReader();
		} catch (SQLException e) {
			throw new ServletException(e);
		}

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

		try {
			documents.addAll(dbsr.read((request.getParameter("doc"))).getSet());
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
		return Area.DocumentSet;
	}

}
