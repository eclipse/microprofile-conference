package io.microprofile.showcase.bootstrap;

import io.microprofile.showcase.session.Session;

/**
 * @author Heiko Braun
 * @since 16/09/16
 */
public class SessionFactory {

    public static Session fromBootstrap(io.microprofile.showcase.bootstrap.Session bootstrapModel) {
        Session session = new Session(bootstrapModel.getId(), bootstrapModel.getUnderlying());
        session.setSpeakers(bootstrapModel.getSpeakers());
        session.setSchedule(bootstrapModel.getSchedule());
        return session;
    }
}
