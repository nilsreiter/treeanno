package de.nilsreiter.web.rpc;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.impl.Alignment_impl;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentWriter;

/**
 * Servlet implementation class AlignDocumentSet
 */
public class AlignDocumentSet extends RPCServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject obj = new JSONObject();
		StringBuilder formula = new StringBuilder();
		for (String s : request.getParameterMap().keySet()) {
			for (String v : request.getParameterValues(s)) {
				obj.append(s, v);
				if (s.startsWith("weight_") && Double.valueOf(v) > 0) {
					formula.append(v).append(s.substring(38)).append(" + ");
				}
			}
		}
		obj.put("formula", formula.toString());
		// start the background process
		ThreadPoolExecutor service =
				(ThreadPoolExecutor) getServletContext().getAttribute(
						"EXECUTOR");
		AlignmentThread at =
				new AlignmentThread(request.getParameter("alignmenttitle"));
		at.setDocumentSet(getDocumentSet(request,
				request.getParameter("fileselector")));
		at.setAlgorithm(request.getParameter("algorithm"));
		at.setFuture(service.submit(at));
		at.setSettings(obj);

		((List<AlignmentThread>) getServletContext().getAttribute("futures"))
				.add(at);

		returnJSON(response, obj);
	}

	public class AlignmentThread implements Callable<Alignment<Event>> {

		Future<Alignment<Event>> future;
		JSONObject settings;
		String title;
		DocumentSet docSet;
		String algorithm;
		long start;

		public AlignmentThread(String title) {
			this.title = title;
		}

		public void setDocumentSet(DocumentSet ds) {
			docSet = ds;
		}

		@Override
		public Alignment<Event> call() throws Exception {
			start = System.currentTimeMillis();
			Thread.sleep(10000);
			Alignment<Event> alignment = new Alignment_impl<Event>(title);
			alignment.setTitle(title);
			alignment.addAlignment("al0", new HashSet<Event>());

			alignment.getDocuments().addAll(docSet.getSet());

			DBAlignmentWriter dbaw =
					new DBAlignmentWriter(new DBAlignment(docMan.getDatabase()));
			dbaw.write(alignment);
			docMan.alignmentInfo = null;
			return alignment;
		}

		public Future<Alignment<Event>> getFuture() {
			return future;
		}

		public void setFuture(Future<Alignment<Event>> future) {
			this.future = future;
		}

		public String getAlgorithm() {
			return algorithm;
		}

		public void setAlgorithm(String algorithm) {
			this.algorithm = algorithm;
		}

		public JSONObject getSettings() {
			return settings;
		}

		public void setSettings(JSONObject settings) {
			this.settings = settings;
		}

		public long getStart() {
			return start;
		}

	}
}
