package io.microprofile.showcase.bootstrap;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class Parser {

    public BootstrapData parse(URL jsonResource) {
        try {
            JsonReaderFactory factory = Json.createReaderFactory(null);
            JsonReader reader = factory.createReader(jsonResource.openStream());

            JsonObject root = reader.readObject();
            JsonObject sectionList = (JsonObject) ((JsonArray)root.get("sectionList")).get(0);
            JsonArray items = (JsonArray) sectionList.get("items");

            // parse session objects
            List<Session> sessions = new LinkedList<>();

            for(JsonValue item : items) {
                Session session = new Session((JsonObject)item);
                session.setId(sessions.size()+1);
                sessions.add(session);
            }

            // parse and link speakers and schedules
            List<Speaker> speakers = new LinkedList<>();
            List<Schedule> schedules = new LinkedList<>();

            for(Session session : sessions) {

                // speakers
                JsonArray participants = session.getUnderlying().getJsonArray("participants");

                Collection<Speaker> assignedSpeakers = participants.stream()
                    .map(item -> new Speaker((JsonObject) item))
                    .collect(Collectors.toCollection(HashSet<Speaker>::new));

                assignedSpeakers.forEach(a -> {
                    boolean exists = false;
                    for(Speaker s : speakers) {
                        if(s.getFullName().equals(a.getFullName())) {
                            exists = true;
                            break;
                        }
                    }

                    if(!exists) {
                        a.setId(speakers.size()+1);
                        speakers.add(a);
                    }
                });

                HashSet<Integer> ids = assignedSpeakers.stream()
                    .map(a -> a.getId())
                    .collect(Collectors.toCollection(HashSet::new));
                session.setSpeakers(ids);

                // schedules
                JsonObject times = (JsonObject)session.getUnderlying().getJsonArray("times").getJsonObject(0);
                Schedule schedule = new Schedule(times);
                schedule.setId(schedules.size()+1);
                schedules.add(schedule);
                schedule.setSessionId(session.getId());

                session.setSchedule(schedule.getId());

            }

            reader.close();

            return new BootstrapData(sessions, speakers, schedules);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse 'schedule.json'", e);
        }
    }

}
