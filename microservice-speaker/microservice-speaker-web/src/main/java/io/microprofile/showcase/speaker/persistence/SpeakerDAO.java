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

import io.microprofile.showcase.speaker.domain.Venue;
import io.microprofile.showcase.speaker.model.Speaker;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Speaker Data Access Object
 */
@ApplicationScoped
public class SpeakerDAO {

    private final HashMap<String, Set<Speaker>> speakers = new HashMap<>();

    @Inject
    private List<Venue> venues;

    @PostConstruct
    private void postConstruct() {

        for (final Venue venue : this.venues) {
            this.speakers.put(venue.getName(), venue.getSpeakers());
        }

    }

    public Set<Speaker> getSpeakers() {

        final Set<Speaker> speakers = new HashSet<>();

        this.speakers.values().forEach(speakers::addAll);

        return speakers;
    }

    public Speaker persist(final Speaker speaker) {
        return null;
    }

    public void remove(final String id) {

    }

    public Speaker update(final Speaker speaker) {

        final Set<Speaker> speakers = this.getSpeakers();
        for (final Speaker s : speakers) {
            if (s.getId().equals(speaker.getId())) {
                s.setPicture(speaker.getPicture());
                s.setBiography(speaker.getBiography());
                s.setOrganization(speaker.getOrganization());
                s.setNameFirst(speaker.getNameFirst());
                s.setNameLast(speaker.getNameLast());
                s.setTwitterHandle(speaker.getTwitterHandle());
                return s;
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
