package io.microprofile.showcase.session;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class ScheduleParserTest {

    @Test
    public void testScheduleParser() {
        URL resource = ScheduleParserTest.class.getClassLoader().getResource("schedule.json");
        Assert.assertNotNull("Failed to load 'schedule.json'", resource);

        ScheduleParser parser = new ScheduleParser();
        Schedule schedule = parser.parse(resource);
        Collection<Session> sessions = schedule.getSessions();

        Assert.assertEquals(50, sessions.size());

        Optional<Session> con5594 = sessions.stream()
            .filter(s -> s.getCode().equals("CON5594"))
            .findAny();

        Assert.assertTrue("Expected CON5594 in schedule", con5594.isPresent());
        Assert.assertEquals("Cross-Functional Code Reviews", con5594.get().getTitle());
    }
}
