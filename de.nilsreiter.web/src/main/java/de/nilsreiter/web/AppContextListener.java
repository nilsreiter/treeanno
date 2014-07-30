package de.nilsreiter.web;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import de.nilsreiter.event.similarity.SimilarityDatabase;
import de.nilsreiter.event.similarity.SimilarityProvider;
import de.nilsreiter.event.similarity.impl.SimilarityDatabase_impl;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.DatabaseConfiguration;
import de.nilsreiter.util.db.impl.DatabaseDBConfiguration_impl;
import de.nilsreiter.util.db.impl.DatabaseDataSource_impl;
import de.nilsreiter.web.rpc.AlignDocumentSet;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.RandomMatrix;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class AppContextListener implements ServletContextListener {
	private ExecutorService executor;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();

		try {

			Database database = null;

			if (sc.getInitParameter("development").equalsIgnoreCase("true")) {
				database =
						new DatabaseDBConfiguration_impl(
								DatabaseConfiguration.getLocalConfiguration());
				SimilarityProvider<Event> sde =
						new SimilarityProvider<Event>() {
							Random random = new Random();

							@Override
							public
									double
									getSimilarity(
											Class<? extends SimilarityFunction<Event>> simType,
											Event e1, Event e2)
											throws SQLException {
								return random.nextDouble();
							}

							@Override
							public
									Map<String, Matrix<Event, Event, Double>>
									getSimilarities(Document doc1, Document doc2)
											throws SQLException {

								return new RandomMap();
							}
						};
				sc.setAttribute("simdatabase", sde);
			} else {

				Context initContext;
				Class.forName("com.mysql.jdbc.Driver");

				initContext = new InitialContext();
				Context envContext =
						(Context) initContext.lookup("java:/comp/env");
				DataSource datasource =
						(DataSource) envContext.lookup("jdbc/a10");

				database = new DatabaseDataSource_impl(datasource);
				SimilarityDatabase<Event> simdatabase =
						new SimilarityDatabase_impl<Event>(database);
				sc.setAttribute("simdatabase", simdatabase);
			}
			ServletDocumentManager sdm = new ServletDocumentManager();
			sdm.setDatabase(database);
			sc.setAttribute("docman", sdm);
			sc.setAttribute("devel", sc.getInitParameter("development"));

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ThreadFactory daemonFactory =
				new BasicThreadFactory.Builder()
						.namingPattern("workerthread-%d").daemon(true)
						.priority(Thread.NORM_PRIORITY).build();

		executor =
				Executors.newFixedThreadPool(Runtime.getRuntime()
						.availableProcessors(), daemonFactory);
		sc.setAttribute("EXECUTOR", executor);
		sc.setAttribute("futures",
				new LinkedList<AlignDocumentSet.AlignmentThread>());

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			if (!sce.getServletContext().getAttribute("devel").equals("true"))
				if (sce.getServletContext().getAttribute("database") != null)
					((DatabaseDBConfiguration_impl) sce.getServletContext()
							.getAttribute("database")).disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ServletContext context = sce.getServletContext();
		((ExecutorService) context.getAttribute("EXECUTOR")).shutdownNow();

	}

	public static class RandomMap implements
	Map<String, Matrix<Event, Event, Double>> {

		Random random = new Random();

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean containsKey(Object key) {
			return true;
		}

		@Override
		public boolean containsValue(Object value) {
			return true;
		}

		@Override
		public Matrix<Event, Event, Double> get(Object key) {
			return new RandomMatrix<Event, Event>(random);
		}

		@Override
		public Matrix<Event, Event, Double> put(String key,
				Matrix<Event, Event, Double> value) {
			return null;
		}

		@Override
		public Matrix<Event, Event, Double> remove(Object key) {
			return null;
		}

		@Override
		public
				void
		putAll(Map<? extends String, ? extends Matrix<Event, Event, Double>> m) {

		}

		@Override
		public void clear() {

		}

		@Override
		public Set<String> keySet() {
			return null;
		}

		@Override
		public Collection<Matrix<Event, Event, Double>> values() {
			return null;
		}

		@Override
		public Set<java.util.Map.Entry<String, Matrix<Event, Event, Double>>>
		entrySet() {
			return null;
		}
	}
}
