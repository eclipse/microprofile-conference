/*
 * Copyright(c) 2016-2017 IBM, Red Hat, and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.microprofile.showcase.bootstrap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
import java.io.IOException;
import java.net.URL;
import java.util.*;
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

            final JsonArray items = reader.readArray();

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

                // speaker
                Speaker speaker = new Speaker(session.getUnderlying());
                speaker.setId(String.valueOf(session.getUnderlying().getInt("speaker")));
                speakers.add(speaker);
                session.setSpeakers(Collections.singletonList(speaker.getId()));

                // schedules
                final JsonObject times = session.getUnderlying();
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
