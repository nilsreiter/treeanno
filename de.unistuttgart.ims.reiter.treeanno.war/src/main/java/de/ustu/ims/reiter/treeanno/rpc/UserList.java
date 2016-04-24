package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class UserList
 */
@WebServlet("/rpc/userlist")
public class UserList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataLayer dl = CW.getDataLayer(getServletContext());

		User user = CW.getUser(request);

		if (user == null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		Project project;
		Document document;
		try {
			project = dl.getProject(Util.getFirstProjectId(request, response));
			document =
					dl.getDocument(Util.getFirstDocumentId(request, response));
			if (dl.getAccessLevel(project, user) >= Perm.PADMIN_ACCESS) {
				JSONArray arr = new JSONArray();
				for (User u : dl.getUsers()) {
					int al = dl.getAccessLevel(project, u);

					if (al >= Perm.WRITE_ACCESS) {
						JSONObject o = JSONUtil.getJSONObject(u);
						o.put("accesslevel", al);
						UserDocument ud = dl.getUserDocument(u, document);
						if (ud != null) {
							o.put("userDocument", JSONUtil.getJSONObject(ud));
						}
						arr.put(o);
					}
				}
				Util.returnJSON(response, arr);
			}
		} catch (SQLException e) {
			throw new ServletException(e);
		}

	}
}
