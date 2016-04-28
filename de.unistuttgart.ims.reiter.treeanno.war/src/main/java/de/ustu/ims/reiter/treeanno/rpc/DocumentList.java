package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CA;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentList
 */
@WebServlet("/rpc/documentlist")
public class DocumentList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int projectId =
				Integer.valueOf(request.getParameterValues("projectId")[0]);
		User user = CW.getUser(request);
		try {
			Project proj =
					CW.getDataLayer(getServletContext()).getProject(projectId);
			if (proj == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			if (CW.getDataLayer(getServletContext()).getAccessLevel(proj, user) >= Perm.READ_ACCESS) {
				Collection<Document> list = proj.getDocuments();
				JSONObject obj = new JSONObject();
				for (Document doc : list) {
					UserDocument uDoc =
							CW.getDataLayer(getServletContext())
									.getUserDocument(user, doc);
					obj.append("documents", JSONUtil.getJSONObject(uDoc));
				}
				obj.put("project", JSONUtil.getJSONObject(proj));
				obj.put("accesslevel",
						CW.getDataLayer(getServletContext()).getAccessLevel(
								proj,
								(User) request.getSession().getAttribute(
										CA.USER)));
				this.log(user.getId() + " requests document list.");

				Util.returnJSON(response, obj);
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				this.log(user.getId()
						+ " was denied document list for project "
						+ proj.getId());
				return;
			}
		} catch (SQLException e) {
			this.log(e.getMessage(), e);
			throw new ServletException(e);
		} finally {

		}
	}
}
