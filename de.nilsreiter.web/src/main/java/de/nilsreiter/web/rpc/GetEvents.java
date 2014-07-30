package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.json.JSONObject;

import de.nilsreiter.web.AbstractServlet;
import de.nilsreiter.web.json.JSONConversion;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;

/**
 * Servlet implementation class GetEvents
 */
public class GetEvents extends AbstractServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("doc") == null) {
			response.getWriter().print("");
			return;

		}
		Document document;
		try {
			document = docMan.getDataReader().read(request.getParameter("doc"));

			JSONObject json = new JSONObject();
			json.put("id", document.getId());
			for (Event event : document.getEvents()) {
				json.append("events", JSONConversion.getEvent(event));
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			out.flush();
			out.close();
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

	}

}
