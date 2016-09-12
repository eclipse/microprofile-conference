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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.ws.microprofile.sample.conference.vote.api.SessionVote;
import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.model.SessionRating;

public class SessionVoteTest {

	private static SessionVote sessionVote;
	
	@BeforeClass
	public static void beforeClass() {
		sessionVote = new SessionVote();
	}
	
	@Before
	public void clearPreviousEntries() {
		sessionVote.clearAllAttendees();
		sessionVote.clearAllRatings();
	}

	@Test
	public void testRegisterAttendee() {
		// API method under test:
		Attendee johnDoe = sessionVote.registerAttendee("John Doe");
		assertEquals("Unexpected name returned for registered attendee", "John Doe", johnDoe.getName());

		Attendee janeDoe = sessionVote.registerAttendee("Jane Doe");
		assertEquals("Unexpected name returned for second registered attendee", "Jane Doe", janeDoe.getName());
		assertNotEquals("Both attendees have the same ID", johnDoe.getId(), janeDoe.getId());

		// Verify that both attendees are registered using a non-public method
		Collection<Attendee> attendees = sessionVote.getAllRegisteredAttendees();
		assertTrue("The session vote service is missing the first registered attendee", attendees.contains(johnDoe));
		assertTrue("The session vote service is missing the second registered attendee", attendees.contains(janeDoe));
			
	}

	@Test
	public void testUpdateAttendee() {
		Attendee jonathanDoe = sessionVote.registerAttendee("Jonathan Doe");
		long id = jonathanDoe.getId();
		Attendee jonDoe = new Attendee(id, "Jon Doe");
		
		// API method under test:
		Attendee attendee = sessionVote.updateAttendee(jonDoe);
		assertEquals("Attendee's name was not updated as expected", "Jon Doe", attendee.getName());
		assertEquals("Attendee's ID was updated when it was expected to stay the same", id, attendee.getId());

		// Now verify that there is only one attendee in the system, and that it is the correct one:
		Collection<Attendee> attendees = sessionVote.getAllRegisteredAttendees();
		assertEquals("An unexpected number of attendees have been registered", 1, attendees.size());
		assertTrue("The session vote service is missing the updated attendee", attendees.contains(jonDoe));
	}

	@Test
	public void testRateSession() {
		SessionRating sessionRating = new SessionRating("session1", 200, 5);
		
		// API method under test:
		sessionRating = sessionVote.rateSession(sessionRating);

		assertEquals("Returned SessionRating's session name does not match expected session name", "session1", sessionRating.getSession());
		assertEquals("Returned SessionRating's attendee ID does not match expected attendee ID", 200, sessionRating.getAttendeeId());
		assertEquals("Returned SessionRating's rating value does not match expected value", 5, sessionRating.getRating());

		// Now check that the SessionRating stored in the system matches what was returned
		assertTrue("The session vote service is missing the newly-submitted session rating", sessionVote.getAllSessionRatings().contains(sessionRating));
	}

	@Test
	public void testUpdateRating() {
		SessionRating originalRating = new SessionRating("session1", 200, 5);
		originalRating = sessionVote.rateSession(originalRating);

		SessionRating updatedRating = new SessionRating(originalRating.getId(), "session2", 300, 7);

		// API method under test
		updatedRating = sessionVote.updateRating(updatedRating);

		assertEquals("Unexpected session in updated rating", "session2", updatedRating.getSession());
		assertEquals("Unexpected attendee ID in updated rating", 300, updatedRating.getAttendeeId());
		assertEquals("Unexpected rating value in updated rating", 7, updatedRating.getRating());

		// Now check that the SessionRating stored in the system matches the updated SessionRating that was returned
		assertTrue("The session vote service is missing the newly-updated session rating", sessionVote.getAllSessionRatings().contains(updatedRating));
	}

	@Test
	public void testAllSessionVotes() {
		// submit 4 votes - 3 for session1, and 1 for session2
		SessionRating sr1 = sessionVote.rateSession(new SessionRating("session1", 100, 3));
		SessionRating sr2 = sessionVote.rateSession(new SessionRating("session1", 200, 4));
		SessionRating sr3 = sessionVote.rateSession(new SessionRating("session2", 300, 8));
		SessionRating sr4 = sessionVote.rateSession(new SessionRating("session1", 400, 9));

		// API method under test
		List<SessionRating> session1Ratings = sessionVote.allSessionVotes("session1");

		assertEquals("Unexpected number of ratings for session1", 3, session1Ratings.size());
		assertTrue("Returned ratings does not include all submitted ratings for session1", session1Ratings.contains(sr1) && 
                                                                                                   session1Ratings.contains(sr2) &&
                                                                                                   session1Ratings.contains(sr4));
	}

	@Test
	public void testSessionRatingAverage() {
		// submit 4 votes - 3 for session1, and 1 for session2
		SessionRating sr1 = sessionVote.rateSession(new SessionRating("session1", 101, 3));
		SessionRating sr2 = sessionVote.rateSession(new SessionRating("session1", 201, 8));
		SessionRating sr3 = sessionVote.rateSession(new SessionRating("session2", 301, 8));
		SessionRating sr4 = sessionVote.rateSession(new SessionRating("session1", 401, 10));

		// API method under test
		double avg = sessionVote.sessionRatingAverage("session1");

		assertEquals("Unexpected result from session1 rating average", 7.0, avg, 0.0);
	}

	@Test
	public void testVotesByAttendee() {
		Attendee attendee1 = sessionVote.registerAttendee("John Doe");
		Attendee attendee2 = sessionVote.registerAttendee("Jane Doe");

		// submit 4 votes - 3 for attendee1, and 1 for attendee2
		SessionRating sr1 = sessionVote.rateSession(new SessionRating("session1", attendee1.getId(), 3));
		SessionRating sr2 = sessionVote.rateSession(new SessionRating("session2", attendee2.getId(), 4));
		SessionRating sr3 = sessionVote.rateSession(new SessionRating("session3", attendee1.getId(), 8));
		SessionRating sr4 = sessionVote.rateSession(new SessionRating("session4", attendee1.getId(), 9));

		// API method under test
		List<SessionRating> attendee1Ratings = sessionVote.votesByAttendee(attendee1);
		List<SessionRating> attendee2Ratings = sessionVote.votesByAttendee(attendee2);

		assertEquals("Unexpected number of ratings from attendee1", 3, attendee1Ratings.size());
		assertTrue("Returned ratings does not include all submitted ratings by attendee1", attendee1Ratings.contains(sr1) && 
                                                                                                   attendee1Ratings.contains(sr3) &&
                                                                                                   attendee1Ratings.contains(sr4));
		assertEquals("Unexpected number of ratings from attendee2", 1, attendee2Ratings.size());
		assertTrue("Returned ratings does not include all submitted ratings by attendee2", attendee2Ratings.contains(sr2)); 

	}
}
