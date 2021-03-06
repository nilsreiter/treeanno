package de.ustu.ims.reiter.treeanno;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.ConfigurationMap;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.OverrideCombiner;
import org.apache.commons.io.IOUtils;

import de.ustu.ims.reiter.treeanno.io.DatabaseIO;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
public class ContextListener implements ServletContextListener {

	static final String dataSourceName = "treeanno/jdbc";

	DatabaseIO dr;

	/**
	 * Default constructor.
	 * 
	 * @throws NamingException
	 * @throws ClassNotFoundException
	 */
	public ContextListener() {
	}

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
		Context envContext = null;
		DataSource dataSource = null;
		try {
			envContext = (Context) new InitialContext().lookup("java:/comp/env");

			dataSource = (DataSource) envContext.lookup(dataSourceName);

			try {
				sc.log("Using data source url: " + dataSource.getConnection().getMetaData().getURL());
				dataSource.getConnection();
			} catch (SQLException e) {
				try {
					Class.forName("org.h2.Driver");
					Class.forName("org.sqlite.JDBC");

					System.err.println("Falling back to data source " + dataSourceName);
					dataSource = (DataSource) envContext.lookup(dataSourceName);

					dataSource.getConnection();
					sc.setAttribute("dataSource", dataSource);

				} catch (SQLException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}

			}

			try {
				Object o = sc.getInitParameter("dbInMemory");// envContext.lookup("dbInMemory");
				sc.setAttribute("dbInMemory", o);
			} catch (Exception e2) {
				e2.printStackTrace();
				// fail silently
			}
		} catch (NamingException e1) {
			e1.printStackTrace();
		}

		PropertiesConfiguration defaultConfig = new PropertiesConfiguration();
		PropertiesConfiguration serverConfig = new PropertiesConfiguration();

		InputStream is = null;
		try {
			// reading of default properties from inside the war
			is = getClass().getResourceAsStream("/project.properties");
			if (is != null) {
				defaultConfig.read(new InputStreamReader(is, "UTF-8"));
				// defaults.load();
			}
		} catch (IOException | ConfigurationException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}

		try {
			// reading additional properties in separate file, as specified
			// in the context
			if (envContext != null) {
				String path = (String) envContext.lookup("treeanno/configurationPath");
				is = new FileInputStream(new File(path));
				serverConfig.read(new InputStreamReader(is, "UTF-8"));
			}
		} catch (NameNotFoundException e) {
			sc.log("configuration path not defined, using default properties.");
		} catch (IOException | NamingException | ConfigurationException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}

		CombinedConfiguration config = new CombinedConfiguration(new OverrideCombiner());
		config.addConfiguration(serverConfig);
		config.addConfiguration(defaultConfig);

		sc.setAttribute("conf", new ConfigurationMap(config));

		sc.setAttribute("treeanno.name", config.getString("treeanno.name"));
		sc.setAttribute("treeanno.version", config.getString("treeanno.version"));
		sc.setAttribute("dsName", dataSourceName);

		try {
			CW.setDataLayer(sc, new DatabaseIO(dataSource));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sc.getSessionCookieConfig().setPath("/");
	}
}
