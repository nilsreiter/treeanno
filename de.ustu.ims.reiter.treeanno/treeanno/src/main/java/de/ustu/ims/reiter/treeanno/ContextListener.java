package de.ustu.ims.reiter.treeanno;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.ustu.ims.reiter.treeanno.io.DatabaseReader;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
public class ContextListener implements ServletContextListener {

	DatabaseReader dr;

	/**
	 * Default constructor. 
	 * @throws NamingException 
	 * @throws ClassNotFoundException 
	 */
	public ContextListener() {}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();

		try {
			dr = new DatabaseReader();
			sc.setAttribute("databaseReader", dr);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
