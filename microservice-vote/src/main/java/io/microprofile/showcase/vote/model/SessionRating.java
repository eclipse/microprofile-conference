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

package io.microprofile.showcase.vote.model;

public class SessionRating {

    private String id;
    private String session;
    private String attendeeId;
    private int rating;

    public SessionRating(String session, String attendeeId, int rating) {
        this(null, session, attendeeId, rating);
    }

    public SessionRating(String id, String session, String attendeeId, int rating) {
        this.id = id;
        this.session = session;
        this.attendeeId = attendeeId;
        this.rating = rating;
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
    
}
