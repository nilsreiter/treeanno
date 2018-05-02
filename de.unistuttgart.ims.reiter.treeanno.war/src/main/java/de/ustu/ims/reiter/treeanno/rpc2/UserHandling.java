package de.ustu.ims.reiter.treeanno.rpc2;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

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
}
