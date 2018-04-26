package de.ustu.ims.reiter.treeanno.rpc2;

import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.json.JSONArray;

import de.ustu.ims.reiter.treeanno.CA;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;

@Path("/projectlist")
public class ProjectList {
	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@GET
	public String getProjectList() throws SQLException {
		Collection<Project> list;
			list = CW.getDataLayer(context).getProjects();
			User user = (User) request.getSession().getAttribute(CA.USER);

			JSONArray array = new JSONArray();
			for (Project project : list) {
				if (CW.getDataLayer(context).getAccessLevel(project, user) >= Perm.READ_ACCESS) {
					array.put(JSONUtil.getJSONObject(project));
				}
			}
			context.log(user.getId() + " requests project list.");
			return array.toString();
	}

}
