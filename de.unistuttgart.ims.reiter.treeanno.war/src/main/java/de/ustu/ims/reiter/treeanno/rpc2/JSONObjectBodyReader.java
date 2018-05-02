package de.ustu.ims.reiter.treeanno.rpc2;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class JSONObjectBodyReader implements MessageBodyReader<JSONObject> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return (mediaType.getSubtype().equalsIgnoreCase("json")) && type == JSONObject.class;
	}

	@Override
	public JSONObject readFrom(Class<JSONObject> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		return new JSONObject(IOUtils.toString(entityStream, "UTF-8"));
	}

}
