package com.ibm.ws.microprofile.sample.conference.vote.api;

import static com.ibm.ws.microprofile.sample.conference.vote.utils.Debug.isDebugEnabled;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendeeProvider implements MessageBodyReader<Attendee>, MessageBodyWriter<Attendee> {

	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		
		if (isDebugEnabled()) System.out.println("AP.isReadable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(Attendee.class));
		return clazz.equals(Attendee.class);
	}

	@Override
	public Attendee readFrom(Class<Attendee> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream is) throws IOException, WebApplicationException {
		JsonReader rdr = null; 
		try {
		     rdr = Json.createReader(is);
		     JsonObject attendeeJson = rdr.readObject();
		     JsonNumber idJson = attendeeJson.getJsonNumber("id");
		     JsonString nameJson = attendeeJson.getJsonString("name");
		     Attendee attendee = new Attendee(idJson.longValue(), nameJson.getString());
		     return attendee;
		} finally {
			if (rdr != null) {
				rdr.close();
			}
		}
		
	}
	
	@Override
	public long getSize(Attendee attendee, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("AP.getSize() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType);
		return 0;
	}

	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("AP.isWriteable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(Attendee.class));
		return clazz.equals(Attendee.class);
	}

	@Override
	public void writeTo(Attendee attendee, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> map, OutputStream os) throws IOException, WebApplicationException {
		JsonGenerator jsonGenerator = Json.createGenerator(os);
		jsonGenerator.writeStartObject();
		jsonGenerator.write("id", attendee.getId());
		jsonGenerator.write("name", attendee.getName());
		jsonGenerator.writeEnd();
		jsonGenerator.close();
	}
}
