package de.ustu.ims.reiter.treeanno.rpc2;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.json.JSONObject;

@Singleton
@Produces({ MediaType.APPLICATION_JSON })
@Provider
public class JSONObjectBodyWriter implements MessageBodyWriter<JSONObject> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return (mediaType.getSubtype().equalsIgnoreCase("json")) && JSONObject.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(JSONObject t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(JSONObject t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
		entityStream.write(t.toString().getBytes());
	}

}
