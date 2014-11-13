package de.nilsreiter.web.rpc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.nilsreiter.alignment.algorithm.AlgorithmFactory;
import de.nilsreiter.alignment.algorithm.AlignmentAlgorithm;
import de.nilsreiter.util.db.Database;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignment;
import de.uniheidelberg.cl.a10.data2.alignment.io.DBAlignmentWriter;

/**
 * Servlet implementation class AlignDocumentSet
 */
public class AlignDocumentSet extends RPCServlet {
	Logger logger = LoggerFactory.getLogger(AlignDocumentSet.class);

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Configuration config = new BaseConfiguration();
		config.setProperty("alignment.algorithm",
				request.getParameter("algorithm"));
		String algName;
		try {
			algName =
					Class.forName(request.getParameter("algorithm"))
							.getSimpleName();
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
		config.setProperty(algName + ".threshold",
				request.getParameter("threshold"));
		config.setProperty(algName + ".combination",
				request.getParameter("combination"));
		config.setProperty("BayesianModelMerging.threaded", true);

		JSONObject obj = new JSONObject();
		StringBuilder formula = new StringBuilder();
		for (String s : request.getParameterMap().keySet()) {
			for (String v : request.getParameterValues(s)) {
				obj.append(s, v);
				if (s.startsWith("weight_") && Double.valueOf(v) > 0) {
					formula.append(v).append(s.substring(38)).append(" + ");
					config.addProperty(algName + ".similarityFunctions",
							s.substring(7));
					config.addProperty(algName + ".weights", Double.valueOf(v));
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

		logger.debug("Alignment algorithm configuration\n {}",
				ConfigurationUtils.toString(config));
		at.setConfiguration(config);
		try {
			at.init(docMan.getDatabase());
			at.setFuture(service.submit(at));
			at.setSettings(obj);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		((List<AlignmentThread>) getServletContext().getAttribute("futures"))
		.add(at);

		returnJSON(response, obj);
	}

	public class AlignmentThread implements Callable<Alignment<Event>> {

		Future<Alignment<Event>> future;
		JSONObject settings;
		String title;
		DocumentSet docSet;
		long start;
		Configuration configuration;
		AlignmentAlgorithm<Event> algorithm;
		Logger logger = LoggerFactory.getLogger(AlignmentThread.class);

		public AlignmentThread(String title) {
			this.title = title;
		}

		public void setDocumentSet(DocumentSet ds) {
			docSet = ds;
		}

		public void init(Database dataSource) throws ClassNotFoundException,
		FileNotFoundException, SecurityException,
		InstantiationException, IllegalAccessException {
			AlgorithmFactory factory = new AlgorithmFactory();
			algorithm = factory.getAlgorithm(dataSource, getConfiguration());

		}

		@Override
		public Alignment<Event> call() throws Exception {
			logger.info("Starting alignment thread.");
			start = System.currentTimeMillis();
			// Thread.sleep(100000);
			Alignment<Event> alignment = null;
			for (int i = 0; i < docSet.size(); i++) {
				for (int j = i + 1; j < docSet.size(); j++) {
					logger.debug("Aligning documents {} and {}.", docSet
							.getSet().get(i).getId(), docSet.getSet().get(j)
							.getId());
					alignment =
							algorithm.align(title, docSet.getSet().get(i)
									.getEvents(), docSet.getSet().get(j)
									.getEvents());
					alignment.setTitle(title);

					logger.debug("Storing alignment to database.");
					DBAlignmentWriter dbaw =
							new DBAlignmentWriter(new DBAlignment(
									docMan.getDatabase()));
					dbaw.write(alignment);
					docMan.alignmentInfo = null;

				}
			}
			return alignment;
		}

		public Future<Alignment<Event>> getFuture() {
			return future;
		}

		public void setFuture(Future<Alignment<Event>> future) {
			this.future = future;
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

		public Configuration getConfiguration() {
			return configuration;
		}

		public void setConfiguration(Configuration configuration) {
			this.configuration = configuration;
		}

	}
}
