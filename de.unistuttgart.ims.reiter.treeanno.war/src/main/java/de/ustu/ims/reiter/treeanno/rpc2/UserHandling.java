package de.ustu.ims.reiter.treeanno.rpc2;

import java.net.URI;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.CA;
import de.ustu.ims.reiter.treeanno.CW;
import de.ustu.ims.reiter.treeanno.DataLayer;
import de.ustu.ims.reiter.treeanno.JSONUtil;
import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.beans.User;

@Path("user")
public class UserHandling {

	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@Path("login")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public Response login(@FormParam("username") String username, @FormParam("password") String password)
			throws Exception {
		if (check(username, password)) {
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(0);
			User user = null;
			DataLayer dl = CW.getDataLayer(context);
			try {
				user = dl.getUser(Integer.valueOf(username));
			} catch (NumberFormatException | SQLException e) {
				context.log(e.getMessage(), e);
				throw new ServletException(e);
			}
			if (user != null) {
				session.setAttribute(CA.USER, user);
				context.log("User " + user.getId() + " logged in.");
				return Response.temporaryRedirect(new URI("../projects.jsp")).build();
			}

		}
		throw new ForbiddenException();
	}

	@Path("assign")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public JSONObject assign(JSONObject object) throws Exception {
		DataLayer dataLayer = CW.getDataLayer(context);
		User activeUser = CW.getUser(request);
		if (!activeUser.isAdmin())
			throw new ForbiddenException();
		int targetUserId = object.getInt("user");
		User targetUser = dataLayer.getUser(targetUserId);

		int maxLevel = 0;
		JSONArray levels = object.getJSONArray("levels");
		for (int i = 0; i < levels.length(); i++) {
			JSONObject jObj = levels.getJSONObject(i);
			int projectId = jObj.getInt("project");
			int level = jObj.getInt("level");
			dataLayer.setAccessLevel(dataLayer.getProject(projectId), targetUser, level);
			maxLevel = Math.max(maxLevel, level);
		}

		targetUser.setAdmin(maxLevel >= Perm.ADMIN_ACCESS);
		dataLayer.updateUser(targetUser);

		return new JSONObject();
	}

	@Path("create")
	@Consumes(MediaType.APPLICATION_JSON)
	@POST
	public JSONObject createUser(JSONObject object) throws Exception {
		DataLayer dataLayer = CW.getDataLayer(context);
		User activeUser = CW.getUser(request);
		int accessLevel = dataLayer.getAccessLevel(null, activeUser);
		if (accessLevel < Perm.PADMIN_ACCESS)
			throw new ForbiddenException();

		User user = new User();
		user.setName(object.getString("name"));
		user.setEmail(object.getString("email"));
		user.setLanguage(object.optString("language", "en"));
		user = dataLayer.createNewUser(user);

		return JSONUtil.getJSONObject(user);
	}

	@Path("list")
	@GET
	public JSONArray getList() throws Exception {
		DataLayer dataLayer = CW.getDataLayer(context);
		User activeUser = CW.getUser(request);
		int accessLevel = dataLayer.getAccessLevel(null, activeUser);
		if (accessLevel < Perm.PADMIN_ACCESS)
			throw new ForbiddenException();

		JSONArray array = new JSONArray();
		for (User u : dataLayer.getUserList())
			array.put(JSONUtil.getJSONObject(u));
		return array;

	}

	protected boolean check(String username, String password) {
		return true;
	}
}
