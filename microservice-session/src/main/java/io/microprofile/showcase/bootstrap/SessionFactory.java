package io.microprofile.showcase.bootstrap;

import io.microprofile.showcase.session.Session;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Heiko Braun
 * @since 16/09/16
 */
public class SessionFactory {

    public static Session fromBootstrap(final io.microprofile.showcase.bootstrap.Session bootstrapModel) {
        final Session session = new Session(bootstrapModel.getId(), bootstrapModel.getUnderlying());
        session.setSpeakers(bootstrapModel.getSpeakers());
        session.setSchedule(Integer.valueOf(bootstrapModel.getSchedule()));
        return session;
    }
}
