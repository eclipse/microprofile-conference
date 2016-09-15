package io.microprofile.showcase.session;

import java.net.URL;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
@ApplicationScoped
public class ScheduleProducer {

    @Produces
    public Schedule createSchedule() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("schedule.json");
        assert resource !=null : "Failed to load 'schedule.json'";

        ScheduleParser parser = new ScheduleParser();
        Schedule schedule = parser.parse(resource);

        System.out.println("Schedule contains "+schedule.getSessions().size() + " sessions");

        return schedule;
    }
}
