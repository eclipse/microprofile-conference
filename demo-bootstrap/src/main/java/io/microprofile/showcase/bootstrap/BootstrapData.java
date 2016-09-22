package io.microprofile.showcase.bootstrap;

import java.util.Collection;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class BootstrapData {

    private final Collection<Session> sessions;

    private final Collection<Speaker> speakers;

    private final Collection<Schedule> schedules;

    BootstrapData(final Collection<Session> sessions, final Collection<Speaker> speakers, final Collection<Schedule> schedules) {
        this.sessions = sessions;
        this.speakers = speakers;
        this.schedules = schedules;
    }

    public Collection<Session> getSessions() {
        return sessions;
    }

    public Collection<Speaker> getSpeaker() {
        return speakers;
    }

    public Collection<Schedule> getSchedules() {
        return schedules;
    }
}
