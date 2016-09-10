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
import io.swagger.annotations.Api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/")
@Api(description = "Schedule REST Endpoint")
@RequestScoped
public class ScheduleResource {

    @Inject
    private ScheduleDAO scheduleDAO;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response add(Schedule schedule) {
        Schedule created = scheduleDAO.addSchedule(schedule);
        return Response.created(URI.create("/" + created.getId()))
                        .entity(created)
                        .build();
    }

    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response retrieve(@PathParam("id") Long id) {
        return scheduleDAO.findById(id)
            .map(schedule -> Response.ok(schedule).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Produces("application/json")
    public Response allForVenue(@QueryParam("venue") String venue) {
        List<Schedule> schedulesByVenue = scheduleDAO.findByVenue(venue);
        GenericEntity<List<Schedule>> entity = buildEntity(schedulesByVenue);
        return Response.ok(entity).build();
    }

//    public Response activeAtDate(@QueryParam("time") String dateTime) {

//    }
//
//    public Response allForDay(@QueryParam("day") String date) {
//
//    }
//

    private GenericEntity<List<Schedule>> buildEntity(final List<Schedule> scheduleList) {
        return new GenericEntity<List<Schedule>>(scheduleList) {};
    }
}
