package de.ustu.ims.reiter.treeanno.rpc;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;

import de.ustu.ims.reiter.treeanno.CA;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;
import de.ustu.ims.reiter.treeanno.util.Util;

/**
 * Servlet implementation class DocumentList
 */
@Deprecated
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
				ForeignCollection<Document> list = proj.getDocuments();
				JSONObject obj = new JSONObject();
				CloseableIterator<Document> iter = list.iteratorThrow();
				while (iter.hasNext()) {
					Document doc = iter.next();
					obj.append("documents", JSONUtil.getJSONObject(doc));
				}
				iter.close();
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
