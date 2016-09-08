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

public class SessionRating {
	
	private long id = -1;
	private String session;
	private long attendeeId;
	private int rating;

	public SessionRating(long id, String session, long attendeeId, int rating) {
		this(session, attendeeId, rating);
		this.id = id;
	}
	
	public SessionRating(String session, long attendeeId, int rating) {
		this.session = session;
		this.attendeeId = attendeeId;
		this.rating = rating;
	}

	public long getId() {
		return id;
	}

	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public long getAttendeeId() {
		return attendeeId;
	}
	public void setAttendee(long attendeeId) {
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
