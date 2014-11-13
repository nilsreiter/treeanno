package de.nilsreiter.web.json;

import org.json.JSONObject;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Sentence;
import de.uniheidelberg.cl.a10.data2.Token;

public class JSONConversion {
	public static JSONObject getToken(Token token) {
		JSONObject json = getAnnotationObjectInDocument(token);
		// json.put("globalid", token.getGlobalId());
		json.put("surface", token.getSurface());
		json.put("begin", token.getBegin());
		json.put("end", token.getEnd());
		for (Mention m : token.getMentions()) {
			json.append("mentionIds", m.getId());
			json.append("entityIds", m.getEntity().getId());
		}
		for (Frame frame : token.getFrames()) {
			json.append("frames", frame.getId());
		}
		return json;
	}

	public static JSONObject getEvent(Event event) {
		if (event == null) return new JSONObject();
		JSONObject eventObject = getAnnotationObjectInDocument(event);
		eventObject.put("class", event.getEventClass());
		eventObject.put("anchorId", event.firstToken().getId());
		eventObject.put("position", (double) event.indexOf()
				/ event.getRitualDocument().getEvents().size());
		eventObject.put("sentence", getSentence(event.firstToken()
				.getSentence()));
		eventObject.put("documentId", event.getRitualDocument().getId());
		for (Token token : event.getTokens()) {
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
		JSONObject sentenceObject = getAnnotationObjectInDocument(sentence);
		for (Token token : sentence) {
			sentenceObject.append("tokens", JSONConversion.getToken(token));
		}
		return sentenceObject;
	}

	public static JSONObject getFrame(Frame frame) {
		JSONObject obj = getAnnotationObjectInDocument(frame);
		obj.put("name", frame.getFrameName());
		for (Token token : frame.getTokens()) {
			obj.append("target", token.getId());
			obj.append("tl", token.getId());
		}
		for (FrameElement fe : frame.getFrameElms()) {
			obj.append("fes", getFrameElement(fe));
		}
		return obj;
	}

	public static JSONObject getFrameElement(FrameElement frameElement) {
		JSONObject obj = getAnnotationObjectInDocument(frameElement);

		return obj;
	}

	public static JSONObject getAnnotationObjectInDocument(
			AnnotationObjectInDocument aoi) {
		JSONObject obj = new JSONObject();
		obj.put("id", aoi.getId());
		return obj;
	}
}
