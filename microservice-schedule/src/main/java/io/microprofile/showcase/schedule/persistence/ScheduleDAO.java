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

import io.microprofile.showcase.schedule.model.Schedule;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ScheduleDAO {

    private long sequence = 0L;

    private final Map<Long, Schedule> scheduleMap = new HashMap<>();

    public Schedule addSchedule(Schedule schedule) {
        long id;
        synchronized (scheduleMap) {
            id = sequence++;
            schedule.setId(id);
            if (schedule.getSession().getId() == null) {
                schedule.getSession().setId(sequence++);
            }
            if (schedule.getVenue().getId() == null) {
                schedule.getVenue().setId(sequence++);
            }
        }

        scheduleMap.put(id, schedule);

        return schedule;
    }

    public List<Schedule> getAllSchedules() {
        return new ArrayList<>(scheduleMap.values());
    }

    public Optional<Schedule> findById(long id) {
        return Optional.ofNullable(scheduleMap.get(id));
    }

    public Schedule updateSchedule(Schedule schedule) {
        if (schedule.getId() == null) {
            return addSchedule(schedule);
        }

        scheduleMap.put(schedule.getId(), schedule);
        return schedule;
    }

    public void deleteSchedule(Schedule schedule) {
        if (schedule != null && schedule.getId() != null) {
            scheduleMap.remove(schedule.getId());
        }
    }

    public List<Schedule> findByVenue(String venue) {
        return scheduleMap.values()
                .stream()
                .filter(schedule -> schedule.getVenue().getName().equals(venue))
                .collect(Collectors.toList());
    }
}
