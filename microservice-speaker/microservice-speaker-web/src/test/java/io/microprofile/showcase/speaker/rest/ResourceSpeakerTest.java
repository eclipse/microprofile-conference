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
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.net.URL;
import java.util.Set;

@RunWith(Arquillian.class)
public class ResourceSpeakerTest {

    @Deployment(testable = false)
    public static WebArchive deploy() {

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
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void resourceTest(@ArquillianResource final URL url) {

        final Client client = ClientBuilder.newBuilder().build();
        final WebTarget target = client.target(url.toExternalForm() + "speaker");
        final Set<Speaker> speakers = target.request().get(new GenericType<Set<Speaker>>() {
        });

        for (final Speaker speaker : speakers) {
            System.out.println(speaker.getNameFirst() + " " + speaker.getNameLast());
        }
    }

}
