/*
 * Copyright 2016 Microprofile.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.microprofile.showcase.schedule.persistence;

import io.microprofile.showcase.bootstrap.BootstrapData;
import io.microprofile.showcase.schedule.cdi.ScheduleCache;
import io.microprofile.showcase.schedule.model.Schedule;
import io.microprofile.showcase.schedule.model.adapters.LocalDateAdapter;
import io.microprofile.showcase.schedule.model.adapters.LocalTimeAdapter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.cache.Cache;

@ApplicationScoped
public class ScheduleDAO {


    @Inject
    BootstrapData bootstrapData;

    private long sequence = 11L;

    @Inject
    @ScheduleCache
    private Cache<Long, Schedule> scheduleCache;
    private Map<Long, String> venues = new ConcurrentHashMap<>();

    @PostConstruct
    private void initStore() {
        Logger.getLogger(ScheduleDAO.class.getName()).log(Level.INFO, "Initialise schedule DAO from bootstrap data");

        LocalDateAdapter dateAdapter = new LocalDateAdapter();
        LocalTimeAdapter timeAdapter = new LocalTimeAdapter();

        bootstrapData.getSchedules()
            .forEach(bootstrap -> {

                try {

                    Long venueId = null;
                    for(Long key : venues.keySet()) {
                        String v = venues.get(key);
                        if(v.equals(bootstrap.getVenue())) {
                            // existing venue
                            venueId = key;
                            break;
                        }
                    }

                    // generate a new key
                    if(null==venueId)
                        venueId = sequence++;

                    Schedule sched = new Schedule(
                        new Long(bootstrap.getId()),
                        new Long(bootstrap.getSessionId()),
                        bootstrap.getVenue(),
                        venueId,
                        dateAdapter.unmarshal(bootstrap.getDate()),
                        timeAdapter.unmarshal(bootstrap.getStartTime()),
                        Duration.ofMinutes(new Double(bootstrap.getLength()).longValue())
                    );


                    scheduleCache.put(new Long(bootstrap.getId()), sched);
                    venues.put(venueId, sched.getVenue());

                } catch (Exception e) {
                    System.out.println("Failed to parse bootstrap data: "+ e.getMessage());
                }

            });

    }

    public Schedule addSchedule(Schedule schedule) {

        long id = sequence++;
        schedule.setId(id);

        if (schedule.getSessionId() == null) {
            schedule.setSessionId(sequence++);
        }

        scheduleCache.put(id, schedule);

        return schedule;
    }

    public List<Schedule> getAllSchedules() {
        final ArrayList<Schedule> result = new ArrayList<>();
        for (Cache.Entry<Long,Schedule> e : scheduleCache) {
            result.add(e.getValue());
        }
        return result;
    }

    public Optional<Schedule> findById(long id) {
        return Optional.ofNullable(scheduleCache.get(id));
    }

    public Schedule updateSchedule(Schedule schedule) {
        if (schedule.getId() == null) {
            return addSchedule(schedule);
        }

        scheduleCache.put(schedule.getId(), schedule);
        return schedule;
    }

    public void deleteSchedule(Long scheduleId) {
        if (scheduleId != null) {
            scheduleCache.remove(scheduleId);
        }
    }

    public List<Schedule> findByVenue(Long venueId) {
        return getAllSchedules()
            .stream()
            .filter(schedule -> schedule.getVenueId().equals(venueId))
            .collect(Collectors.toList());
    }

    public List<Schedule> findByDate(LocalDate date) {
        return getAllSchedules()
                .stream()
                .filter(schedule -> schedule.getDate().equals(date))
                .collect(Collectors.toList());
    }
}
