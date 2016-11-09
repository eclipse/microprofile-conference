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
import static org.junit.Assert.assertTrue;

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

import io.microprofile.showcase.vote.api.AttendeeListProvider;
import io.microprofile.showcase.vote.api.AttendeeProvider;
import io.microprofile.showcase.vote.model.Attendee;

public class AttendeeClientUtility {

	private static String ROOT_URL = "http://localhost:" + System.getProperty("liberty.test.port") + "/" + System.getProperty("app.context.root");
    private static String ATTENDEE_URL = ROOT_URL + "/attendee";

    private final Client attendeeClient;

    public AttendeeClientUtility() {
        attendeeClient = ClientBuilder.newBuilder().build();
        attendeeClient.register(AttendeeProvider.class);
        attendeeClient.register(AttendeeListProvider.class);
    }
    
    public void deleteAllAttendees() {
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


	public void checkRoot(String expectedOutput) {
		UriBuilder uriBuilder = UriBuilder.fromPath(ROOT_URL);
		Response response = attendeeClient.target(uriBuilder).request().get();
        String responseString = response.readEntity(String.class);
        response.close();
        assertTrue("Incorrect response, response is " + responseString, responseString.contains(expectedOutput));
	}
}
