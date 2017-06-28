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
    public void testScheduleAndSpeakerParser() {
        URL schedule = ParserTest.class.getClassLoader().getResource("schedule.json");
        Assert.assertNotNull("Failed to load 'schedule.json'", schedule);

        URL speaker = ParserTest.class.getClassLoader().getResource("speaker.json");
        Assert.assertNotNull("Failed to load 'speaker.json'", speaker);

        Parser parser = new Parser();
        BootstrapData data = parser.parse(schedule, speaker);
        Collection<Session> sessions = data.getSessions();

        Assert.assertEquals(100, sessions.size());

        Optional<Session> sessionCode = sessions.stream()
            .filter(s -> s.getCode().equals("6E168E19-4B92-ECD8-01C6-6B4FC55C68FE"))
            .findAny();

        Assert.assertTrue("Expected specific session code in schedule", sessionCode.isPresent());
        Assert.assertEquals("et tristique pellentesque, tellus", sessionCode.get().getTitle());

        Optional<Speaker> matchingSpeaker = data.getSpeaker().stream()
            .filter(sp -> sp.getId().equals("55"))
            .findFirst();

        Assert.assertTrue(matchingSpeaker.isPresent());
        Assert.assertTrue(sessionCode.get().getSpeakers().contains(matchingSpeaker.get().getId()));


        Optional<Session> sessionCode2 = sessions.stream()
            .filter(s -> s.getCode().equals("890F4E4A-27EA-5C5D-8B40-1DAAB8E3FC05"))
            .findAny();
        Assert.assertTrue(sessionCode2.isPresent());

        Optional<Schedule> matchingSchedule = data.getSchedules().stream()
            .filter(sched -> sched.getId() == sessionCode2.get().getSchedule())
            .findFirst();
        Assert.assertTrue(matchingSchedule.isPresent());
        Assert.assertEquals("Le Mans", matchingSchedule.get().getVenue());
        Assert.assertEquals("2018-05-18", matchingSchedule.get().getDate());
        Assert.assertEquals("11:33:29", matchingSchedule.get().getStartTime());
        Assert.assertEquals("60.0", String.valueOf(matchingSchedule.get().getLength()));

        //Confirm no null elements
        for (final Session session : sessions) {
            //noinspection SuspiciousMethodCalls
            Assert.assertTrue(!session.getSpeakers().removeAll(Collections.singleton(null)));
        }
    }
}
