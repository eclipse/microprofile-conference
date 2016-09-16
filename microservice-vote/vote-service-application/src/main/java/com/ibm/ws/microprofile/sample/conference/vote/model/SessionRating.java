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

package com.ibm.ws.microprofile.sample.conference.vote.model;

import java.util.UUID;

public class SessionRating {
	
	private String id;
	private String revision;
	private String session;
	private String attendeeId;
	private int rating;

	
	
	public SessionRating(String session, String attendeeId, int rating) {
		this(null, null, session, attendeeId, rating);
	}

	public SessionRating(String id, String revision, String session, String attendeeId, int rating) {
		this.id = id;
		this.revision = revision;
		this.session = session;
		this.attendeeId = attendeeId;
		this.rating = rating;
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getRevision() {
		return revision;
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
	public boolean equals(Object o) {
		if (o instanceof SessionRating) {
			SessionRating other = (SessionRating) o;
			return id == other.id && session.equals(other.session) && attendeeId == other.attendeeId && rating == other.rating;
		}
		return false;
	}
}
