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

package com.ibm.ws.microprofile.conference.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.ibm.ws.microprofile.sample.conference.vote.api.AttendeeProvider;
import com.ibm.ws.microprofile.sample.conference.vote.api.SessionRatingListProvider;
import com.ibm.ws.microprofile.sample.conference.vote.api.SessionRatingProvider;
import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.model.SessionRating;

import org.junit.Test;

public class VoteClient {

	private static String ROOT_URL = "http://localhost:9080/vote/rest/session";
	private static String ATTENDEE_URL = ROOT_URL + "/attendee";
	private static String RATE_URL = ROOT_URL + "/rate";
	private static String RATE_BY_SESSION_URL = ROOT_URL + "/ratingsBySession";
	private static String AVG_RATE_BY_SESSION_URL = ROOT_URL + "/averageRatingBySession";
	private static String RATE_BY_ATTENDEE_URL = ROOT_URL + "/ratingsByAttendee";
	
	private final Client client;
	
	VoteClient() {
		client = ClientBuilder.newBuilder().build();
		client.register(AttendeeProvider.class);
		client.register(SessionRatingProvider.class);
		client.register(SessionRatingListProvider.class);
	}
	
	public Attendee registerAttendee(String name) {
		UriBuilder uriBuilder = UriBuilder.fromPath(ATTENDEE_URL);
		Response r = client.target(uriBuilder).request(MediaType.APPLICATION_JSON).put(Entity.json(name));
		Attendee registeredAttendee = r.readEntity(Attendee.class);
		return registeredAttendee;
	}

	public Attendee updateAttendee(Attendee attendee) {
		UriBuilder uriBuilder = UriBuilder.fromPath(ATTENDEE_URL);
		Response r = client.target(uriBuilder).request(MediaType.APPLICATION_JSON).post(Entity.json(attendee));
		Attendee updatedAttendee = r.readEntity(Attendee.class);
		return updatedAttendee;
	}
	
	public SessionRating rateSession(SessionRating rating) {
		UriBuilder uriBuilder = UriBuilder.fromPath(RATE_URL);
		Response r = client.target(uriBuilder).request(MediaType.APPLICATION_JSON).put(Entity.json(rating));
		SessionRating updatedRating = r.readEntity(SessionRating.class);
		return updatedRating;
	}
	
	public SessionRating updateRating(SessionRating rating) {
		UriBuilder uriBuilder = UriBuilder.fromPath(RATE_URL);
		Response r = client.target(uriBuilder).request(MediaType.APPLICATION_JSON).post(Entity.json(rating));
		SessionRating updatedRating = r.readEntity(SessionRating.class);
		return updatedRating;
	}
	
	public List<SessionRating> listAllRatingsBySession(String session) throws IOException {
		UriBuilder uriBuilder = UriBuilder.fromPath(RATE_BY_SESSION_URL);
		Response r = client.target(uriBuilder).request(MediaType.APPLICATION_JSON).post(Entity.json(session));
		@SuppressWarnings("unchecked")
		List<SessionRating> ratings = r.readEntity(List.class);
		return ratings;
	}
	
	public double getAverageRatingsBySession(String session) throws IOException {
		UriBuilder uriBuilder = UriBuilder.fromPath(AVG_RATE_BY_SESSION_URL);
		Response r = client.target(uriBuilder).request().post(Entity.json(session));
		double d = r.readEntity(Double.class);
		return d;
	}
	
	public List<SessionRating> listAllRatingsByAttendee(Attendee attendee) throws IOException {
		UriBuilder uriBuilder = UriBuilder.fromPath(RATE_BY_ATTENDEE_URL);
		Response r = client.target(uriBuilder).request(MediaType.APPLICATION_JSON).post(Entity.json(attendee));
		@SuppressWarnings("unchecked")
		List<SessionRating> ratings = r.readEntity(List.class);
		return ratings;
	}
	
	@Test
	public void testRegisterAttendee() {
		// Register attendee and verify that the returned name matches the name submitted
		Attendee attendee = registerAttendee("Adam Smith");
		assertEquals("Registered attendees name does not match name submitted", "Adam Smith", attendee.getName());

		// Register another attendee and verify that the IDs are not the same
		Attendee attendee2 = registerAttendee("Sally Smith");
		assertNotEquals("Mulitple registered attendees have the same ID", attendee.getId(), attendee2.getId());
	}

	@Test
	public void testUpdateAttendee() {
		// Register attendee with full name
		Attendee originalAttendee = registerAttendee("Josef Chechov");

		// Update with nickname
		Attendee updatedAttendee = new Attendee(originalAttendee.getId(), "Joe Chechov");
		updatedAttendee = updateAttendee(updatedAttendee);

		assertEquals("Unexpected name for updated attendee", "Joe Chechov", updatedAttendee.getName());
		assertEquals("Unexpected change of ID when updating attendee name", originalAttendee.getId(), updatedAttendee.getName());
	}

