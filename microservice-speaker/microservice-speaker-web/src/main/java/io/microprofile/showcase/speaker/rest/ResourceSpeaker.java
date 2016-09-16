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
package io.microprofile.showcase.speaker.rest;


import io.microprofile.showcase.speaker.model.Speaker;
import io.microprofile.showcase.speaker.persistence.SpeakerDAO;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * The Speaker resource
 */
@ApplicationScoped
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class ResourceSpeaker {

    @Inject
    private SpeakerDAO speakerDAO;

    @GET
    public Collection<Speaker> retrieveAll() {
        return this.speakerDAO.getSpeakers();
    }

    @POST
    @Lock(LockType.WRITE)
    @Path("/add")
    public Speaker add(final Speaker speaker) {
        return this.speakerDAO.persist(speaker);
    }

    @DELETE
    @Path("/remove/{id}")
    public void remove(@PathParam("id") final String id) {
        this.speakerDAO.remove(id);
    }

    @PUT
    @Lock(LockType.WRITE)
    @Path("/update")
    public Speaker update(final Speaker speaker) {
        return this.speakerDAO.update(speaker);
    }

    @GET
    @Path("/retrieve/{id}")
    public Speaker retrieve(@PathParam("id") final String id) {
        return this.speakerDAO.getSpeaker(id).orElse(new Speaker());
    }

    @PUT
    @Path("/search")
    public Set<Speaker> search(final Speaker speaker) {
        return this.speakerDAO.find(speaker).orElse(Collections.emptyNavigableSet());
    }
}
