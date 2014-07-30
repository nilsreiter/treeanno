package de.nilsreiter.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.xom.ParsingException;
import nu.xom.ValidityException;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.data2.impl.DocumentSet_impl;
import de.uniheidelberg.cl.a10.data2.io.DBDataReader;
import de.uniheidelberg.cl.a10.data2.io.DBDocumentSet;
import de.uniheidelberg.cl.a10.data2.io.DBDocumentSetWriter;

/**
 * Servlet implementation class CreateDocumentSet
 */
public class CreateDocumentSet extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	DBDocumentSetWriter dbsw;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			dbsw = new DBDocumentSetWriter(docMan.getDatabase());
			DBDocumentSet dbds = new DBDocumentSet(docMan.getDatabase());
			dbds.initIfTableNotExists();
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		DocumentSet docset =
				new DocumentSet_impl(request.getParameter("setname"));

		DBDataReader dr = docMan.getDataReader();
		Enumeration<String> pEnum = request.getParameterNames();
		while (pEnum.hasMoreElements()) {
			String pName = pEnum.nextElement();
			if (pName.startsWith("doc")) {
				String dId = pName.substring(3);
				try {
					Document document = dr.read(String.valueOf(dId));
					docset.add(document);
				} catch (ValidityException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ParsingException e) {
					e.printStackTrace();
				}
			}
		}
		dbsw.write(docset);
		docMan.documentSetInfo = null;

		response.sendRedirect("view-document-set?doc="
				+ dbsw.getLastCreatedId());

	}
}
