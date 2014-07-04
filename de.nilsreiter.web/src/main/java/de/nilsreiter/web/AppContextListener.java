package de.nilsreiter.web;

import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.nilsreiter.event.similarity.SimilarityDatabase;
import de.nilsreiter.event.similarity.impl.SimilarityDatabase_impl;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.DatabaseConfiguration;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public class AppContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();

		if (sc.getInitParameter("development").equalsIgnoreCase("true")) {
			sc.setAttribute("database", new SimilarityDatabase<Event>() {
				Random random = new Random();

				@Override
				public double getSimilarity(
						Class<? extends SimilarityFunction> simType, Event e1,
						Event e2) throws SQLException {
					return random.nextDouble();
				}
			});
		} else {
			DatabaseConfiguration dbConfig = new DatabaseConfiguration(
					sc.getInitParameter("dbhost"),
					sc.getInitParameter("dbname"),
					sc.getInitParameter("dbuser"),
					sc.getInitParameter("dbpassword"));

			try {
				SimilarityDatabase_impl<Event> database = new SimilarityDatabase_impl<Event>(
						dbConfig, sc.getInitParameter("dbidentifier"));
				sc.setAttribute("database", database);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sc.setAttribute("docman", new ServletDocumentManager());
		sc.setAttribute("devel", sc.getInitParameter("development"));

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			if (!sce.getServletContext().getAttribute("devel").equals("true"))
				if (sce.getServletContext().getAttribute("database") != null)
					((Database) sce.getServletContext()
							.getAttribute("database")).disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
