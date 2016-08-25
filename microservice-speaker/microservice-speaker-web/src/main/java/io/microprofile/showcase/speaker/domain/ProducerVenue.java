/**
 * Tomitribe Confidential
 * <p/>
 * Copyright(c) Tomitribe Corporation. 2014
 * <p/>
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 * <p/>
 */
package io.microprofile.showcase.speaker.domain;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ProducerVenue {

    private final Logger log = LogManager.getLogManager().getLogger(ProducerVenue.class.getName());

    @Produces
    @ApplicationScoped
    @QVenue
    public Venue produceVenue() {
        try {
            return new VenueJavaOne2016();
        } catch (final MalformedURLException e) {
            this.log.log(Level.SEVERE, "Failed to produce a Venue", e);
        }

        return null;
    }
}
