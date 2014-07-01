package de.nilsreiter.web.json;

import org.json.JSONObject;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Token;

public class JSONDocumentWriter extends JSONConverter<Document> {
	public JSONObject getJSON(Document d) {
		JSONObject json = new JSONObject();
		for (Token token : d.getTokens()) {
			json.append("tokens", getJSON(token));
		}
		for (Event event : d.getEvents()) {
			json.append("events", getJSON(event));
		}
		return json;
	}

	protected JSONObject getJSON(Token token) {
		JSONObject json = new JSONObject();
		json.put("id", token.getId());
		json.put("globalid", token.getGlobalId());
		json.put("surface", token.getSurface());
		return json;
	}

	protected JSONObject getJSON(Event event) {
		JSONObject json = new JSONObject();
		json.put("id", event.getId());
		for (Token token : event.getTokens()) {
			json.append("tokens", token.getId());
		}
		return json;
	}
}
