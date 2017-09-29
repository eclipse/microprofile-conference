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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Logger;


public class ResourceSpeakerIT {

    private final Logger log = Logger.getLogger(ResourceSpeakerIT.class.getName());
    private URL base;

    @Before
    public void setup() {

        try {
            base = new URL("http://localhost:8080/");
        } catch (MalformedURLException ignored) {
        }
    }

    @Test
    public void testGet() {

        final Set<Speaker> speakers = this.getWebTarget("speaker")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get(new GenericType<Set<Speaker>>() {
            });

        Assert.assertFalse(speakers.isEmpty());

        for (final Speaker speaker : speakers) {
            this.log.info("Listed: " + speaker.getNameFirst() + " " + speaker.getNameLast());
        }
    }

    @Test
    public void testSearch() {

        final Speaker search = new Speaker();
        search.setNameFirst("Oct");
        search.setNameLast("O");

        final Set<Speaker> speakers = this.getWebTarget("speaker/search").request(MediaType.APPLICATION_JSON_TYPE)
            .put(Entity.json(search))
            .readEntity(new GenericType<Set<Speaker>>() {
            });

        Assert.assertFalse(speakers.isEmpty());

        boolean foundOctavia = false;

        for (final Speaker speaker : speakers) {
            this.log.info("Found: " + speaker.getNameFirst() + " " + speaker.getNameLast());
            if ("Octavia".equals(speaker.getNameFirst()) && "Olson".equals(speaker.getNameLast())) {
                foundOctavia = true;
            }
        }

        Assert.assertTrue(foundOctavia);
    }

    @Test
    public void testAddandRetrieve() {

        Speaker speaker = new Speaker();
        speaker.setNameFirst("Andy");
        speaker.setNameLast("Gumbrecht");
        speaker.setOrganization("Tomitribe");
        speaker.setTwitterHandle("@AndyGeeDe");
        speaker.setBiography("Some bloke");
        speaker.setPicture("https://pbs.twimg.com/profile_images/425313992689475584/KIrtgA86.jpeg");

        final Speaker added = this.getWebTarget("speaker/add").request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.json(speaker))
            .readEntity(Speaker.class);

        final String id = added.getId();
        Assert.assertNotNull(id);

        speaker = this.getWebTarget("speaker/retrieve/{id}").resolveTemplate("id", id)
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get()
            .readEntity(Speaker.class);

        Assert.assertEquals("Failed to get added speaker", "Gumbrecht", speaker.getNameLast());
        this.log.info("Added: " + speaker.toString());
    }

    @Test
    public void testUpdate() {

        final Speaker search = new Speaker();
        search.setNameFirst("Zena");
        search.setNameLast("Armstrong");

        final Set<Speaker> speakers = this.getWebTarget("speaker/search").request(MediaType.APPLICATION_JSON_TYPE)
            .put(Entity.json(search))
            .readEntity(new GenericType<Set<Speaker>>() {
            });

        final Speaker found = speakers.iterator().next();
        final String id = found.getId();

        Assert.assertEquals("Erat Nonummy Ultricies Incorporated", found.getOrganization());

        found.setOrganization("Erat Corporation");

        this.getWebTarget("speaker/update").request(MediaType.APPLICATION_JSON_TYPE)
            .put(Entity.json(found));

        final Speaker updated = this.getWebTarget("speaker/retrieve/{id}").resolveTemplate("id", id)
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get()
            .readEntity(Speaker.class);

        Assert.assertEquals("Failed to update speaker", "Erat Corporation", updated.getOrganization());
        this.log.info("Updated: " + updated.toString());
    }

    @Test
    public void testRemove() {
        final Speaker search = new Speaker();
        search.setNameFirst("Brent");
        search.setNameLast("Collins");

        final Set<Speaker> speakers = this.getWebTarget("speaker/search").request(MediaType.APPLICATION_JSON_TYPE)
            .put(Entity.json(search))
            .readEntity(new GenericType<Set<Speaker>>() {
            });

        final Speaker found = speakers.iterator().next();
        final String id = found.getId();

        this.getWebTarget("speaker/remove/{id}").resolveTemplate("id", id)
            .request(MediaType.APPLICATION_JSON_TYPE)
            .delete();

        final Speaker updated = this.getWebTarget("speaker/retrieve/{id}").resolveTemplate("id", id)
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get()
            .readEntity(Speaker.class);

        Assert.assertNull("Found unexpected response", updated.getId());
    }

    private WebTarget getWebTarget(final String endpoint) {
        final Client client = ClientBuilder.newBuilder().build();
        return client.target(this.base.toExternalForm() + endpoint);
    }

}
