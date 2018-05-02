package de.ustu.ims.reiter.treeanno.rpc2;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

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

@Path("/{projectId}")
public class DocumentList {

	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@GET
	public JSONObject getDocumentList(@PathParam("projectId") int projectId) throws SQLException {
		User user = CW.getUser(request);
		Project proj = CW.getDataLayer(context).getProject(projectId);
		if (proj == null)
			throw new ForbiddenException();

		if (CW.getDataLayer(context).getAccessLevel(proj, user) >= Perm.READ_ACCESS) {
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
					CW.getDataLayer(context).getAccessLevel(proj, (User) request.getSession().getAttribute(CA.USER)));
			context.log(user.getId() + " requests document list.");

			return obj;
		} else {
			context.log(user.getId() + " was denied document list for project " + proj.getId());
			throw new ForbiddenException();
		}

	}

}
