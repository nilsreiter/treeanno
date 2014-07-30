package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.nilsreiter.web.AbstractServlet;
import de.uniheidelberg.cl.a10.data2.io.DBDocumentSet;

/**
 * Servlet implementation class DeleteDocumentSet
 */
public class DeleteDocumentSet extends AbstractServlet {
	private static final long serialVersionUID = 1L;
	DBDocumentSet dbds;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			dbds = new DBDocumentSet(docMan.getDatabase());
			dbds.initIfTableNotExists();
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String docId = request.getParameter("doc");
		if (request.getParameter("delete") != null) {
			try {
				dbds.deleteDocumentSet(docId);
			} catch (SQLException e) {
				throw new ServletException(e);
			}
			JSONObject json = new JSONObject();
			json.put("delete", docId);

			PrintWriter out = response.getWriter();
			out.print(json.toString());
			out.flush();
			out.close();

		}
	}
}
