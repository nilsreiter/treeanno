package de.nilsreiter.web.rpc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import de.nilsreiter.web.json.JSONConversion;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.Sentence;
import de.uniheidelberg.cl.a10.data2.Token;

/**
 * Servlet implementation class GetDocument
 */
public class GetDocument extends RPCServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Document document = this.getDocument(request);

		JSONObject json = new JSONObject();
		json.put("id", document.getId());

		JSONObject tokens = new JSONObject();
		for (Token token : document.getTokens()) {
			tokens.put(token.getId(), JSONConversion.getToken(token));
		}
		json.put("tokens", tokens);

		JSONArray sentences = new JSONArray();
		for (Sentence sentence : document.getSentences()) {
			JSONObject sent =
					JSONConversion.getAnnotationObjectInDocument(sentence);
			for (Token tok : sentence.getTokens()) {
				sent.append("tl", tok.getId());
			}
			sentences.put(sent);
		}
		json.put("sentences", sentences);

		JSONObject frames = new JSONObject();
		for (Frame frame : document.getFrames()) {
			frames.put(frame.getId(), JSONConversion.getFrame(frame));
		}
		json.put("frames", frames);

		JSONObject events = new JSONObject();
		for (Event event : document.getEvents()) {
			events.put(event.getId(), JSONConversion.getEvent(event));
		}
		json.put("events", events);

		returnJSON(response, json);

	}
}
