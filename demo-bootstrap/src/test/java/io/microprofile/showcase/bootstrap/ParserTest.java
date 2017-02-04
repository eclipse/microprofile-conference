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

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class ParserTest {

    @Test
    public void testScheduleParser() {
        URL resource = ParserTest.class.getClassLoader().getResource("schedule.json");
        Assert.assertNotNull("Failed to load 'schedule.json'", resource);

        Parser parser = new Parser();
        BootstrapData data = parser.parse(resource);
        Collection<Session> sessions = data.getSessions();

        Assert.assertEquals(50, sessions.size());

        Optional<Session> con5594 = sessions.stream()
            .filter(s -> s.getCode().equals("CON5594"))
            .findAny();

        Assert.assertTrue("Expected CON5594 in schedule", con5594.isPresent());
        Assert.assertEquals("Cross-Functional Code Reviews", con5594.get().getTitle());

        Optional<Speaker> speaker = data.getSpeaker().stream()
            .filter(sp -> sp.getFullName().equals("Margaret Fero"))
            .findFirst();

        Assert.assertTrue(speaker.isPresent());
        Assert.assertTrue(con5594.get().getSpeakers().contains(speaker.get().getId()));


        Optional<Session> con4226 = sessions.stream()
            .filter(s -> s.getCode().equals("CON4226"))
            .findAny();
        Assert.assertTrue(con4226.isPresent());

        Optional<Schedule> schedule = data.getSchedules().stream()
            .filter(sched -> sched.getId() == con4226.get().getSchedule())
            .findFirst();
        Assert.assertTrue(schedule.isPresent());
        Assert.assertEquals("Hilton - Continental Ballroom 4", schedule.get().getVenue());
        Assert.assertEquals("2016-09-21", schedule.get().getDate());
        Assert.assertEquals("16:30", schedule.get().getStartTime());
        Assert.assertEquals("60.0", String.valueOf(schedule.get().getLength()));

        //Confirm no null elements
        for (final Session session : sessions) {
            //noinspection SuspiciousMethodCalls
            Assert.assertTrue(!session.getSpeakers().removeAll(Collections.singleton(null)));
        }
    }
}
