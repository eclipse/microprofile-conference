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
package io.microprofile.showcase.schedule.resources;

import io.microprofile.showcase.schedule.model.Schedule;
import io.microprofile.showcase.schedule.persistence.ScheduleDAO;
import io.microprofile.showcase.schedule.rest.Application;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class ScheduleResourceClientTest {

    private static final Long TEST_SESSION = new Long(12);
    private static final String TEST_VENUE = "Metropolis";

    @ArquillianResource
    private URL base;

    private static Long scheduleId;

    @Deployment
    public static WebArchive createDeployment() {

        File bootstrapLib = Maven.resolver().resolve("io.microprofile.showcase:demo-bootstrap:1.0.0-SNAPSHOT").withoutTransitivity().asSingleFile();

        return ShrinkWrap.create(WebArchive.class, "schedule-microservice.war")
                        .addPackage(Schedule.class.getPackage())
                        .addClasses(ScheduleResource.class, ScheduleDAO.class, Application.class)
            .addAsLibraries(bootstrapLib);
    }

    @Test
    @InSequence(1)
    public void shouldCreateSchedule() throws Exception {
        Response response = createScheduledSession(TEST_SESSION, TEST_VENUE, 400l, LocalDate.now(), LocalTime.now());
        assertEquals(201, response.getStatus());
        scheduleId = getScheduleId(response);
    }

    @Test
    @InSequence(2)
    public void shouldGetAlreadyCreatedSchedule() throws Exception {
        URL url = new URL(base, "schedule/" + scheduleId);
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals(scheduleId.intValue(), jsonObject.getInt("id"));
        assertEquals(TEST_SESSION, new Long(jsonObject.getJsonNumber("sessionId").longValue()));
        assertEquals(TEST_VENUE, jsonObject.getString("venue"));
    }

    @Test
    @InSequence(3)
    public void shouldGetScheduledSessionsForVenue() throws Exception {
        Long microprofile = new Long(20);
        Long javaeeNext = new Long(40);
        Long payaraMicro = new Long(60);

        createScheduledSession(microprofile, "Hilton", 500l, LocalDate.now(), LocalTime.now());
        createScheduledSession(javaeeNext, "Moscone", 600l, LocalDate.now(), LocalTime.now());
        createScheduledSession(payaraMicro, "Hilton", 500l, LocalDate.now(), LocalTime.now());

        URL url = new URL(base, "schedule/venue/500");
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonArray jsonArray = readJsonArray(response);
        assertEquals(2, jsonArray.size());
    }

    @Test
    @InSequence(4)
    public void shouldGetActiveScheduledSessions() throws Exception {
        Long webServices = new Long(100);
        Long designPatterns = new Long(120);
        Long java = new Long(140);

        createScheduledSession(webServices, "Moscone", 600l, LocalDate.of(1995, 9, 20), LocalTime.of(16, 0));
        createScheduledSession(designPatterns, "Moscone", 600l, LocalDate.of(1995, 9, 20), LocalTime.of(17, 0));
        createScheduledSession(java, "Moscone", 600l, LocalDate.of(1995, 9, 21), LocalTime.of(16, 0));

        URL url = new URL(base, "schedule/active/" + LocalDateTime.of(1995, 9, 20, 17, 34, 29));
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonArray jsonArray = readJsonArray(response);
        assertEquals(1, jsonArray.size());
    }

    @Test
    @InSequence(5)
    public void shouldGetScheduledSessionsByDate() throws Exception {
        URL url = new URL(base, "schedule/all/" + LocalDate.of(1995, 9, 20));
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonArray jsonArray = readJsonArray(response);
        assertEquals(2, jsonArray.size());
    }

    @Test
    @InSequence(6)
    public void shouldRemoveSchedule() throws Exception {
        Long removeMe = new Long(200);
        Response createResponse = createScheduledSession(removeMe, "Far far away", 666l, LocalDate.now(), LocalTime.now());
        Long removeId = getScheduleId(createResponse);

        URL url = new URL(base, "schedule/" + removeId);
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Response deleteResponse = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
        assertEquals(204, deleteResponse.getStatus());

        URL checkUrl = new URL(base, "schedule/" + removeId);
        WebTarget checkTarget = ClientBuilder.newClient().target(checkUrl.toExternalForm());
        Response checkResponse = checkTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(404, checkResponse.getStatus());
    }

    private long getScheduleId(Response response) {
        String location = response.getHeaderString("location");
        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }

    private static JsonObject readJsonContent(Response response) {
        JsonReader jsonReader = readJsonStringFromResponse(response);
        return jsonReader.readObject();
    }

    private static JsonArray readJsonArray(Response response) {
        JsonReader jsonReader = readJsonStringFromResponse(response);
        return jsonReader.readArray();
    }

    private static JsonReader readJsonStringFromResponse(Response response) {
        String competitionJson = response.readEntity(String.class);
        StringReader stringReader = new StringReader(competitionJson);
        return Json.createReader(stringReader);
    }

    private Response createScheduledSession(Long session, String venue, Long venueId,  LocalDate date, LocalTime time) throws MalformedURLException {
        URL url = new URL(base, "schedule");
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Schedule schedule = new Schedule(session, venue, venueId, date, time, Duration.ofHours(1L));
        return target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(schedule, MediaType.APPLICATION_JSON_TYPE));
    }
}
