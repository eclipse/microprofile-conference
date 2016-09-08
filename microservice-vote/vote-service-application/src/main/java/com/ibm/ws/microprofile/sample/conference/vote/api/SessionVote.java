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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.model.SessionRating;

@Path("/session")
public class SessionVote {

	private AtomicLong nextAttendeeId = new AtomicLong(0);
	private Map<Long,Attendee> attendees = new HashMap<Long,Attendee>();

	private AtomicLong nextSessionId = new AtomicLong(0);
	private Map<Long,SessionRating> allRatings = new HashMap<Long,SessionRating>();

	private Map<String,List<Long>> ratingIdsBySession = new HashMap<String,List<Long>>();

	private Map<Long,List<Long>> ratingIdsByAttendee = new HashMap<Long,List<Long>>();

	@PUT
	@Path("/attendee")
	@Produces(APPLICATION_JSON)
        @Consumes(APPLICATION_JSON)
	public Attendee registerAttendee(String name) {
		Long id = nextAttendeeId.incrementAndGet();
		Attendee attendee = new Attendee(id, name);
		attendees.put(id, attendee);
		return attendee;  
	}
	
	@POST
	@Path("/attendee")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public Attendee updateAttendee(Attendee attendee) {
		attendees.put(attendee.getId(), attendee);
		return attendee;
	}

	@PUT
	@Path("/rate")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public SessionRating rateSession(SessionRating sessionRating) {
		long id = nextSessionId.incrementAndGet();
		String session = sessionRating.getSession();
		long attendeeId = sessionRating.getAttendeeId();
		int rating = sessionRating.getRating();
		SessionRating sr = new SessionRating(id, session, attendeeId, rating);
		allRatings.put(id, sr);

		List<Long> sessionRatings = ratingIdsByAttendee.get(attendeeId);
		if (sessionRatings == null) {
			sessionRatings = new ArrayList<Long>();
			ratingIdsByAttendee.put(attendeeId, sessionRatings);
		}
		sessionRatings.add(id);

		sessionRatings = ratingIdsBySession.get(session);
		if (sessionRatings == null) {
			sessionRatings = new ArrayList<Long>();
			ratingIdsBySession.put(session, sessionRatings);
		}
		sessionRatings.add(id);

		return sr;
	}

	@POST
	@Path("/rate")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public SessionRating updateRating(SessionRating newRating) {
		allRatings.put(newRating.getId(), newRating);
		return newRating;
	}

	@POST
	@Path("/ratingsBySession")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public List<SessionRating> allSessionVotes(String sessionId) {
		System.out.println("> allSessionVotes() " + sessionId);
		for (Map.Entry<String, List<Long>> entry : ratingIdsBySession.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
		List<SessionRating> allSessionVotes = new ArrayList<SessionRating>();
		List<Long> ids = ratingIdsBySession.get(sessionId);
		for (Long id : ids) {
			allSessionVotes.add(allRatings.get(id));
		}
		return allSessionVotes;
	}

	@POST
	@Path("/averageRatingBySession")
	//@Produces(APPLICATION_JSON)
    //@Consumes(APPLICATION_JSON)
	public double sessionRatingAverage(String sessionId) {
		List<SessionRating> allSessionVotes = allSessionVotes(sessionId);
		int denominator = allSessionVotes.size();
		int numerator = 0;
		for (SessionRating rating : allSessionVotes) {
			numerator += rating.getRating();
		}
		return ((double) numerator / denominator);
	}

	@POST
	@Path("/ratingsByAttendee")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public List<SessionRating> votesByAttendee(Attendee attendee) {
		List<SessionRating> allAttendeeVotes = new ArrayList<SessionRating>();
		List<Long> ids = ratingIdsByAttendee.get(attendee.getId());
		for (Long id : ids) {
			allAttendeeVotes.add(allRatings.get(id));
		}
		return allAttendeeVotes;
	}
	

	///////////////////////////////////////////////////////////////////////////////
	// The following methods are intended for testing only - not part of the API //
	Collection<Attendee> getAllRegisteredAttendees() {
		return attendees.values();
	}

	Collection<SessionRating> getAllSessionRatings() {
		return allRatings.values();
	}

	void clearAllAttendees() {
		attendees.clear();
	}

	void clearAllRatings() {
		allRatings.clear();
		ratingIdsByAttendee.clear();
		ratingIdsBySession.clear();
	}
}
