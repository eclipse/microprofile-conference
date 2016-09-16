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
import io.microprofile.showcase.speaker.domain.VenueList;
import io.microprofile.showcase.speaker.model.Speaker;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Speaker Data Access Object
 */
@ApplicationScoped
public class SpeakerDAO {

    private final HashMap<String, Set<Speaker>> speakers = new HashMap<>();

    @Inject
    @VenueList
    @Named(value = "venueList")
    private List<Venue> venues;

    @PostConstruct
    private void postConstruct() {

        for (final Venue venue : this.venues) {
            this.speakers.put(venue.getName(), venue.getSpeakers());
        }

    }

    /**
     * Get all known Speakers
     *
     * @return Set of Speakers
     */
    public Set<Speaker> getSpeakers() {

        final Set<Speaker> speakers = new HashSet<>();

        this.speakers.values().forEach(speakers::addAll);

        return speakers;
    }

    /**
     * Persist the given speaker
     *
     * @param speaker Speaker to store
     * @return Speaker seeded with new ID
     */
    public Speaker persist(final Speaker speaker) {

        speaker.setId(UUID.randomUUID().toString());

        //TODO - Venue based
        this.speakers.get(this.speakers.keySet().iterator().next()).add(speaker);

        return speaker;
    }

    /**
     * Remove the Speaker for the given ID.
     * Fails silently if the ID is not found.
     *
     * @param id Valid ID
     */
    public void remove(final String id) {
        for (final Map.Entry<String, Set<Speaker>> entry : this.speakers.entrySet()) {
            final Iterator<Speaker> it = entry.getValue().iterator();
            while (it.hasNext()) {
                if (it.next().getId().equals(id)) {
                    it.remove();
                    return;
                }
            }
        }
    }

    /**
     * Update a speaker if found
     *
     * @param speaker Speaker to update
     * @return The found Speaker or null
     */
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

    /**
     * Get the Speaker that matches the specified ID, or empty.
     *
     * @param id Valid ID
     * @return Optional Speaker
     */
    public Optional<Speaker> getSpeaker(final String id) {

        for (final Map.Entry<String, Set<Speaker>> entry : this.speakers.entrySet()) {
            for (final Speaker speaker : entry.getValue()) {
                if (speaker.getId().equals(id)) {
                    return Optional.of(speaker);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Try and fuzzy find the specified Speaker
     *
     * @param speaker Speaker to find - may contain partial details
     * @return Optional matching speakers
     */
    public Optional<Set<Speaker>> find(final Speaker speaker) {

        final ArrayList<Speaker> speakers = new ArrayList<>();

        for (final Map.Entry<String, Set<Speaker>> entry : this.speakers.entrySet()) {
            speakers.addAll(entry.getValue());
        }

        CollectionUtils.filter(speakers, object -> {
            final Speaker find = Speaker.class.cast(object);
            return (
                    isMatch(find.getNameFirst(), speaker.getNameFirst()) ||
                            isMatch(find.getNameLast(), speaker.getNameLast()) ||
                            isMatch(find.getOrganization(), speaker.getOrganization()) ||
                            isMatch(find.getTwitterHandle(), speaker.getTwitterHandle()));
        });

        if (!speakers.isEmpty()) {
            final Set<Speaker> result = new HashSet<>(speakers.size());
            result.addAll(speakers);
            return Optional.of(result);
        }


        return Optional.empty();
    }

    /**
     * Really simple fuzzy match using JaroWinklerDistance
     *
     * @param left  String to match
     * @param right String to compare
     * @return True is match is gt 0.85
     */
    private static boolean isMatch(String left, String right) {

        //No empty strings
        left = null != left ? left : "";
        right = null != right ? right : "";

        return StringUtils.getJaroWinklerDistance(left, right) > 0.85;
    }
}
