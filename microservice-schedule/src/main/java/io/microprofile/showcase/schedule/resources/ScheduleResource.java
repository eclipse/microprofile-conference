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

import io.microprofile.showcase.schedule.model.Schedule;
import io.microprofile.showcase.schedule.persistence.ScheduleDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/")
@RequestScoped
@Produces("application/json")
public class ScheduleResource {

    @Inject
    private ScheduleDAO scheduleDAO;

    @POST
    @Consumes("application/json")
    public Response add(final Schedule schedule) {
        final Schedule created = scheduleDAO.addSchedule(schedule);
        return Response.created(URI.create("/" + created.getId()))
                .entity(created)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response retrieve(@PathParam("id") final String id) {
        return scheduleDAO.findById(id)
                .map(schedule -> Response.ok(schedule).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/all")
    public Response allSchedules() {
        final List<Schedule> allSchedules = scheduleDAO.getAllSchedules();
        final GenericEntity<List<Schedule>> entity = buildEntity(allSchedules);
        return Response.ok(entity).build();
    }

    @GET
    @Path("/venue/{id}")
    public Response allForVenue(@PathParam("id") final String id) {
        final List<Schedule> schedulesByVenue = scheduleDAO.findByVenue(id);
        final GenericEntity<List<Schedule>> entity = buildEntity(schedulesByVenue);
        return Response.ok(entity).build();
    }

    @GET
    @Path("/active/{dateTime}")
    public Response activeAtDate(@PathParam("dateTime") final String dateTimeString) {
        final LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        final List<Schedule> schedulesByDate = scheduleDAO.findByDate(dateTime.toLocalDate());
        final List<Schedule> activeAtTime = schedulesByDate.stream()
                .filter(schedule -> isTimeInSchedule(dateTime.toLocalTime(), schedule))
                .collect(Collectors.toList());
        final GenericEntity<List<Schedule>> entity = buildEntity(activeAtTime);
        return Response.ok(entity).build();
    }

    @GET
    @Path("/all/{date}")
    public Response allForDay(@PathParam("date") final String dateString) {
        final LocalDate date = LocalDate.parse(dateString);
        final List<Schedule> schedulesByDate = scheduleDAO.findByDate(date);
        final GenericEntity<List<Schedule>> entity = buildEntity(schedulesByDate);
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{scheduleId}")
    public Response remove(@PathParam("scheduleId") final String scheduleId) {
        scheduleDAO.deleteSchedule(scheduleId);
        return Response.noContent().build();
    }

    private GenericEntity<List<Schedule>> buildEntity(final List<Schedule> scheduleList) {
        return new GenericEntity<List<Schedule>>(scheduleList) {
        };
    }

    private boolean isTimeInSchedule(final LocalTime currentTime, final Schedule schedule) {
        final LocalTime scheduleStartTime = schedule.getStartTime();
        final LocalTime scheduleEndTime = scheduleStartTime.plus(schedule.getDuration());
        return scheduleStartTime.isBefore(currentTime) &&
                scheduleEndTime.isAfter(currentTime);
    }
}
