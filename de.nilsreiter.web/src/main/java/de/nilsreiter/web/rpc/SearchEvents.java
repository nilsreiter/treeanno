package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.json.JSONArray;
import org.json.JSONObject;

import de.nilsreiter.util.StringUtil;
import de.nilsreiter.web.json.JSONConversion;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.HasTokens;
import de.uniheidelberg.cl.a10.data2.Token;

/**
 * Servlet implementation class SearchEvents
 */
public class SearchEvents extends RPCServlet {
	public static final String CONSTRAINT_EVENT_CLASS = "eventclass";
	public static final String CONSTRAINT_EVENT_LEMMA = "eventlemma";
	public static final String CONSTRAINT_EVENT_SURFACE = "eventsurface";

	private static final long serialVersionUID = 1L;
	double xresolution = 10;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("doc") == null) {
			response.getWriter().print("");
			return;
		}
		System.err.println("Request in " + request.getCharacterEncoding());
		// System.err.println(request.getParameterMap());

		Query query = Query.getQuery(request.getParameterMap());

		int window = 10;
		try {
			window = Integer.valueOf(request.getParameter("windowsize"));
		} catch (NullPointerException e) {
			// do nothing
		} catch (NumberFormatException e) {
			// do nothing
		}

		Collection<? extends Document> documents = null;
		List<Event[]> events = new LinkedList<Event[]>();
		Map<Document, Map<Long, Set<Event>>> countMap =
				new HashMap<Document, Map<Long, Set<Event>>>();
		try {
			documents =
					docMan.getDocumentSetReader()
							.read(request.getParameter("doc")).getSet();
			for (Document document : documents) {
				Map<Long, Set<Event>> thisMap = new HashMap<Long, Set<Event>>();
				List<? extends Event> eventsInDocument = document.getEvents();

				for (int i = 0; i < eventsInDocument.size(); i++) {
					Event event = eventsInDocument.get(i);

					int si = 0;
					while (si < query.getLength() && query.matches(event, si)
							&& i + si < eventsInDocument.size() - 1) {
						si++;
						event = eventsInDocument.get(i + si);
					};
					if (si == query.getLength()) {
						Event[] hitArray =
								new Event[window * 2 + query.getLength()];
						int offset = i - window;
						for (int j = i - window; j <= i + window; j++) {
							if (j < 0)
								hitArray[j - offset] = null;
							else if (j >= eventsInDocument.size())
								hitArray[j - offset] = null;
							else
								hitArray[j - offset] = eventsInDocument.get(j);
						}
						events.add(hitArray);

						long perc =
								Math.round((event.indexOf() / (double) eventsInDocument
										.size()) * xresolution);
						if (!thisMap.containsKey(perc))
							thisMap.put(perc, new HashSet<Event>());
						thisMap.get(perc).add(event);
					}

				}
				countMap.put(document, thisMap);
			}
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject json = new JSONObject();

		for (Event[] hitWindow : events) {
			Document doc = hitWindow[window].getRitualDocument();
			int length = doc.getEvents().size();
			JSONObject hit = new JSONObject();
			JSONArray arr = new JSONArray();
			for (int i = 0; i < hitWindow.length; i++)
				arr.put(i, JSONConversion.getEvent(hitWindow[i]));

			hit.put("list", arr);
			hit.put("document", hitWindow[window].getRitualDocument().getId());
			hit.put("position", hitWindow[window].indexOf() / (double) length);
			json.append("hits", hit);
		}

		for (Document doc : countMap.keySet()) {
			JSONObject obj = new JSONObject();
			obj.put("name", doc.getId());

			for (long i = 0; i <= xresolution; i++) {
				if (countMap.get(doc).containsKey(i))
					obj.append("data", countMap.get(doc).get(i).size());
				else
					obj.append("data", 0);
			}
			json.append("chartdata", obj);
		}
		json.put("windowsize", window);
		json.put("querylength", query.getLength());
		returnJSON(response, json);
	}

	public static class Query {
		QueryItem[] items;
		int length = 0;

		public int getLength() {
			return length;
		}

		public static Query getQuery(Map<String, String[]> parameterMap) {
			Query q = new Query();
			String[] pos = parameterMap.get("position");
			q.length = parameterMap.get("eventclass").length;
			q.items = new QueryItem[q.length];
			String[] eventClass = parameterMap.get("eventclass");
			String[] eventSurface = parameterMap.get("eventsurface");
			String[] eventLemma = parameterMap.get("eventlemma");

			int eventRoleKeys = 0;
			for (String key : parameterMap.keySet()) {
				if (key.startsWith("eventRoleName")) {
					eventRoleKeys++;
				}
			}

			for (int i = 0; i < q.length; i++) {
				QueryItem qi = new QueryItem();

				if (eventClass[i].isEmpty())
					qi.setEventClass(null);
				else
					qi.setEventClass(eventClass[i]);
				if (eventSurface[i].isEmpty())
					qi.setEventSurface(null);
				else
					qi.setEventSurface(eventSurface[i]);
				if (eventLemma[i].isEmpty())
					qi.setEventLemma(null);
				else
					qi.setEventLemma(eventLemma[i]);

				// Event roles
				for (int j = 0; j < eventRoleKeys; j++) {
					try {
						if (!parameterMap.get("eventRoleName" + j)[i].isEmpty()
								&& !parameterMap.get("eventRoleFiller" + j)[i]
										.isEmpty())
							qi.addRoleConstraint(
									parameterMap.get("eventRoleName" + j)[i],
									parameterMap.get("eventRoleFiller" + j)[i]);
					} catch (NullPointerException e) {
						// do nothing
					}
				}

				// Event position
				qi.setPosition(Integer.valueOf(pos[i]));
				q.items[i] = qi;
			}

			Arrays.sort(q.items, new Comparator<QueryItem>() {

				@Override
				public int compare(QueryItem o1, QueryItem o2) {
					return Integer.compare(o1.getPosition(), o2.getPosition());
				}
			});

			Logger.getAnonymousLogger().info(StringUtil.join(q.items, ",", 0));

			return q;
		}

		public boolean matches(Event event, int position) {
			return items[position].matches(event);
		}
	}

	public static class QueryItem {
		Map<String, String> propertyConstraints = new HashMap<String, String>();
		Map<String, String> roleConstraints = null;
		int position;

		public boolean matches(Event event) {
			boolean m = true;
			Token token = event.firstToken();
			if (getEventClass() != null)
				m &= event.getEventClass().matches(getEventClass());
			if (getEventSurface() != null)
				m &= token.getSurface().matches(getEventSurface());
			if (getEventLemma() != null)
				m &= token.getLemma().matches(getEventLemma());
			if (roleConstraints != null) {
				for (String role : roleConstraints.keySet()) {
					m &= event.getArguments().containsKey(role);
					if (m) {
						boolean surfaceMatch = false;
						for (HasTokens shtoken : event.getArguments().get(role)) {
							for (Token stoken : shtoken)
								surfaceMatch |=
								stoken.getSurface().matches(
										roleConstraints.get(role));
						}

						m &= surfaceMatch;
					}
				}
			}
			return m;
		}

		public String getEventClass() {
			return propertyConstraints.get(CONSTRAINT_EVENT_CLASS);
		}

		public void setEventClass(String eventClass) {
			this.propertyConstraints.put(CONSTRAINT_EVENT_CLASS, eventClass);
		}

		public String getEventSurface() {
			return propertyConstraints.get(CONSTRAINT_EVENT_SURFACE);
		}

		public void setEventSurface(String eventSurface) {
			this.propertyConstraints
			.put(CONSTRAINT_EVENT_SURFACE, eventSurface);
		}

		public String getEventLemma() {
			return propertyConstraints.get(CONSTRAINT_EVENT_LEMMA);
		}

		public void setEventLemma(String eventLemma) {
			this.propertyConstraints.put(CONSTRAINT_EVENT_LEMMA, eventLemma);
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		@Override
		public String toString() {
			return this.propertyConstraints.toString()
					+ (roleConstraints != null ? this.roleConstraints
							.toString() : "");
		}

		public void addRoleConstraint(String role, String constraint) {
			if (this.roleConstraints == null)
				this.roleConstraints = new HashMap<String, String>();
			this.roleConstraints.put(role, constraint);
		}
	}
}
