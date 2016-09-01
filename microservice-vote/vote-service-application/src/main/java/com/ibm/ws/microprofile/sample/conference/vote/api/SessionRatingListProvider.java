package com.ibm.ws.microprofile.sample.conference.vote.api;

import static com.ibm.ws.microprofile.sample.conference.vote.utils.Debug.isDebugEnabled;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonWriter;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.ibm.ws.microprofile.sample.conference.vote.model.SessionRating;
import com.ibm.ws.microprofile.sample.conference.vote.utils.TeeOutputStream;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionRatingListProvider implements MessageBodyReader<List<SessionRating>>, MessageBodyWriter<List<SessionRating>> {

	
	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("SRLP.isReadable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(SessionRating.class));
		return List.class.equals(clazz);
	}

	@Override
	public List<SessionRating> readFrom(Class<List<SessionRating>> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream is) throws IOException, WebApplicationException {
		JsonReader rdr = null; 
		try {
			List<SessionRating> ratings = new ArrayList<SessionRating>();
			rdr = Json.createReader(is);
			JsonArray arr = rdr.readArray();
			for (int i = 0 ; i <arr.size(); i++) {
				JsonObject sessionRatingJson = arr.getJsonObject(i);//rdr.readObject();
				if (isDebugEnabled()) System.out.println(sessionRatingJson);
				JsonNumber idJson = sessionRatingJson.getJsonNumber("id");
				JsonString sessionJson = sessionRatingJson.getJsonString("session");
				JsonNumber attendeeIdJson = sessionRatingJson.getJsonNumber("attendeeId");
				int rating = sessionRatingJson.getInt("rating");
				SessionRating sessionRating;
				if (idJson != null) {
					sessionRating = new SessionRating(idJson.longValue(), sessionJson.getString(), attendeeIdJson.longValue(), rating);
				} else {
					sessionRating = new SessionRating(sessionJson.getString(), attendeeIdJson.longValue(), rating);
				}
				ratings.add(sessionRating);
			}
			return ratings;
		} finally {
			if (rdr != null) {
				rdr.close();
			}
		}

	}

	@Override
	public long getSize(List<SessionRating> sessionRating, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("SRLP.getSize() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType);
		return 0;
	}

	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		boolean isWriteable = List.class.isAssignableFrom(clazz);
		if (isDebugEnabled()) System.out.println("SRLP.isWriteable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + isWriteable);
		return isWriteable;
	}

	@Override
	public void writeTo(List<SessionRating> sessionRatings, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> map, OutputStream os) throws IOException, WebApplicationException {
		
//		JsonGenerator jsonGenerator = Json.createGenerator(new TeeOutputStream(os, System.out));
//		jsonGenerator.writeStartArray();
//		for (SessionRating sessionRating : sessionRatings) {
//			jsonGenerator.writeStartObject()
//				.write("id", sessionRating.getId())
//				.write("session", sessionRating.getSession())
//				.write("attendeeId", sessionRating.getAttendeeId())
//				.write("rating", sessionRating.getRating())
//			.writeEnd();
//		}
//		jsonGenerator.writeEnd();
//		jsonGenerator.flush();
//		jsonGenerator.close();
		
		JsonWriter writer = Json.createWriter(new TeeOutputStream(os, System.out));
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for (SessionRating sessionRating : sessionRatings) {
			JsonObject sessionRatingJson = Json.createObjectBuilder()
					.add("id", sessionRating.getId())
				    .add("session", sessionRating.getSession())
				    .add("attendeeId", sessionRating.getAttendeeId())
				    .add("rating", sessionRating.getRating())
				    .build();
			arrayBuilder.add(sessionRatingJson);
		}
		writer.writeArray(arrayBuilder.build());
		writer.close();
	}
}
