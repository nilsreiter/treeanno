package de.nilsreiter.web.json;

import org.json.JSONObject;

import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Sentence;
import de.uniheidelberg.cl.a10.data2.Token;

public class JSONConversion {
	public static JSONObject getToken(Token token) {
		JSONObject json = new JSONObject();
		json.put("id", token.getId());
		// json.put("globalid", token.getGlobalId());
		json.put("surface", token.getSurface());
		for (Mention m : token.getMentions()) {
			json.append("mentionIds", m.getId());
			json.append("entityIds", m.getEntity().getId());
		}
		return json;
	}

	public static JSONObject getEvent(Event event) {
		JSONObject eventObject = new JSONObject();
		if (event == null) return eventObject;
		eventObject.put("id", event.getId());
		eventObject.put("class", event.getEventClass());
		eventObject.put("anchorId", event.firstToken().getId());
		eventObject.put("position", (double) event.indexOf()
				/ event.getRitualDocument().getEvents().size());
		eventObject.put("sentence", getSentence(event.firstToken()
				.getSentence()));
		for (Token token : event) {
			eventObject.append("token", JSONConversion.getToken(token));
		}
		for (String role : event.getArguments().keySet()) {
			JSONObject roleObject = new JSONObject();
			roleObject.put("name", role);
			for (HasTokens ht : event.getArguments().get(role)) {
				for (Token token : ht) {
					roleObject.append("tokens", JSONConversion.getToken(token));
				}
			}
			eventObject.append("roles", roleObject);
		}
		return eventObject;
	}

	public static JSONObject getSentence(Sentence sentence) {
		JSONObject sentenceObject = new JSONObject();
		sentenceObject.put("id", sentence.getId());
		for (Token token : sentence) {
			sentenceObject.append("tokens", JSONConversion.getToken(token));
		}
		return sentenceObject;
	}

}
