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
			classes.add(AccessLevel.class);
			classes.add(Configuration.class);
			classes.add(ProjectList.class);
			classes.add(DocumentDelete.class);
			classes.add(DocumentExport.class);
			classes.add(DocumentList.class);
			classes.add(DocumentMeta.class);
			classes.add(NewDocument.class);
			classes.add(JSONObjectBodyReader.class);
			classes.add(JSONObjectBodyWriter.class);
			classes.add(JSONArrayBodyWriter.class);
			classes.add(UserDocumentList.class);
			classes.add(DocumentContentHandling.class);
		}

		return classes;
	}
}
