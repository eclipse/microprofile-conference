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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.microprofile.showcase.vote.api.AttendeeListProvider;
import io.microprofile.showcase.vote.api.AttendeeProvider;
import io.microprofile.showcase.vote.api.SessionRatingListProvider;
import io.microprofile.showcase.vote.api.SessionRatingProvider;
import io.microprofile.showcase.vote.model.Attendee;
import io.microprofile.showcase.vote.model.SessionRating;

public class VoteClient {

    //private static String ROOT_URL = "http://localhost:" + System.getProperty("liberty.test.port") + "/vote";
    private static String ROOT_URL = "http://localhost:9130/vote";
    private static String ATTENDEE_URL = ROOT_URL + "/attendee";
    private static String RATE_URL = ROOT_URL + "/rate";
    private static String RATE_BY_SESSION_URL = ROOT_URL + "/ratingsBySession";
    private static String AVG_RATE_BY_SESSION_URL = ROOT_URL + "/averageRatingBySession";
    private static String RATE_BY_ATTENDEE_URL = ROOT_URL + "/ratingsByAttendee";

    private final Client attendeeClient;
    private final Client ratingClient;

    public VoteClient() {
        attendeeClient = ClientBuilder.newBuilder().build();
        attendeeClient.register(AttendeeProvider.class);
        attendeeClient.register(AttendeeListProvider.class);
        ratingClient = ClientBuilder.newBuilder().build();
        ratingClient.register(SessionRatingProvider.class);
        ratingClient.register(SessionRatingListProvider.class);
    }
    
    @Before
    public void clearDatabase() {
        deleteAllAttendees();
        deleteAllRatings();
    }
    
    private void deleteAllAttendees() {
        UriBuilder uriBuilder = UriBuilder.fromPath(ATTENDEE_URL);
        Response response = attendeeClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).get();
        @SuppressWarnings("unchecked")
        List<Attendee> attendees = response.readEntity(List.class);
        List<String> attendeeIds = new ArrayList<String>();
        for (Attendee attendee : attendees) {
            attendeeIds.add(attendee.getId());
        }
        for (String id : attendeeIds) {
            deleteAttendee(id);
        }
    }
    
    private void deleteAttendee(String attendeeId) {
        UriBuilder uriBuilder = UriBuilder.fromPath(ATTENDEE_URL + "/" + attendeeId);
        Response response = attendeeClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).delete();
        assertEquals("Deletion of attendee " + attendeeId + " failed.", 204, response.getStatus());
    }
    
    private void deleteAllRatings() {
        UriBuilder uriBuilder = UriBuilder.fromPath(RATE_URL);
        Response response = ratingClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).get();
        @SuppressWarnings("unchecked")
        List<SessionRating> ratings = response.readEntity(List.class);
        List<String> ratingIds = new ArrayList<String>();
        for (SessionRating rating : ratings) {
            ratingIds.add(rating.getId());
        }
        for (String id : ratingIds) {
            deleteRating(id);
        }
    }
    
    private void deleteRating(String ratingId) {
        UriBuilder uriBuilder = UriBuilder.fromPath(RATE_URL + "/" + ratingId);
        Response response = ratingClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).delete();
        assertEquals("Deletion of attendee " + ratingId + " failed.", 204, response.getStatus());
    }

    private void checkResponseStatus(Response response) {
        assertEquals("Response status was " + response.getStatus(), 200, response.getStatus());
    }

    public Attendee registerAttendee(String name) {
        UriBuilder uriBuilder = UriBuilder.fromPath(ATTENDEE_URL);
        JsonObject jsonName = Json.createObjectBuilder().add("name", name).build();
        Response r = attendeeClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).post(Entity.json(jsonName.toString()));
        checkResponseStatus(r);
        Attendee registeredAttendee = r.readEntity(Attendee.class);
        return registeredAttendee;
    }

    public Attendee updateAttendee(Attendee attendee) {
        UriBuilder uriBuilder = UriBuilder.fromPath(ATTENDEE_URL + "/" + attendee.getId());
        Response r = attendeeClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).put(Entity.json(attendee));
        Attendee updatedAttendee = r.readEntity(Attendee.class);
        return updatedAttendee;
    }

    public SessionRating rateSession(SessionRating rating) {
        UriBuilder uriBuilder = UriBuilder.fromPath(RATE_URL);
        Response r = ratingClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).post(Entity.json(rating));
        SessionRating updatedRating = r.readEntity(SessionRating.class);
        return updatedRating;
    }

    public SessionRating updateRating(SessionRating rating) {
        UriBuilder uriBuilder = UriBuilder.fromPath(RATE_URL + "/" + rating.getId());
        Response r = ratingClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).put(Entity.json(rating));
        SessionRating updatedRating = r.readEntity(SessionRating.class);
        return updatedRating;
    }

    public List<SessionRating> listAllRatingsBySession(String session) throws IOException {
        UriBuilder uriBuilder = UriBuilder.fromPath(RATE_BY_SESSION_URL).queryParam("sessionId", session);
        Response r = ratingClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).get();
        @SuppressWarnings("unchecked")
        List<SessionRating> ratings = r.readEntity(List.class);
        return ratings;
    }

    public double getAverageRatingsBySession(String session) throws IOException {
        UriBuilder uriBuilder = UriBuilder.fromPath(AVG_RATE_BY_SESSION_URL).queryParam("sessionId", session);
        Response r = ratingClient.target(uriBuilder).request().get();
        double d = r.readEntity(Double.class);
        return d;
    }

    public List<SessionRating> listAllRatingsByAttendee(Attendee attendee) throws IOException {
        UriBuilder uriBuilder = UriBuilder.fromPath(RATE_BY_ATTENDEE_URL).queryParam("attendeeId", attendee.getId());
        Response r = ratingClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).get();
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
        assertEquals("Unexpected change of ID when updating attendee name", originalAttendee.getId(), updatedAttendee.getId());
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
        SessionRating newSr5 = updateRating(new SessionRating(sr5.getId(), sr5.getRevision(), sr5.getSession(), sr5.getAttendeeId(), 6));
        assertEquals("Unexpected rating value from updated SessionRating", 6, newSr5.getRating());

        // Check rating for a given session
        List<SessionRating> ratingsForMicroprofile = listAllRatingsBySession("Microprofile: The Next Big Thing");
        assertEquals("The returned list of Microprofile session ratings contains an unexpected number of entries", 2,
                     ratingsForMicroprofile.size());
        boolean foundSr1 = false;
        boolean foundSr2 = false;
        for (SessionRating rating : ratingsForMicroprofile) {
            if (rating.equals(sr1)) {
                foundSr1 = true;
            } else if (rating.equals(sr2)) {
                foundSr2 = true;
            } else {
                System.out.println("Rating not found:" + sr1.toString());
                System.out.println("Rating not found:" + sr2.toString());
                Assert.fail("Unexpected rating found:" + rating.toString());
            }
        }
        assertTrue("The returned ratings for the Microprofile session do not contain the expected ratings."
                , foundSr1 && foundSr2);

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
