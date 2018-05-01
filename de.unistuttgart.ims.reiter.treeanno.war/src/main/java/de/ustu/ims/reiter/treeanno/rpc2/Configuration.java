package de.ustu.ims.reiter.treeanno.rpc2;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.commons.configuration2.ConfigurationMap;
import org.json.JSONObject;

import de.ustu.ims.reiter.treeanno.Perm;
import de.ustu.ims.reiter.treeanno.ProjectType;

@Path("config")
public class Configuration {
	@javax.ws.rs.core.Context
	ServletContext context;

	@javax.ws.rs.core.Context
	HttpServletRequest request;

	@GET
	public String doGet() throws ServletException, IOException {
		ConfigurationMap cnf = (ConfigurationMap) context.getAttribute("conf");

		JSONObject o = new JSONObject(cnf);
		StringBuilder b = new StringBuilder();
		b.append("var configuration = ");
		b.append(o.toString());
		b.append(";\n");

		b.append("var Perm = ");
		b.append(new JSONObject(new Perm()).toString());
		b.append(";\n");

		b.append("var ProjectType = ");
		b.append(new JSONObject(new ProjectType()).toString());
		b.append(";\n");

		return b.toString();

	}
}
