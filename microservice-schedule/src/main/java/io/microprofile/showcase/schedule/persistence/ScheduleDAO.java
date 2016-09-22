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

import io.microprofile.showcase.schedule.cdi.ScheduleCache;
import io.microprofile.showcase.schedule.model.Schedule;
import io.microprofile.showcase.schedule.model.Session;
import io.microprofile.showcase.schedule.model.Venue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;



import static java.time.Month.SEPTEMBER;
import javax.cache.Cache;
import javax.cache.Cache.Entry;
import javax.inject.Inject;

@ApplicationScoped
public class ScheduleDAO {

    private long sequence = 11L;

    @Inject
    @ScheduleCache
    private Cache<Long, Schedule> scheduleCache;

    @PostConstruct
    private void intializeScheduleMapWithDummyData() {
        for (long i = 1; i <= 10; i++) {
            scheduleCache.put(i, new Schedule(i,
                new Session(i, "Java " + i + " for dummies", "The one who can't be named"),
                new Venue(i, "Room 2" + i + i + 1),
                LocalDate.of(2016, SEPTEMBER, 18), LocalTime.of(14, (int) i), Duration.ofHours(1)));
        }
    }

    public Schedule addSchedule(Schedule schedule) {
        long id;
        synchronized (scheduleCache) {
            id = sequence++;
            schedule.setId(id);
            if (schedule.getSession().getId() == null) {
                schedule.getSession().setId(sequence++);
            }
            if (schedule.getVenue().getId() == null) {
                schedule.getVenue().setId(sequence++);
            }
        }

        scheduleCache.put(id, schedule);

        return schedule;
    }

    public List<Schedule> getAllSchedules() {
        final ArrayList<Schedule> result = new ArrayList<>();
        for (Entry<Long,Schedule> e : scheduleCache) {
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

    public List<Schedule> findByVenue(String venue) {
        return getAllSchedules()
            .stream()
            .filter(schedule -> schedule.getVenue().getName().equals(venue))
            .collect(Collectors.toList());
    }

    public List<Schedule> findByDate(LocalDate date) {
        return getAllSchedules()
                .stream()
                .filter(schedule -> schedule.getDate().equals(date))
                .collect(Collectors.toList());
    }
}
