package io.microprofile.showcase.bootstrap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class Parser {

    private final Logger log = Logger.getLogger(Parser.class.getName());
    private final AtomicInteger id = new AtomicInteger(0);

    public BootstrapData parse(final URL jsonResource) {
        try {
            final JsonReaderFactory factory = Json.createReaderFactory(null);
            final JsonReader reader = factory.createReader(jsonResource.openStream());

            final JsonObject root = reader.readObject();
            final JsonObject sectionList = (JsonObject) ((JsonArray) root.get("sectionList")).get(0);
            final JsonArray items = (JsonArray) sectionList.get("items");

            // parse session objects
            final List<Session> sessions = new LinkedList<>();

            for (final JsonValue item : items) {
                final Session session = new Session((JsonObject) item);
                session.setId(String.valueOf(this.id.incrementAndGet()));
                sessions.add(session);
            }

            // parse and link speakers and schedules
            final List<Speaker> speakers = new LinkedList<>();
            final List<Schedule> schedules = new LinkedList<>();

            for (final Session session : sessions) {

                // speakers
                final JsonArray participants = session.getUnderlying().getJsonArray("participants");

                final Collection<Speaker> assignedSpeakers = participants.stream()
                        .map(item -> new Speaker((JsonObject) item))
                        .collect(Collectors.toCollection(HashSet<Speaker>::new));

                assignedSpeakers.forEach(a -> {
                    boolean exists = false;
                    for (final Speaker s : speakers) {
                        if (s.getFullName().equals(a.getFullName())) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        a.setId(String.valueOf(this.id.incrementAndGet()));
                        speakers.add(a);
                    }
                });

                final HashSet<String> ids = assignedSpeakers.stream()
                        .map(JsonWrapper::getId)
                        .collect(Collectors.toCollection(HashSet::new));
                session.setSpeakers(ids);

                // schedules
                final JsonObject times = session.getUnderlying().getJsonArray("times").getJsonObject(0);
                final Schedule schedule = new Schedule(times);
                schedule.setId(String.valueOf(this.id.incrementAndGet()));
                schedules.add(schedule);
                schedule.setSessionId(session.getId());

                session.setSchedule(schedule.getId());

            }

            reader.close();

            return new BootstrapData(sessions, speakers, schedules);

        } catch (final IOException e) {
            throw new RuntimeException("Failed to parse 'schedule.json'", e);
        }
    }

}
