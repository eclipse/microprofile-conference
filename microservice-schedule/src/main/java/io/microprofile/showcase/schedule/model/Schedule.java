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
package io.microprofile.showcase.schedule.model;

import io.microprofile.showcase.schedule.model.adapters.DurationAdapter;
import io.microprofile.showcase.schedule.model.adapters.LocalDateAdapter;
import io.microprofile.showcase.schedule.model.adapters.LocalTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Schedule {

    private String id;
    private String sessionId;
    private String venue;
    private String venueId;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime startTime;

    @XmlJavaTypeAdapter(DurationAdapter.class)
    private Duration duration;

    public Schedule() {
    }

    public Schedule(final String sessionId, final String venue, final String venueId, final LocalDate date, final LocalTime startTime, final Duration duration) {
        this(null, sessionId, venue, venueId, date, startTime, duration);
    }

    public Schedule(final String id, final String sessionId, final String venue, final String venueId, final LocalDate date, final LocalTime startTime, final Duration duration) {
        this.id = id;
        this.sessionId = sessionId;
        this.venue = venue;
        this.venueId = venueId;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    public String getVenue() {
        return venue;
    }

    public String getVenueId() {
        return venueId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Schedule schedule = (Schedule) o;
        return Objects.equals(sessionId, schedule.sessionId) &&
                Objects.equals(venue, schedule.venue) &&
                Objects.equals(date, schedule.date) &&
                Objects.equals(startTime, schedule.startTime) &&
                Objects.equals(duration, schedule.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, venue, date, startTime, duration);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "session=" + sessionId +
                ", venue='" + venue + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
