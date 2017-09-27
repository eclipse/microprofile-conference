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

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonWriter;

public class SessionRating {

    private final String _id;  // internal field for CouchDB
    private final String _rev; // internal field for CouchDB
    private String id;
    private String session;
    private String attendeeId;
    private int rating;

    public SessionRating(String session, String attendeeId, int rating) {
        this(null, session, attendeeId, rating);
    }

    public SessionRating(String id, String session, String attendeeId, int rating) {
    	this(id, session, attendeeId, rating, null, null);
    }
    
    private SessionRating(String id, String session, String atteendeeId, int rating, String _id, String _rev) {
        this.id = id;
        this.session = session;
        this.attendeeId = atteendeeId;
        this.rating = rating;
        this._id = _id;
        this._rev = _rev;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendee(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getRev() {
    	return this._rev;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attendeeId == null) ? 0 : attendeeId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + rating;
        result = prime * result + ((session == null) ? 0 : session.hashCode());
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
        SessionRating other = (SessionRating) obj;
        if (attendeeId == null) {
            if (other.attendeeId != null)
                return false;
        } else if (!attendeeId.equals(other.attendeeId))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (rating != other.rating)
            return false;
        if (session == null) {
            if (other.session != null)
                return false;
        } else if (!session.equals(other.session))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SessionRating [id=" + id + ", session=" + session + ", attendeeId="
                + attendeeId + ", rating=" + rating + "]";
    }
    
    public String toDebug() {
    	return "SessionRating [id=" + id + ", session=" + session + ", attendeeId="
                + attendeeId + ", rating=" + rating +  ", _rev=" + _rev + ", _id=" + _id + "]";
    }
    
    public static SessionRating fromJSON(InputStream is) {
    	try(JsonReader rdr = Json.createReader(is)) {
            JsonObject sessionRatingJson = rdr.readObject();
            SessionRating sessionRating = fromJSON(sessionRatingJson);
            return sessionRating;
    	}
    }

    public static SessionRating fromJSON(JsonObject sessionRatingJson) {
    	String id = getStringFromJson("id", sessionRatingJson);
    	String session = getStringFromJson("session", sessionRatingJson);
    	String attendeeId = getStringFromJson("attendeeId", sessionRatingJson);
    	String _rev = getStringFromJson("_rev", sessionRatingJson);
    	String _id = getStringFromJson("_id", sessionRatingJson);
    	int rating = 0;
    	if (sessionRatingJson.containsKey("rating")) {
    		JsonNumber ratingJson = sessionRatingJson.getJsonNumber("rating");
    		if (ratingJson != null) {
    			rating = ratingJson.intValue();
    		}
    	}
    	
        SessionRating sessionRating = new SessionRating(id, session, attendeeId, rating, _id, _rev);
        return sessionRating;
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
    
    public static void toJSON(OutputStream os, SessionRating sessionRating) {
        JsonWriter jsonWriter = Json.createWriter(os);
        jsonWriter.writeObject(toJSON(sessionRating));
        jsonWriter.close();
    }

    public static JsonObject toJSON(SessionRating sessionRating) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (sessionRating.id != null)
            builder = builder.add("id", sessionRating.id);
        if(sessionRating._rev != null)
        	builder = builder.add("_rev", sessionRating._rev);
        if(sessionRating._id != null)
        	builder = builder.add("_id", sessionRating._id);
        if(sessionRating.session != null)
        	builder = builder.add("session", sessionRating.session);
        if(sessionRating.attendeeId != null)
        	builder = builder.add("attendeeId", sessionRating.attendeeId);
        builder = builder.add("rating", sessionRating.getRating());
        return builder.build();
    }

    public static String toJSONString(SessionRating sessionRating) {
        return toJSON(sessionRating).toString();
    }
    
}
