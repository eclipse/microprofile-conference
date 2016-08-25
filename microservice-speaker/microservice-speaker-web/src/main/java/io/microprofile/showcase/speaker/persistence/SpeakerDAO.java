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
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Speaker Data Access Object
 */
@ApplicationScoped
public class SpeakerDAO {

    private final HashMap<String, Set<Speaker>> speakers = new HashMap<>();

    @Inject
    @QVenue
    private Venue venue;

    @PostConstruct
    private void postConstruct() {
        this.speakers.put(this.venue.getName(), this.venue.getSpeakers());
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
