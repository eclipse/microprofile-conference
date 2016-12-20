/*
 * Copyright 2016 Microprofile.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.microprofile.showcase.vote.api;

import static io.microprofile.showcase.vote.utils.Debug.isDebugEnabled;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
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

import io.microprofile.showcase.vote.model.Attendee;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendeeProvider implements MessageBodyReader<Attendee>, MessageBodyWriter<Attendee> {

    @Override
    public boolean isReadable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {

        if (isDebugEnabled())
            System.out.println("AP.isReadable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> "
                               + clazz.equals(Attendee.class));
        return clazz.equals(Attendee.class);
    }

    @Override
    public Attendee readFrom(Class<Attendee> clazz, Type type, Annotation[] annotations, MediaType mediaType,
                             MultivaluedMap<String, String> map, InputStream is) throws IOException, WebApplicationException {
        return fromJSON(is);
    }

    @Override
    public long getSize(Attendee attendee, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
        if (isDebugEnabled())
            System.out.println("AP.getSize() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType);
        return 0;
    }

    @Override
    public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
        if (isDebugEnabled())
            System.out.println("AP.isWriteable() clazz=" + clazz + " type=" + type + " annotations=" + annotations + " mediaType=" + mediaType + " ==> "
                               + clazz.equals(Attendee.class));
        return clazz.equals(Attendee.class);
    }

    @Override
    public void writeTo(Attendee attendee, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> map, OutputStream os) throws IOException, WebApplicationException {
        toJSON(os, attendee);
    }

    public static Attendee fromJSON(InputStream is) {
        JsonReader rdr = null;
        try {
            rdr = Json.createReader(is);
            JsonObject attendeeJson = rdr.readObject();
            Attendee attendee = fromJSON(attendeeJson);
            return attendee;
        } finally {
            if (rdr != null) {
                rdr.close();
            }

        }

    }

    public static Attendee fromJSON(JsonObject attendeeJson) {
    	String id = getStringFromJson("id", attendeeJson);
    	String name = getStringFromJson("name", attendeeJson);
        Attendee attendee = new Attendee(id, name);
        return attendee;
    }

    private static String getStringFromJson(String key, JsonObject json) {
    	String returnedString = null;
		if (json.containsKey(key)) {
			JsonString value = json.getJsonString(key);
			if (value != null) {
				returnedString = value.getString();
			}
		}
		return returnedString;
	}

	public static void toJSON(OutputStream os, Attendee attendee) {
        JsonWriter jsonWriter = Json.createWriter(os);
        jsonWriter.writeObject(toJSON(attendee));
        jsonWriter.close();
    }

    public static JsonObject toJSON(Attendee attendee) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (attendee.getId() != null)
            builder = builder.add("id", attendee.getId());
        builder = builder.add("name", attendee.getName());
        JsonObject jsonObject = builder.build();
        return jsonObject;
    }

    public static String toJSONString(Attendee attendee) {
        return toJSON(attendee).toString();
    }
}
