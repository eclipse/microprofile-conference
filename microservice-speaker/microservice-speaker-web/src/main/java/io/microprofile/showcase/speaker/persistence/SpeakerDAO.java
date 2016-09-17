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

import io.microprofile.showcase.bootstrap.BootstrapData;
import io.microprofile.showcase.speaker.model.Speaker;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Speaker Data Access Object
 */
@ApplicationScoped
public class SpeakerDAO {

    @Inject
    BootstrapData bootstrapData;

    private final HashMap<String, Speaker> speakers = new HashMap<>();

    @PostConstruct
    private void initStore() {
        System.out.println("Initialise speaker DAO from bootstrap data");

        this.bootstrapData.getSpeaker()
                .forEach(bootstrap -> {

                    final String id = String.valueOf(bootstrap.getId());
                    final String[] names = bootstrap.getFullName().split(" ");
                    final Speaker sp = new Speaker();
                    sp.setId(id);
                    sp.setNameFirst(names[0]);
                    sp.setNameLast(names[1]);
                    sp.setOrganization(bootstrap.getCompany());
                    sp.setBiography(bootstrap.getJobTitle());
                    this.speakers.put(id, sp);
                });

    }

    /**
     * Get all known Speakers
     *
     * @return Set of Speakers
     */
    public Collection<Speaker> getSpeakers() {

        return this.speakers.values();
    }

    /**
     * Persist the given speaker
     *
     * @param speaker Speaker to store
     * @return Speaker seeded with new ID
     */
    public Speaker persist(final Speaker speaker) {

        final String id = UUID.randomUUID().toString();
        speaker.setId(id);

        this.speakers.put(id, speaker);

        return speaker;
    }

    /**
     * Remove the Speaker for the given ID.
     * Fails silently if the ID is not found.
     *
     * @param id Valid ID
     */
    public void remove(final String id) {
        this.speakers.remove(id);
    }

    /**
     * Update a speaker if found
     *
     * @param speaker Speaker to update
     * @return The found Speaker or null
     */
    public Speaker update(final Speaker speaker) {

        if (!this.speakers.keySet().contains(speaker.getId()))
            throw new IllegalArgumentException("Speaker not found " + speaker.getId());

        return this.speakers.put(speaker.getId(), speaker);
    }

    /**
     * Get the Speaker that matches the specified ID, or empty.
     *
     * @param id Valid ID
     * @return Optional Speaker
     */
    public Optional<Speaker> getSpeaker(final String id) {

        if (this.speakers.containsKey(id))
            return Optional.of(this.speakers.get(id));

        return Optional.empty();
    }

    /**
     * Try and fuzzy find the specified Speaker
     *
     * @param speaker Speaker to find - may contain partial details
     * @return Optional matching speakers
     */
    public Set<Speaker> find(final Speaker speaker) {

        final ArrayList<Speaker> speakers = new ArrayList<>();
        speakers.addAll(this.speakers.values());

        CollectionUtils.filter(speakers, object -> {
            final Speaker find = Speaker.class.cast(object);
            return (
                    isMatch(find.getNameFirst(), speaker.getNameFirst()) ||
                            isMatch(find.getNameLast(), speaker.getNameLast()) ||
                            isMatch(find.getOrganization(), speaker.getOrganization()) ||
                            isMatch(find.getTwitterHandle(), speaker.getTwitterHandle()));
        });

        if (!speakers.isEmpty()) {
            return new HashSet<>(speakers);
        }


        return Collections.emptySet();
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
