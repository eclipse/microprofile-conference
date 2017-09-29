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
package io.microprofile.showcase.speaker.rest;


import io.microprofile.showcase.speaker.model.Speaker;
import io.microprofile.showcase.speaker.persistence.SpeakerDAO;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
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

    @Context
    private UriInfo uriInfo;

    @GET
    public Collection<Speaker> retrieveAll() {
        final Collection<Speaker> speakers = this.speakerDAO.getSpeakers();

        speakers.forEach(this::addHyperMedia);

        return speakers;
    }

    @POST
    @Path("/add")
    public Speaker add(final Speaker speaker) {
        return this.addHyperMedia(this.speakerDAO.persist(speaker));
    }

    @DELETE
    @Path("/remove/{id}")
    public void remove(@PathParam("id") final String id) {
        this.speakerDAO.remove(id);
    }

    @PUT
    @Path("/update")
    public Speaker update(final Speaker speaker) {
        return this.addHyperMedia(this.speakerDAO.update(speaker));
    }

    @GET
    @Path("/retrieve/{id}")
    public Speaker retrieve(@PathParam("id") final String id) {
        return this.addHyperMedia(this.speakerDAO.getSpeaker(id).orElse(new Speaker()));
    }

    @PUT
    @Path("/search")
    public Set<Speaker> search(final Speaker speaker) {
        final Set<Speaker> speakers = this.speakerDAO.find(speaker);

        speakers.forEach(this::addHyperMedia);

        return speakers;
    }

    private Speaker addHyperMedia(final Speaker s) {

        if (null != s) {

            if (null != s.getId()) {
                s.getLinks().put("self", this.getUri(s, "retrieve"));
                s.getLinks().put("remove", this.getUri(s, "remove"));
                s.getLinks().put("update", this.getUri("update"));
            }

            s.getLinks().put("add", this.getUri("add"));
            s.getLinks().put("search", this.getUri("search"));
        }

        return s;
    }

    private URI getUri(final Speaker s, final String path) {
        return this.uriInfo.getBaseUriBuilder().path(ResourceSpeaker.class).path(ResourceSpeaker.class, path).build(s.getId());
    }

    private URI getUri(final String path) {
        return this.uriInfo.getBaseUriBuilder().path(ResourceSpeaker.class).build(path);
    }
}
