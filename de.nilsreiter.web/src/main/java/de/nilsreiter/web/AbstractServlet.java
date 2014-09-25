package de.nilsreiter.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nu.xom.ParsingException;
import nu.xom.ValidityException;
import de.nilsreiter.web.beans.menu.Location;
import de.nilsreiter.web.beans.menu.Location.Area;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public abstract class AbstractServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected ServletDocumentManager docMan;

	@Override
	public void init() throws ServletException {
		docMan =
				(ServletDocumentManager) this.getServletContext().getAttribute(
						"docman");
	}

	protected Location getLocation(Area area, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("location") == null) {
			session.setAttribute("location", new Location());

		}
		((Location) session.getAttribute("location")).setArea(area);
		return (Location) session.getAttribute("location");

	}

	protected Location getLocation(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("location") == null) {
			session.setAttribute("location", new Location());

		}
		return (Location) session.getAttribute("location");

	}

	public Document getDocument(HttpServletRequest request) {
		Location loc = this.getLocation(Area.Document, request);
		String docId = request.getParameter("doc");
		if (loc.getOpenObjects(Area.Document).containsKey(docId)) {
			return (Document) loc.getOpenObjects(Area.Document).get(docId);
		} else {
			Document document;
			try {
				document = docMan.getDataReader().read(String.valueOf(docId));
				loc.addOpenObject(Area.Document, docId, document);

				return document;

			} catch (ValidityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	};

	@SuppressWarnings("unchecked")
	public Alignment<Event> getAlignment(HttpServletRequest request) {
		Location loc = this.getLocation(Area.Alignment, request);
		String docId = request.getParameter("doc");
		if (loc.getOpenObjects(Area.Alignment).containsKey(docId)) {
			return (Alignment<Event>) loc.getOpenObjects(Area.Alignment).get(
					docId);
		} else {
			Alignment<Event> alignment;
			try {
				alignment =
						docMan.getAlignmentReader().read(
								(request.getParameter("doc")));
				loc.addOpenObject(Area.Alignment, docId, alignment);
				return alignment;

			} catch (ValidityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	};

	public DocumentSet getDocumentSet(HttpServletRequest request, String docId) {
		Location location = this.getLocation(request);

		if (location.getOpenObjects().containsKey(docId)) {
			return (DocumentSet) location.getOpenObjects().get(docId);
		} else {
			DocumentSet document;
			try {
				document =
						docMan.getDocumentSetReader().read(
								String.valueOf(docId));
				location.addOpenObject(Area.DocumentSet, docId, document);

				return document;

			} catch (ValidityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParsingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

	public DocumentSet getDocumentSet(HttpServletRequest request) {
		Location location = this.getLocation(Area.DocumentSet, request);
		String docId = request.getParameter("doc");

		location.setCurrentObject(Area.DocumentSet, docId);

		return this.getDocumentSet(request, docId);
	};
}
