/*
 * Copyright (c) 2016 IBM, and others
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

package io.microprofile.showcase.vote.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonWriter;

public class Attendee {

    private final String _id;  // internal field for CouchDB
    private final String _rev; // internal field for CouchDB
    private String id;
    private String name;

    public Attendee(String name) {
        this(null, name);
    }

    public Attendee(String id, String name) {
        this(id, name, null, null);
    }
    
    private Attendee(String id, String name, String _rev, String _id) {
        this.id = id;
        this.name = name;
        this._rev = _rev;
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getRev() {
    	return _rev;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Attendee other = (Attendee) obj;
        if(!Objects.equals(this.id, other.id))
        	return false;
        if(!Objects.equals(this.name, other.name))
        	return false;
        return true;
    }

    @Override
    public String toString() {
        return "Attendee [id=" + id + ", name=" + name + "]";
    }
    
    public String toDebug() {
    	return "Attendee [id=" + id + ", name=" + name + ", _rev=" + _rev + ", _id=" + _id + "]";
    }
    
    public static Attendee fromJSON(InputStream is) {
    	try(JsonReader rdr = Json.createReader(is)) {
            JsonObject attendeeJson = rdr.readObject();
            Attendee attendee = fromJSON(attendeeJson);
            return attendee;
    	}
    }

    public static Attendee fromJSON(JsonObject attendeeJson) {
    	String id = getStringFromJson("id", attendeeJson);
    	String name = getStringFromJson("name", attendeeJson);
    	String _rev = getStringFromJson("_rev", attendeeJson);
    	String _id = getStringFromJson("_id", attendeeJson);
        return new Attendee(id, name, _rev, _id);
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
        if(attendee._rev != null)
        	builder = builder.add("_rev", attendee._rev);
        if(attendee._id != null)
        	builder = builder.add("_id", attendee._id);
        return builder.build();
    }

    public static String toJSONString(Attendee attendee) {
        return toJSON(attendee).toString();
    }

}
