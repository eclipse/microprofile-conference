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

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.model.SessionRating;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.AttendeeDAO;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.NonPersistent;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.Persistent;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.SessionRatingDAO;
import com.ibm.ws.microprofile.sample.conference.vote.utils.Log;

@ApplicationScoped
@Path("/session")
@Log
public class SessionVote {

	private @Inject @NonPersistent AttendeeDAO hashMapAttendeeDAO;
	private @Inject @Persistent AttendeeDAO couchDBAttendeeDAO;
	
	private @Inject @NonPersistent SessionRatingDAO hashMapSessionRatingDAO;
	private @Inject @Persistent SessionRatingDAO couchDBSessionRatingDAO;
	
	private AttendeeDAO selectedAttendeeDAO;
	private SessionRatingDAO selectedSessionRatingDAO;
	
	@PostConstruct
	private void connectToDAO()
	{
		if (couchDBAttendeeDAO.isAccessible()) {
			selectedAttendeeDAO = 	couchDBAttendeeDAO;
			selectedSessionRatingDAO = couchDBSessionRatingDAO;
		} else {
			selectedAttendeeDAO = 	hashMapAttendeeDAO;
			selectedSessionRatingDAO = hashMapSessionRatingDAO;
		}
	}
	
	public void setAttendeeSessionRating(AttendeeDAO attendee, SessionRatingDAO rating) {
		this.selectedAttendeeDAO = attendee;
		this.selectedSessionRatingDAO = rating;
	}
	
	@POST
	@Path("/attendee")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public Attendee registerAttendee(String name) {
		Attendee attendee = selectedAttendeeDAO.createNewAttendee(new Attendee(name));
		return attendee;  
	}
	
	@PUT
	@Path("/attendee/{id}")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public Attendee updateAttendee(@PathParam("id") String id, Attendee attendee) {
		attendee.setID(id);
		Attendee updated = selectedAttendeeDAO.updateAttendee(attendee);
		return updated;
	}

	@GET
	@Path("/attendee")
	@Produces(APPLICATION_JSON)
	public Collection<Attendee> getAllAttendees() {
		return selectedAttendeeDAO.getAllAttendees();
	}
	
	@GET
	@Path("/attendee/{id}")
	@Produces(APPLICATION_JSON)
	public Attendee getAttendee(@PathParam("id") String id) {
		return selectedAttendeeDAO.getAttendee(id);
	}
	
	@DELETE
	@Path("/attendee/{id}")
	@Produces(APPLICATION_JSON)
	public void deleteAttendee(@PathParam("id") String id) {
		 selectedAttendeeDAO.deleteAttendee(id);
	}
	
	@POST
	@Path("/rate")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public SessionRating rateSession(SessionRating sessionRating) {
		SessionRating rating = selectedSessionRatingDAO.rateSession(sessionRating);
		return rating;
	}

	@GET
	@Path("/rate")
	@Produces(APPLICATION_JSON)
	public Collection<SessionRating> getAllSessionRatings() {
		return selectedSessionRatingDAO.getAllRatings();
	}
	
	@PUT
	@Path("/rate/{id}")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public SessionRating updateRating(@PathParam("id") String id, SessionRating newRating) {
		newRating.setId(id);
		selectedSessionRatingDAO.updateRating(newRating);
		
		return newRating;
	}
	
	@GET
	@Path("/rate/{id}")
	@Produces(APPLICATION_JSON)
	public SessionRating getRating(@PathParam("id") String id) {
		return selectedSessionRatingDAO.getRating(id);
	}
	
	@DELETE
	@Path("/rate/{id}")
	@Produces(APPLICATION_JSON)
	public void deleteRating(@PathParam("id") String id) {
		selectedSessionRatingDAO.deleteRating(id);
	}

	@GET
	@Path("/ratingsBySession")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public Collection<SessionRating> allSessionVotes(@QueryParam("sessionId") String sessionId) {
		return selectedSessionRatingDAO.getRatingsBySession(sessionId);
	}

	@GET
	@Path("/averageRatingBySession")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public double sessionRatingAverage(@QueryParam("sessionId") String sessionId) {
		Collection<SessionRating> allSessionVotes = allSessionVotes(sessionId);
		int denominator = allSessionVotes.size();
		int numerator = 0;
		for (SessionRating rating : allSessionVotes) {
			numerator += rating.getRating();
		}
		return ((double) numerator / denominator);
	}

	@GET
	@Path("/ratingsByAttendee")
	@Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
	public Collection<SessionRating> votesByAttendee(@QueryParam("attendeeId") String attendeeId) {
		return selectedSessionRatingDAO.getRatingsByAttendee(attendeeId);
	}
	

	///////////////////////////////////////////////////////////////////////////////
	// The following methods are intended for testing only - not part of the API //
	void clearAllAttendees() {
		selectedAttendeeDAO.clearAllAttendees();
	}

	void clearAllRatings() {
		selectedSessionRatingDAO.clearAllRatings();
	}
}
