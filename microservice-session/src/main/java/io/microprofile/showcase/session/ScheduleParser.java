package io.microprofile.showcase.session;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class ScheduleParser {
    public Schedule parse(URL jsonResource) {
        try {
            JsonReaderFactory factory = Json.createReaderFactory(null);
            JsonReader reader = factory.createReader(jsonResource.openStream());

            JsonObject root = reader.readObject();
            JsonObject sectionList = (JsonObject) ((JsonArray)root.get("sectionList")).get(0);
            JsonArray items = (JsonArray) sectionList.get("items");

            // parse session objects
            List<Session> sessions = items.stream()
                .map(i -> (JsonObject) i)
                .map(o -> parseSession(o))
                .collect(Collectors.toList());

            // parse and link speakers
            sessions.forEach( s -> parseSpeaker(s) );

            return new Schedule(sessions);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse 'schedule.json'", e);
        }
    }

    private Session parseSession(JsonObject o) {
        return new Session(o);
    }

    private Collection<SessionSpeaker> parseSpeaker(Session session) {

        JsonArray participants = session.getUnderlying().getJsonArray("participants");

        // parse
        Collection<SessionSpeaker> speakers = participants.stream()
            .map(item -> new SessionSpeaker((JsonObject) item))
            .collect(Collectors.toCollection(HashSet<SessionSpeaker>::new));

        // link
        session.setSpeakers(speakers);

        return speakers;
    }
}
