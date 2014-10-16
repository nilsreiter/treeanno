package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;

import de.nilsreiter.util.StringUtil;
import de.nilsreiter.web.json.JSONConversion;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.Mention;
import de.uniheidelberg.cl.a10.data2.Sentence;
import de.uniheidelberg.cl.a10.data2.Token;

/**
 * Servlet implementation class GetDocument
 */
public class GetDocument extends RPCServlet {
	private static final long serialVersionUID = 1L;
	Map<Document, JSONObject> jsonCache = new HashMap<Document, JSONObject>();
	Map<Document, String> htmlCache = new HashMap<Document, String>();

	enum DocumentFormat {
		JSON, HTML
	};

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		DocumentFormat format =
				getArgument(request, DocumentFormat.class, "format",
						DocumentFormat.JSON);
		switch (format) {
		case HTML:
			this.doGetHTML(request, response);
			break;
		default:
		case JSON:
			this.doGetJSON(request, response);
			break;
		}

	}

	protected void doGetHTML(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Document document = this.getDocument(request);
		Div docDiv = new Div();
		// docDiv.setCSSClass(document.getId());

		Map<Token, Set<Event>> tokenEventMap = new HashMap<Token, Set<Event>>();
		for (Event event : document.getEvents()) {
			Token evoker = event.firstToken();
			if (!tokenEventMap.containsKey(evoker))
				tokenEventMap.put(evoker, new HashSet<Event>());
			tokenEventMap.get(evoker).add(event);
		}
		for (Sentence sentence : document.getSentences()) {
			Span sentenceSpan = new Span();
			sentenceSpan.setCSSClass(sentence.getId() + " sentence");
			int lastpos = 0;
			for (Token token : sentence.getTokens()) {
				Span tokenSpan = new Span();
				tokenSpan.setId(token.getGlobalId());
				Set<String> classes = new HashSet<String>();
				classes.add("token");
				classes.add(token.getId());
				for (Frame frame : token.getFrames()) {
					classes.add("frame");
					classes.add(frame.getFrameName());
				}
				for (Mention mention : token.getMentions()) {
					classes.add("mention");
					classes.add(mention.getId());
					classes.add(mention.getEntity().getId());
				}
				if (tokenEventMap.containsKey(token))
					for (Event event : tokenEventMap.get(token)) {
						classes.add("event");
						classes.add(event.getId());
						classes.add(event.getEventClass());
					}
				tokenSpan.setCSSClass(StringUtil.join(classes, " ", ""));
				tokenSpan.appendText(token.getSurface());
				if (token.getBegin() > lastpos) {
					sentenceSpan.appendText(" ");
				}
				sentenceSpan.appendChild(tokenSpan);
				lastpos = token.getEnd();
			}
			docDiv.appendChild(sentenceSpan);
		}
		returnHTML(response, docDiv.write());
	}

	protected void doGetJSON(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Document document = this.getDocument(request);

		if (jsonCache.containsKey(document))
			returnJSON(response, jsonCache.get(document));
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

		jsonCache.put(document, json);

		returnJSON(response, json);
	}
}
