package de.ustu.ims.reiter.treeanno;

import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.beans.Document;
import de.ustu.ims.reiter.treeanno.beans.UserDocument;

public class JSONUtil {
	public static JSONObject getJSONObject(UserDocument ud) {
		JSONObject json = new JSONObject();
		json.put(UserDocument.FIELD_ID, ud.getId());
		json.put(UserDocument.FIELD_MODIFICATION_DATE, ud.getModificationDate());
		json.put(UserDocument.FIELD_USER, new JSONObject(ud.getUser()));
		json.put(UserDocument.FIELD_SRC_DOCUMENT, ud.getDocument()
				.getDatabaseId());
		json.put("project", new JSONObject(ud.getDocument().getProject()));
		return json;
	}

	public static JSONObject getJSONObject(Document document) {
		JSONObject json = new JSONObject();
		json.put("id", document.getId());
		json.put("name", document.getName());
		json.put("project", document.getProject().getDatabaseId());
		return json;
	}
}
