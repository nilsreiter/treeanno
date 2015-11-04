package de.ustu.ims.reiter.treeanno;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;

public class JSONUtil {
	public static JSONObject getJSONObject(UserDocument ud) {
		JSONObject json = new JSONObject();
		json.put(UserDocument.FIELD_ID, ud.getId());
		json.put(UserDocument.FIELD_MODIFICATION_DATE, ud.getModificationDate());
		json.put(UserDocument.FIELD_USER, new JSONObject(ud.getUser()));
		json.put(UserDocument.FIELD_SRC_DOCUMENT,
				getJSONObject(ud.getDocument()));
		return json;
	}

	public static JSONObject getJSONObject(Document document) {
		JSONObject json = new JSONObject();
		json.put("id", document.getId());
		json.put("name", document.getName());
		json.put("project", JSONUtil.getJSONObject(document.getProject()));
		return json;
	}

	public static JSONObject getJSONObject(Project project) {
		JSONObject json = new JSONObject();
		json.put("id", project.getId());
		json.put("name", project.getName());
		json.put("type", project.getType());
		return json;
	}
}
