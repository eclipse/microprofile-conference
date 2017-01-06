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

package io.microprofile.showcase.vote.it.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.microprofile.showcase.vote.model.Attendee;
import io.microprofile.showcase.vote.model.SessionRating;

public class VoteClientTest {

	private static final AttendeeClientUtility acu = new AttendeeClientUtility();
	private static final RatingClientUtility rcu = new RatingClientUtility();

    @Before
    public void clearDatabase() {
        acu.deleteAllAttendees();
        rcu.deleteAllRatings();
    }
    
    @Test
    public void testRegisterAttendee() {
        // Register attendee and verify that the returned name matches the name submitted
        Attendee attendee = acu.registerAttendee("Adam Smith");
        assertEquals("Registered attendees name does not match name submitted", "Adam Smith", attendee.getName());

        // Register another attendee and verify that the IDs are not the same
        Attendee attendee2 = acu.registerAttendee("Sally Smith");
        assertNotEquals("Mulitple registered attendees have the same ID", attendee.getId(), attendee2.getId());
    }

    @Test
    public void testUpdateAttendee() {
        // Register attendee with full name
        Attendee originalAttendee = acu.registerAttendee("Josef Chechov");

        // Update with nickname
        Attendee updatedAttendee = new Attendee(originalAttendee.getId(), "Joe Chechov");
        updatedAttendee = acu.updateAttendee(updatedAttendee);

        assertEquals("Unexpected name for updated attendee", "Joe Chechov", updatedAttendee.getName());
        assertEquals("Unexpected change of ID when updating attendee name", originalAttendee.getId(), updatedAttendee.getId());
    }

    @Test
    public void testRateUpdateAndCheckSession() throws Exception {
        // Register attendees
        Attendee attendee1 = acu.registerAttendee("Tyrone Watson");
        Attendee attendee2 = acu.registerAttendee("Maria Iglesia");

        // Rate sessions
        SessionRating sr1 = rcu.rateSessionAndCheck(new SessionRating("Microprofile: The Next Big Thing", attendee1.getId(), 9));
        SessionRating sr2 = rcu.rateSessionAndCheck(new SessionRating("Microprofile: The Next Big Thing", attendee2.getId(), 8));
        @SuppressWarnings("unused")
		SessionRating sr3 = rcu.rateSessionAndCheck(new SessionRating("What's coming in Java EE 8?", attendee1.getId(), 7));
        SessionRating sr4 = rcu.rateSessionAndCheck(new SessionRating("What's coming in Java EE 8?", attendee2.getId(), 8));
        SessionRating sr5 = rcu.rateSessionAndCheck(new SessionRating("OSGi Basics", attendee1.getId(), 4));
        @SuppressWarnings("unused")
		SessionRating sr6 = rcu.rateSessionAndCheck(new SessionRating("What is WebSphere Liberty?", attendee1.getId(), 10));
        SessionRating sr7 = rcu.rateSessionAndCheck(new SessionRating("What is WebSphere Liberty?", attendee2.getId(), 10));

        // Update a session
        SessionRating newSr5 = rcu.updateRating(new SessionRating(sr5.getId(), sr5.getSession(), sr5.getAttendeeId(), 6));
        assertEquals("Unexpected rating value from updated SessionRating", 6, newSr5.getRating());

        // Check rating for a given session
        List<SessionRating> ratingsForMicroprofile = rcu.listAllRatingsBySession("Microprofile: The Next Big Thing");
        assertEquals("The returned list of Microprofile session ratings contains an unexpected number of entries", 2,
                     ratingsForMicroprofile.size());
        
        assertTrue("The returned ratings for the Microprofile session do not contain the expected ratings."
                , ratingsForMicroprofile.contains(sr1) && ratingsForMicroprofile.contains(sr2));

        // Check the average rating for a different session
        double avg = rcu.getAverageRatingsBySession("What's coming in Java EE 8?");
        assertEquals("Unexpected average rating for Java EE8 session", 7.5, avg, 0.0);

        // Check rating for a given attendee
        List<SessionRating> ratingsFromAttendee2 = rcu.listAllRatingsByAttendee(attendee2);
        assertEquals("The returned list of attendee2's session ratings contains an unexpected number of entries", 3,
                     ratingsFromAttendee2.size());
        assertTrue("The returned ratings from attendee2 do not contain the expected ratings",
                   ratingsFromAttendee2.contains(sr2) && ratingsFromAttendee2.contains(sr4) && ratingsFromAttendee2.contains(sr7));

    }
    
    @Test
    public void testDeployment() {
    	acu.checkRoot("Microservice Session Vote Application");
    }
    
}
