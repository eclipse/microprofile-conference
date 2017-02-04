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

import io.microprofile.showcase.speaker.domain.ProducerVenue;
import io.microprofile.showcase.speaker.domain.Venue;
import io.microprofile.showcase.speaker.domain.VenueJavaOne2016;
import io.microprofile.showcase.speaker.model.Speaker;
import io.microprofile.showcase.speaker.persistence.SpeakerDAO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.net.URL;
import java.util.Set;
import java.util.logging.Logger;

@RunWith(Arquillian.class)
public class ResourceSpeakerTest {

    private final Logger log = Logger.getLogger(ResourceSpeakerTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive deploy() {

        final File bootstrapLib = Maven.resolver().resolve("io.microprofile.showcase:demo-bootstrap:1.0.0-SNAPSHOT").withoutTransitivity().asSingleFile();

        return ShrinkWrap.create(WebArchive.class
                , ResourceSpeakerTest.class.getName() + ".war")
                .addClasses(
                        SpeakerDAO.class,
                        ResourceSpeaker.class,
                        Application.class,
                        Venue.class,
                        VenueJavaOne2016.class,
                        ProducerVenue.class,
                        Speaker.class
                )
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(
                        bootstrapLib
                );
    }

    @ArquillianResource
    private URL url;

    @Test
    @RunAsClient
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
    @RunAsClient
    public void testSearch() {

        final Speaker search = new Speaker();
        search.setNameFirst("Ar");
        search.setNameLast("G");

        final Set<Speaker> speakers = this.getWebTarget("speaker/search").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(search))
                .readEntity(new GenericType<Set<Speaker>>() {
                });

        Assert.assertFalse(speakers.isEmpty());

        boolean foundArun = false;

        for (final Speaker speaker : speakers) {
            this.log.info("Found: " + speaker.getNameFirst() + " " + speaker.getNameLast());
            if ("Arun".equals(speaker.getNameFirst()) && "Gupta".equals(speaker.getNameLast())) {
                foundArun = true;
            }
        }

        Assert.assertTrue(foundArun);
    }

    @Test
    @RunAsClient
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
    @RunAsClient
    public void testUpdate() {

        final Speaker search = new Speaker();
        search.setNameFirst("Charlie");
        search.setNameLast("Hunt");

        final Set<Speaker> speakers = this.getWebTarget("speaker/search").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(search))
                .readEntity(new GenericType<Set<Speaker>>() {
                });

        final Speaker found = speakers.iterator().next();
        final String id = found.getId();

        Assert.assertEquals("Oracle", found.getOrganization());

        found.setOrganization("Oracle Corporation");

        this.getWebTarget("speaker/update").request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(found));

        final Speaker updated = this.getWebTarget("speaker/retrieve/{id}").resolveTemplate("id", id)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get()
                .readEntity(Speaker.class);

        Assert.assertEquals("Failed to update speaker", "Oracle Corporation", updated.getOrganization());
        this.log.info("Updated: " + updated.toString());
    }

    @Test
    @RunAsClient
    public void testRemove() {
        final Speaker search = new Speaker();
        search.setNameFirst("Markus");
        search.setNameLast("Eisele");

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
        return client.target(this.url.toExternalForm() + endpoint);
    }

}
