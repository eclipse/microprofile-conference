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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
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

import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.utils.TeeOutputStream;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttendeeListProvider implements MessageBodyReader<List<Attendee>>, MessageBodyWriter<List<Attendee>> {

	
	@Override
	public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		boolean isReadable = List.class.isAssignableFrom(clazz);
		if(isReadable && type instanceof ParameterizedType){
			ParameterizedType paramType = (ParameterizedType) type;
			Type[] actualTypes = paramType.getActualTypeArguments();
			if(actualTypes.length == 1){
				isReadable = actualTypes[0] == Attendee.class;
			}
		}
		if (isDebugEnabled()) System.out.println("SRLP.isReadable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + clazz.equals(Attendee.class));
		return isReadable;
	}

	@Override
	public List<Attendee> readFrom(Class<List<Attendee>> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream is) throws IOException, WebApplicationException {
		JsonReader rdr = null; 
		try {
			List<Attendee> ratings = new ArrayList<Attendee>();
			rdr = Json.createReader(is);
			JsonArray arr = rdr.readArray();
			for (int i = 0 ; i <arr.size(); i++) {
				JsonObject attendeeJson = arr.getJsonObject(i);//rdr.readObject();
				if (isDebugEnabled()) System.out.println(attendeeJson);
				Attendee attendee = AttendeeProvider.fromJSON(attendeeJson);
				ratings.add(attendee);
			}
			return ratings;
		} finally {
			if (rdr != null) {
				rdr.close();
			}
		}

	}

	@Override
	public long getSize(List<Attendee> Attendee, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		if (isDebugEnabled()) System.out.println("ALP.getSize() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType);
		return 0;
	}

	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		boolean isWriteable = List.class.isAssignableFrom(clazz);
		if(isWriteable && type instanceof ParameterizedType){
			ParameterizedType paramType = (ParameterizedType) type;
			Type[] actualTypes = paramType.getActualTypeArguments();
			if(actualTypes.length == 1){
				isWriteable = actualTypes[0] == Attendee.class;
			}
		}
		
		if (isDebugEnabled()) System.out.println("ALP.isWriteable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> " + isWriteable);
		return isWriteable;
	}

	@Override
	public void writeTo(List<Attendee> Attendees, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> map, OutputStream os) throws IOException, WebApplicationException {
		
//		JsonGenerator jsonGenerator = Json.createGenerator(new TeeOutputStream(os, System.out));
//		jsonGenerator.writeStartArray();
//		for (Attendee Attendee : Attendees) {
//			jsonGenerator.writeStartObject()
//				.write("id", Attendee.getId())
//				.write("session", Attendee.getSession())
//				.write("attendeeId", Attendee.getAttendeeId())
//				.write("rating", Attendee.getRating())
//			.writeEnd();
//		}
//		jsonGenerator.writeEnd();
//		jsonGenerator.flush();
//		jsonGenerator.close();
		
		JsonWriter writer = Json.createWriter(new TeeOutputStream(os, System.out));
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for (Attendee attendee : Attendees) {
			JsonObject attendeeJson = AttendeeProvider.toJSON(attendee);
			arrayBuilder.add(attendeeJson);
		}
		writer.writeArray(arrayBuilder.build());
		writer.close();
	}
}
