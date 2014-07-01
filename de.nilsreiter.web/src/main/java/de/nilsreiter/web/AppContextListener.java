package de.nilsreiter.web;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.nilsreiter.event.similarity.SimilarityDatabase;
import de.nilsreiter.util.db.Database;
import de.nilsreiter.util.db.DatabaseConfiguration;
import de.uniheidelberg.cl.a10.data2.Event;

public class AppContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();

		DatabaseConfiguration dbConfig = new DatabaseConfiguration(
				sc.getInitParameter("dbhost"), sc.getInitParameter("dbname"),
				sc.getInitParameter("dbuser"),
				sc.getInitParameter("dbpassword"));

		try {
			SimilarityDatabase<Event> database = new SimilarityDatabase<Event>(
					dbConfig, sc.getInitParameter("dbidentifier"));
			sc.setAttribute("database", database);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sc.setAttribute("docman", new ServletDocumentManager());
		sc.setAttribute("devel", sc.getInitParameter("development"));

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			if (sce.getServletContext().getAttribute("database") != null)
				((Database) sce.getServletContext().getAttribute("database"))
						.disconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

}
