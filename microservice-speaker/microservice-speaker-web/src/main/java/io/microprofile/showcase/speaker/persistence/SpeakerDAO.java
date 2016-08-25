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
package io.microprofile.showcase.speaker.persistence;

import io.microprofile.showcase.speaker.domain.QVenue;
import io.microprofile.showcase.speaker.domain.Venue;
import io.microprofile.showcase.speaker.model.Speaker;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Speaker Data Access Object
 */
@ApplicationScoped
public class SpeakerDAO {

    private final HashMap<String, HashSet<Speaker>> speakers = new HashMap<>();

    @Inject
    @QVenue
    private Venue venue;

    @PostConstruct
    private void postConstruct() {
        this.speakers.put(this.venue.getName(), this.createSpeakers(this.venue.getName()));
    }

    private HashSet<Speaker> createSpeakers(final String venue) {
        final HashSet<Speaker> speakers = new HashSet<>();
        speakers.add(this.createSpeaker());
        return speakers;
    }

    private Speaker createSpeaker() {
        final Speaker s = new Speaker();
        s.setId(UUID.randomUUID().toString());
        s.setNameFirst("Sebastian");
        s.setNameLast("Daschner");
        s.setOrganization("Freelancer");
        s.setBiography("Sebastian Daschner is a Java freelancer working as a consultant/software developer/architect. He is enthusiastic about programming and Java EE. He is participating in the JCP, serving in the JSR 370 Expert Group, and hacking on various open source projects on Github. He is a Java Champion and has been working with Java for more than six years. In addition to Java, Daschner is also a heavy user of Linux and container technologies such as Docker. He evangelizes computer science practices on blog.sebastian-daschner.com and on Twitter via @DaschnerS. When not working with Java, Daschner also loves to travel the world, either by plane or motorbike.");
        s.setTwitterHandle("@DaschnerS");
        s.setPicture("https://www.oracle.com/us/assets/cw16-sebastian-daschner-3102339.png");
        return s;
    }

    public Optional<Set<Speaker>> getSpeakers() {

        return Optional.ofNullable(this.speakers.get(this.venue.getName()));
    }

    public Speaker persist(final Speaker speaker) {
        return null;
    }

    public void remove(final String id) {

    }

    public Speaker update(final Speaker speaker) {

        final Iterator<Speaker> iterator = this.speakers.get(this.venue.getName()).iterator();
        Speaker next = null;

        while (iterator.hasNext()) {
            next = iterator.next();
            if (next.getId().equals(speaker.getId())) {
                next.setPicture(speaker.getPicture());
                next.setBiography(speaker.getBiography());
                next.setOrganization(speaker.getOrganization());
                next.setNameFirst(speaker.getNameFirst());
                next.setNameLast(speaker.getNameLast());
                next.setTwitterHandle(speaker.getTwitterHandle());
                return next;
            }
        }
        return null;
    }

    public Optional<Speaker> getSpeaker(final String id) {
        return null;
    }

    public Optional<Set<Speaker>> find(final Speaker speaker) {
        return null;
    }
}
