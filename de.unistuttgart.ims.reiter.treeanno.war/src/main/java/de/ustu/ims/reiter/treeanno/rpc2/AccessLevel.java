package de.ustu.ims.reiter.treeanno.rpc2;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.json.JSONArray;
import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CA;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.User;

@Path("AccessLevel")
public class AccessLevel {
	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@GET
	public JSONObject doGet(@QueryParam("projectId") int projectId) throws Exception {

		User user = (User) request.getSession().getAttribute(CA.USER);

		Project proj = CW.getDataLayer(context).getProject(projectId);

		JSONObject json = new JSONObject();
		json.put("ProjectId", projectId);
		json.put("AccessLevel", CW.getDataLayer(context).getAccessLevel(proj, user));
		return json;
	}

	@GET
	@Path("all")
	public JSONArray getAllForUser(@QueryParam("userId") int userId) throws Exception {

		DataLayer dl = CW.getDataLayer(context);
		User user;
		if (userId == -1)
			user = CW.getUser(request);
		else
			user = dl.getUser(userId);

		JSONArray arr = new JSONArray();
		for (Project p : dl.getProjects()) {
			int l = dl.getAccessLevel(p, user);
			JSONObject json = new JSONObject();
			json.put("ProjectId", p.getId());
			json.put("AccessLevel", l);
			json.put("project", JSONUtil.getJSONObject(p));
			arr.put(json);
		}
		return arr;
	}

}
