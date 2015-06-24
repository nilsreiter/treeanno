package de.ustu.ims.reiter.treeanno;

import java.util.List;

import javax.servlet.ServletContext;

import de.ustu.ims.reiter.treeanno.beans.Project;
import de.ustu.ims.reiter.treeanno.io.DatabaseIO;

public class CW {
	public static DatabaseIO getDatabaseIO(ServletContext sc) {
		return (DatabaseIO) sc.getAttribute(CA.DATABASEIO);
	}

	@SuppressWarnings("unchecked")
	public static List<Project> getProjectList(ServletContext sc) {
		return (List<Project>) sc.getAttribute(CA.PROJECTLIST);
	}

	public static DataLayer getDataLayer(ServletContext sc) {
		return (DataLayer) sc.getAttribute(CA.DATALAYER);
	}

	public static void setDataLayer(ServletContext sc, DataLayer layer) {
		sc.setAttribute(CA.DATALAYER, layer);
	}

}
