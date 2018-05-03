package de.ustu.ims.reiter.treeanno.rpc2;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/rpc")
public class RPC extends ResourceConfig {

	public RPC() {
		packages("de.ustu.ims.reiter.treeanno.rpc2");
		register(MultiPartFeature.class);
	}

}
