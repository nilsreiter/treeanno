package de.ustu.creta.annotation.webtool;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.ustu.creta.annotation.webtool.beans.TextDocument;

public class AppContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		sc.setAttribute("documents", new HashMap<String, TextDocument>());

	}

}
