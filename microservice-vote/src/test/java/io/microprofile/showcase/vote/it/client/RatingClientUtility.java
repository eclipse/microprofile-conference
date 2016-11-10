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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import io.microprofile.showcase.vote.api.SessionRatingListProvider;
import io.microprofile.showcase.vote.api.SessionRatingProvider;
import io.microprofile.showcase.vote.model.Attendee;
import io.microprofile.showcase.vote.model.SessionRating;

public class RatingClientUtility {

	private static String ROOT_URL = "http://localhost:" + System.getProperty("liberty.test.port") + "/" + System.getProperty("app.context.root");
    private static String RATE_URL = ROOT_URL + "/rate";
    private static String RATE_BY_SESSION_URL = ROOT_URL + "/ratingsBySession";
    private static String AVG_RATE_BY_SESSION_URL = ROOT_URL + "/averageRatingBySession";
    private static String RATE_BY_ATTENDEE_URL = ROOT_URL + "/ratingsByAttendee";

    private final Client ratingClient;

    public RatingClientUtility() {
        ratingClient = ClientBuilder.newBuilder().build();
        ratingClient.register(SessionRatingProvider.class);
        ratingClient.register(SessionRatingListProvider.class);
    }
    
    public void deleteAllRatings() {
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
        String string = r.readEntity(String.class);
        Double d = Double.parseDouble(string);
        return d;
    }

    public List<SessionRating> listAllRatingsByAttendee(Attendee attendee) throws IOException {
        UriBuilder uriBuilder = UriBuilder.fromPath(RATE_BY_ATTENDEE_URL).queryParam("attendeeId", attendee.getId());
        Response r = ratingClient.target(uriBuilder).request(MediaType.APPLICATION_JSON).get();
        @SuppressWarnings("unchecked")
        List<SessionRating> ratings = r.readEntity(List.class);
        return ratings;
    }

    public SessionRating rateSessionAndCheck(SessionRating rating) {
        SessionRating returnedRating = rateSession(rating);
        assertEquals("Unexpected session from returned SessionRating", rating.getSession(), returnedRating.getSession());
        assertEquals("Unexpected attendee ID from returned SessionRating", rating.getAttendeeId(), returnedRating.getAttendeeId());
        assertEquals("Unexpected rating value from returned SessionRating", rating.getRating(), returnedRating.getRating());
        return returnedRating;
    }

}
