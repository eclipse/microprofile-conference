/*
 * (C) Copyright IBM Corporation 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import javax.json.JsonObjectBuilder;
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

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionRatingProvider implements MessageBodyReader<SessionRating>, MessageBodyWriter<SessionRating> {

	
	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("AP.isReadable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(SessionRating.class));
		return clazz.equals(SessionRating.class);
	}

	@Override
	public SessionRating readFrom(Class<SessionRating> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream is) throws IOException, WebApplicationException {
		return fromJSON(is);
	}
	
	@Override
	public long getSize(SessionRating sessionRating, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("AP.getSize() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType);
		return 0;
	}

	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("AP.isWriteable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(SessionRating.class));
		return clazz.equals(SessionRating.class);
	}

	@Override
	public void writeTo(SessionRating sessionRating, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> map, OutputStream os) throws IOException, WebApplicationException {
		toJSON(os, sessionRating);
	}
	
	public static SessionRating fromJSON(InputStream is) {
		JsonReader rdr = null; 
		try {
		     rdr = Json.createReader(is);
		     JsonObject sessionRatingJson = rdr.readObject();
		     SessionRating sessionRating = fromJSON(sessionRatingJson);
		     return sessionRating;
		} finally {
			if (rdr != null) {
				rdr.close();
			}
		}
		
	}
	
	public static SessionRating fromJSON(JsonObject sessionRatingJson) {
	     JsonString idJson = sessionRatingJson.getJsonString("id");
	     if(idJson == null){
	    	 idJson = sessionRatingJson.getJsonString("_id");
	     }
	     JsonString revJson = sessionRatingJson.getJsonString("_rev");
	     JsonString sessionJson = sessionRatingJson.getJsonString("session");
	     JsonString attendeeIdJson = sessionRatingJson.getJsonString("attendeeId");
	     JsonNumber ratingJson = sessionRatingJson.getJsonNumber("rating");
	     SessionRating sessionRating = new SessionRating(idJson != null ? idJson.getString() : null,
	    		 						revJson != null ? revJson.getString() : null,
	    		 								sessionJson.getString(), attendeeIdJson.getString(), ratingJson.intValue());
	     return sessionRating;
	}
	
	public static void toJSON(OutputStream os, SessionRating sessionRating) {
		JsonWriter jsonWriter = Json.createWriter(os);
		jsonWriter.writeObject(toJSON(sessionRating));
		jsonWriter.close();
	}
	
	public static JsonObject toJSON(SessionRating sessionRating) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		
		if(sessionRating.getId() != null) builder = builder.add("id", sessionRating.getId());
		builder = builder.add("session", sessionRating.getSession());
		builder = builder.add("attendeeId", sessionRating.getAttendeeId());
		builder = builder.add("rating", sessionRating.getRating());
		JsonObject jsonObject = builder.build();
		return jsonObject;
	}
	
	public static String toJSONString(SessionRating sessionRating) {
		return toJSON(sessionRating).toString();
	}
}
