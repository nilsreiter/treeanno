package de.ustu.ims.reiter.treeanno;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import de.ustu.ims.reiter.treeanno.io.DatabaseIO;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
public class ContextListener implements ServletContextListener {

	DatabaseIO dr;

	/**
	 * Default constructor. 
	 * @throws NamingException 
	 * @throws ClassNotFoundException 
	 */
	public ContextListener() {}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		Context envContext;
		try {
			envContext =
					(Context) new InitialContext().lookup("java:/comp/env");
			DataSource dataSource =
					(DataSource) envContext.lookup("jdbc/treeanno");
			sc.setAttribute("dataSource", dataSource);
		} catch (NamingException e1) {
			e1.printStackTrace();
		}

		try {
			dr = new DatabaseIO();
			sc.setAttribute("databaseReader", dr);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
