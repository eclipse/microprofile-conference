package io.microprofile.showcase.bootstrap;

import io.microprofile.showcase.session.Session;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Heiko Braun
 * @since 16/09/16
 */
public class SessionFactory {

    public static Session fromBootstrap(io.microprofile.showcase.bootstrap.Session bootstrapModel) {
        Session session = new Session(Integer.valueOf(bootstrapModel.getId()), bootstrapModel.getUnderlying());
        session.setSpeakers(mapStringToIntCollection(bootstrapModel.getSpeakers()));
        session.setSchedule(Integer.valueOf(bootstrapModel.getSchedule()));
        return session;
    }

    private static Collection<Integer> mapStringToIntCollection(Collection<String> speakers) {
        return speakers.stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
