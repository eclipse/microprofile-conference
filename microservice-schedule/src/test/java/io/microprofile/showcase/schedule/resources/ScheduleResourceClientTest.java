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
import io.microprofile.showcase.schedule.model.Session;
import io.microprofile.showcase.schedule.model.Venue;
import io.microprofile.showcase.schedule.persistence.ScheduleDAO;
import io.microprofile.showcase.schedule.rest.Application;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
@RunAsClient
public class ScheduleResourceClientTest {

    private static final Session TEST_SESSION = new Session("MicroProfile", "Joe Black");
    private static final Venue TEST_VENUE = new Venue("Metropolis");

    @ArquillianResource
    private URL base;

    private static Long scheduleId;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "schedule-microservice.war")
                        .addPackage(Schedule.class.getPackage())
                        .addClasses(ScheduleResource.class, ScheduleDAO.class, Application.class);
    }

    @Test
    @InSequence(1)
    public void shouldCreateSchedule() throws Exception {
        URL url = new URL(base, "schedule");
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Schedule schedule = new Schedule(TEST_SESSION, TEST_VENUE, LocalDate.now(), LocalTime.now(), Duration.ofHours(1L));
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(schedule, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(201, response.getStatus());
        String location = response.getHeaderString("location");
        assertNotNull(location);
        scheduleId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
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
        assertEquals(TEST_SESSION.getTitle(), jsonObject.getJsonObject("session").getString("title"));
        assertEquals(TEST_VENUE.getName(), jsonObject.getJsonObject("venue").getString("name"));
    }

    private static JsonObject readJsonContent(Response response) {
        String competitionJson = response.readEntity(String.class);
        StringReader stringReader = new StringReader(competitionJson);
        JsonReader jsonReader = Json.createReader(stringReader);
        return jsonReader.readObject();
    }
}
