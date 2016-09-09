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
package io.microprofile.showcase.speaker.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.microprofile.showcase.speaker.model.Speaker;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.Set;
import java.util.logging.Logger;

public class VenueJavaOne2016Test {

    private final Logger log = Logger.getLogger(VenueJavaOne2016Test.class.getName());

    @Test
    public void testGetSpeakers() throws Exception {

        final Set<Speaker> speakers = new VenueJavaOne2016().getSpeakers();

        Assert.assertFalse("Failed to get any speakers", speakers.isEmpty());

        for (final Speaker speaker : speakers) {
            Assert.assertNotNull(speaker.getNameLast());
            this.log.info(speaker.getNameFirst() + " " + speaker.getNameLast());
        }
    }

    @Test
    public void readSpeakers() throws Exception {
        final ObjectMapper om = new ObjectMapper();
        final InputStream is = this.getClass().getResourceAsStream("/speakers.json");
        final Set<Speaker> speakers = om.readValue(is, new TypeReference<Set<Speaker>>() {
        });

        Assert.assertFalse("Failed to get any speakers", speakers.isEmpty());

        for (final Speaker speaker : speakers) {
            Assert.assertNotNull(speaker.getNameLast());
            this.log.info(speaker.getNameFirst() + " " + speaker.getNameLast());
        }
    }
}