	@Test
	public void testRateUpdateAndCheckSession() throws Exception {
		// Register attendees
		Attendee attendee1 = registerAttendee("Tyrone Watson");
		Attendee attendee2 = registerAttendee("Maria Iglesia");
		
		// Rate sessions
		SessionRating sr1 = rateSessionAndCheck(new SessionRating("Microprofile: The Next Big Thing", attendee1.getId(), 9));
		SessionRating sr2 = rateSessionAndCheck(new SessionRating("Microprofile: The Next Big Thing", attendee2.getId(), 8));
		SessionRating sr3 = rateSessionAndCheck(new SessionRating("What's coming in Java EE 8?", attendee1.getId(), 7));
		SessionRating sr4 = rateSessionAndCheck(new SessionRating("What's coming in Java EE 8?", attendee2.getId(), 8));
		SessionRating sr5 = rateSessionAndCheck(new SessionRating("OSGi Basics", attendee1.getId(), 4));
		SessionRating sr6 = rateSessionAndCheck(new SessionRating("What is WebSphere Liberty?", attendee1.getId(), 10));
		SessionRating sr7 = rateSessionAndCheck(new SessionRating("What is WebSphere Liberty?", attendee2.getId(), 10));

		// Update a session
		SessionRating originalSr5 = sr5;
		sr5 = updateRating(new SessionRating(originalSr5.getId(), originalSr5.getRevision(), originalSr5.getSession(), originalSr5.getAttendeeId(), 6));
		assertEquals("Unexpected rating value from udpated SessionRating", 6, sr5.getRating());

		// Check rating for a given session
		List<SessionRating> ratingsForMicroprofile = listAllRatingsBySession("Microprofile: The Next Big Thing");
		assertEquals("The returned list of Microprofile session ratings contains an unexpected number of entries", 2, 
				ratingsForMicroprofile.size());
		assertTrue("The returned ratings for the Microprofile session do not contain the expected ratings",
				ratingsForMicroprofile.contains(sr1) && ratingsForMicroprofile.contains(sr2));

		// Check the average rating for a different session
		double avg = getAverageRatingsBySession("What's coming in Java EE 8?");
		assertEquals("Unexpected average rating for Java EE8 session", 7.5, avg, 0.0);

		// Check rating for a given attendee
		List<SessionRating> ratingsFromAttendee2 = listAllRatingsByAttendee(attendee2);
		assertEquals("The returned list of attendee2's session ratings contains an unexpected number of entries", 3, 
				ratingsFromAttendee2.size());
		assertTrue("The returned ratings from attendee2 do not contain the expected ratings",
				ratingsFromAttendee2.contains(sr2) && ratingsFromAttendee2.contains(sr4) && ratingsFromAttendee2.contains(sr7));


		
	}

	private SessionRating rateSessionAndCheck(SessionRating rating) {
		SessionRating returnedRating = rateSession(rating);
		assertEquals("Unexpected session from returned SessionRating", rating.getSession(), returnedRating.getSession());
		assertEquals("Unexpected attendee ID from returned SessionRating", rating.getAttendeeId(), returnedRating.getAttendeeId());
		assertEquals("Unexpected rating value from returned SessionRating", rating.getRating(), returnedRating.getRating());
		return returnedRating;
	}

	public static void main(String[] args) throws Throwable {
		VoteClient voteClient = new VoteClient();
		
		// register attendee
		Attendee attendee = voteClient.registerAttendee("James Brown");
		
		// update his name
		attendee.setName("Jim Brown");
		Attendee updatedAttendee = voteClient.updateAttendee(attendee);
		
		// provide his opinion on Session 1
		SessionRating rating1 = new SessionRating("Java 101", updatedAttendee.getId(), 7);
		rating1 = voteClient.rateSession(rating1);
		
		// provide his opinion on Session 2
		SessionRating rating2 = new SessionRating("Cool Tricks with Jython", updatedAttendee.getId(), 7);
		rating2 = voteClient.rateSession(rating2);
		
		// oops he meant to give Session 1 a higher rating... let's change that now
		rating1.setRating(9);
		rating1 = voteClient.updateRating(rating1);
		
		// Andy was in the same session as Doug - let's see Doug's feedback
		Attendee doug = voteClient.registerAttendee("Doug Adams");
		SessionRating dougsRating = new SessionRating("Java 101", doug.getId(), 7);
		dougsRating = voteClient.rateSession(dougsRating);
		
		// now let's see all of the ratings for the Java 101 session
		List<SessionRating> java101Ratings = voteClient.listAllRatingsBySession("Java 101");
		for (SessionRating rating : java101Ratings) {
			System.out.println(rating.getAttendeeId() + ": " + rating.getRating());
		}
		
		// now, let's check the average rating for Java 101:
		double d = voteClient.getAverageRatingsBySession("Java 101");
		System.out.println("Average Rating for Java 101: " + d);
		
		// now, let's see all of the sessions that Andy rated:
		List<SessionRating> andyRatings = voteClient.listAllRatingsByAttendee(attendee);
		for (SessionRating rating : andyRatings) {
			System.out.println(rating.getAttendeeId() + ": " + rating.getRating());
		}
	}

}
