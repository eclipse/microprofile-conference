package io.microprofile.showcase.bootstrap;

import java.util.Collection;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class BootstrapData {

    private Collection<Session> sessions;

    private Collection<Speaker> speakers;

    private Collection<Schedule> schedules;

    BootstrapData(Collection<Session> sessions, Collection<Speaker> speakers, Collection<Schedule> schedules) {
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
