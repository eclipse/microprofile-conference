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

import io.microprofile.showcase.speaker.model.Speaker;

import java.util.Set;

public abstract class Venue {

    public abstract String getName();

    public abstract Set<Speaker> getSpeakers();
}
