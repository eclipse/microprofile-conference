/*
 * Copyright(c) 2016-2017 IBM, Red Hat, and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.microprofile.showcase.schedule.resources;

import com.kumuluz.ee.EeApplication;
import io.microprofile.showcase.schedule.model.Schedule;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class ScheduleResourceClientIT {

    private static final String TEST_SESSION = String.valueOf(12);
    private static final String TEST_VENUE = "Metropolis";

    private URL base;

//    private static EeApplication eeApplication;

    private static String scheduleId;

//    @BeforeClass
//    public static void setUpClass() {
//        eeApplication = new EeApplication();
//    }
//
//    @AfterClass
//    public static void tearDownClass() {
//        eeApplication = null;
//    }

    @Before
    public void setup() {

        try {
            base = new URL("http://localhost:8080");
        } catch (MalformedURLException ignored) {
        }
    }

    @Test
    public void should1CreateSchedule() throws Exception {

        final Response response = createScheduledSession(TEST_SESSION, TEST_VENUE, String.valueOf(400), LocalDate.now(), LocalTime.now());
        assertEquals(201, response.getStatus());
        scheduleId = getScheduleId(response);
    }

    @Test
    public void should2GetAlreadyCreatedSchedule() throws Exception {
        final URL url = new URL(base, "schedule/" + scheduleId);
        final WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        final JsonObject jsonObject = readJsonContent(response);
        assertEquals(scheduleId, jsonObject.getString("id"));
        assertEquals(TEST_SESSION, jsonObject.getString("sessionId"));
        assertEquals(TEST_VENUE, jsonObject.getString("venue"));
    }

    @Test
    public void should3GetScheduledSessionsForVenue() throws Exception {
        final String microprofile = String.valueOf(20);
        final String javaeeNext = String.valueOf(40);
        final String payaraMicro = String.valueOf(60);

        createScheduledSession(microprofile, "Hilton", String.valueOf(500), LocalDate.now(), LocalTime.now());
        createScheduledSession(javaeeNext, "Moscone", String.valueOf(600), LocalDate.now(), LocalTime.now());
        createScheduledSession(payaraMicro, "Hilton", String.valueOf(500), LocalDate.now(), LocalTime.now());

        final URL url = new URL(base, "schedule/venue/500");
        final WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        final JsonArray jsonArray = readJsonArray(response);
        assertEquals(2, jsonArray.size());
    }

    @Test
    public void should4GetActiveScheduledSessions() throws Exception {
        final String webServices = String.valueOf(100);
        final String designPatterns = String.valueOf(120);
        final String java = String.valueOf(140);

        createScheduledSession(webServices, "Moscone", String.valueOf(600), LocalDate.of(1995, 9, 20), LocalTime.of(16, 0));
        createScheduledSession(designPatterns, "Moscone", String.valueOf(600), LocalDate.of(1995, 9, 20), LocalTime.of(17, 0));
        createScheduledSession(java, "Moscone", String.valueOf(600), LocalDate.of(1995, 9, 21), LocalTime.of(16, 0));

        final URL url = new URL(base, "schedule/active/" + LocalDateTime.of(1995, 9, 20, 17, 34, 29));
        final WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        final JsonArray jsonArray = readJsonArray(response);
        assertEquals(1, jsonArray.size());
    }

    @Test
    public void should5GetScheduledSessionsByDate() throws Exception {
        final URL url = new URL(base, "schedule/all/" + LocalDate.of(1995, 9, 20));
        final WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        final JsonArray jsonArray = readJsonArray(response);
        assertEquals(2, jsonArray.size());
    }

    @Test
    public void should6RemoveSchedule() throws Exception {
        final String removeMe = String.valueOf(200);
        final Response createResponse = createScheduledSession(removeMe, "Far far away", String.valueOf(666), LocalDate.now(), LocalTime.now());
        final String removeId = getScheduleId(createResponse);

        final URL url = new URL(base, "schedule/" + removeId);
        final WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        final Response deleteResponse = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
        assertEquals(204, deleteResponse.getStatus());

        final URL checkUrl = new URL(base, "schedule/" + removeId);
        final WebTarget checkTarget = ClientBuilder.newClient().target(checkUrl.toExternalForm());
        final Response checkResponse = checkTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(404, checkResponse.getStatus());
    }

    private String getScheduleId(final Response response) {
        final String location = response.getHeaderString("location");
        return location.substring(location.lastIndexOf("/") + 1);
    }

    private static JsonObject readJsonContent(final Response response) {
        final JsonReader jsonReader = readJsonStringFromResponse(response);
        return jsonReader.readObject();
    }

    private static JsonArray readJsonArray(final Response response) {
        final JsonReader jsonReader = readJsonStringFromResponse(response);
        return jsonReader.readArray();
    }

    private static JsonReader readJsonStringFromResponse(final Response response) {
        final String competitionJson = response.readEntity(String.class);
        final StringReader stringReader = new StringReader(competitionJson);
        return Json.createReader(stringReader);
    }

    private Response createScheduledSession(final String session, final String venue, final String venueId, final LocalDate date, final LocalTime time) throws MalformedURLException {
        final URL url = new URL(base, "schedule");
        final WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        final Schedule schedule = new Schedule(session, venue, venueId, date, time, Duration.ofHours(1L));
        return target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(schedule, MediaType.APPLICATION_JSON_TYPE));
    }
}
