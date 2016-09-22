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
import java.io.Serializable;

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
public class Schedule implements Serializable {

    private Long id;
    private Session session;
    private Venue venue;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime startTime;
    @XmlJavaTypeAdapter(DurationAdapter.class)
    private Duration duration;

    public Schedule() {
    }

    public Schedule(Session session, Venue venue, LocalDate date, LocalTime startTime, Duration duration) {
        this(null, session, venue, date, startTime, duration);
    }

    public Schedule(Long id, Session session, Venue venue, LocalDate date, LocalTime startTime, Duration duration) {
        this.id = id;
        this.session = session;
        this.venue = venue;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public Venue getVenue() {
        return venue;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(session, schedule.session) &&
            Objects.equals(venue, schedule.venue) &&
            Objects.equals(date, schedule.date) &&
            Objects.equals(startTime, schedule.startTime) &&
            Objects.equals(duration, schedule.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, venue, date, startTime, duration);
    }

    @Override
    public String toString() {
        return "Schedule{" +
            "session=" + session +
            ", venue='" + venue + '\'' +
            ", date=" + date +
            ", startTime=" + startTime +
            ", duration=" + duration +
            '}';
    }
}
