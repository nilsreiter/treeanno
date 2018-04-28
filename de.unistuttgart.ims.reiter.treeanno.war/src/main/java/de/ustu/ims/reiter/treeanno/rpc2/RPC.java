package de.ustu.ims.reiter.treeanno.rpc2;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rpc")
public class RPC extends Application {

	Set<Class<?>> classes = null;

	@Override
	public Set<Class<?>> getClasses() {
		if (classes == null) {
			classes = new HashSet<Class<?>>(super.getClasses());
			classes.add(ProjectList.class);
			classes.add(DocumentList.class);
			classes.add(JSONObjectBodyWriter.class);
			classes.add(JSONArrayBodyWriter.class);
		}

		return classes;
	}
}
