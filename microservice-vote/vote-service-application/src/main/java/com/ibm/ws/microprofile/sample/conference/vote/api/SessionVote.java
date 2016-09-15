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

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.model.SessionRating;
import com.ibm.ws.microprofile.sample.conference.vote.store.AttendeeStore;
import com.ibm.ws.microprofile.sample.conference.vote.store.Persistent;

@Path("/session")
public class SessionVote {

	@Inject
	@Persistent
	AttendeeStore store;
	
	private AtomicLong nextSessionId = new AtomicLong(0);
	private Map<String,SessionRating> allRatings = new HashMap<String,SessionRating>();

	private Map<String,List<String>> ratingIdsBySession = new HashMap<String,List<String>>();

	private Map<String,List<String>> ratingIdsByAttendee = new HashMap<String,List<String>>();

	@POST
	@Path("/attendee")
	@Produces(APPLICATION_JSON)
        @Consumes(APPLICATION_JSON)
	public Attendee registerAttendee(String name) {
		Attendee attendee = store.createNewAttendee(new Attendee(name));
		return attendee;  
	}
	
	@PUT
	@Path("/attendee/{id}")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public Attendee updateAttendee(@PathParam("id") String id, Attendee attendee) {
		attendee.setID(id);
		Attendee updated = store.updateAttendee(attendee);
		return updated;
	}

	@GET
	@Path("/attendee")
	@Produces(APPLICATION_JSON)
	public Collection<Attendee> listAttendees() {
		return store.getAllAttendees();
	}
	
	@GET
	@Path("/attendee/{id}")
	@Produces(APPLICATION_JSON)
	public Attendee getAttendee(@PathParam("id") String id) {
		return store.getAttendee(id);
	}
	
	@DELETE
	@Path("/attendee/{id}")
	@Produces(APPLICATION_JSON)
	public Attendee deleteAttendee(@PathParam("id") String id) {
		return store.deleteAttendee(id);
	}
	
	@PUT
	@Path("/rate")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public SessionRating rateSession(SessionRating sessionRating) {
		String id = ""+nextSessionId.incrementAndGet();
		String session = sessionRating.getSession();
		String attendeeId = sessionRating.getAttendeeId();
		int rating = sessionRating.getRating();
		SessionRating sr = new SessionRating(id, session, attendeeId, rating);
		allRatings.put(id, sr);

		List<String> sessionRatings = ratingIdsByAttendee.get(attendeeId);
		if (sessionRatings == null) {
			sessionRatings = new ArrayList<String>();
			ratingIdsByAttendee.put(attendeeId, sessionRatings);
		}
		sessionRatings.add(id);

		sessionRatings = ratingIdsBySession.get(session);
		if (sessionRatings == null) {
			sessionRatings = new ArrayList<String>();
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
		for (Map.Entry<String, List<String>> entry : ratingIdsBySession.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
		List<SessionRating> allSessionVotes = new ArrayList<SessionRating>();
		List<String> ids = ratingIdsBySession.get(sessionId);
		for (String id : ids) {
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
		List<String> ids = ratingIdsByAttendee.get(attendee.getId());
		for (String id : ids) {
			allAttendeeVotes.add(allRatings.get(id));
		}
		return allAttendeeVotes;
	}
	

	///////////////////////////////////////////////////////////////////////////////
	// The following methods are intended for testing only - not part of the API //
	Collection<Attendee> getAllRegisteredAttendees() {
		return store.getAllAttendees();
	}

	Collection<SessionRating> getAllSessionRatings() {
		return allRatings.values();
	}

	void clearAllAttendees() {
		store.clearAllAttendees();
	}

	void clearAllRatings() {
		allRatings.clear();
		ratingIdsByAttendee.clear();
		ratingIdsBySession.clear();
	}
}
