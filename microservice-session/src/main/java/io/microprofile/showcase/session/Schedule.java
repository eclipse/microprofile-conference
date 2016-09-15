package io.microprofile.showcase.session;

import java.util.Collection;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class Schedule {

    private Collection<Session> sessions;

    public Schedule(Collection<Session> sessions) {
        this.sessions = sessions;
    }

    public Collection<Session> getSessions() {
        return sessions;
    }
}
