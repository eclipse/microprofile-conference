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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@ApplicationScoped
public class ProducerVenue {

    private final Logger log = LogManager.getLogManager().getLogger(ProducerVenue.class.getName());

    /**
     * Support for eventually having more than one venue
     *
     * @return List of Venue
     */
    @Produces
    @VenueList
    @Named(value = "venueList")
    public List<Venue> produceVenues() {

        final List<Venue> venues = new ArrayList<>();

        try {
            venues.add(new VenueJavaOne2016());
        } catch (final MalformedURLException e) {
            this.log.log(Level.SEVERE, "Failed to produce a Venue", e);
        }

        return venues;
    }
}
