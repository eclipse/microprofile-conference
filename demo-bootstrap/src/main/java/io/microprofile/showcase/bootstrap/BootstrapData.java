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

import java.util.Collection;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class BootstrapData {

    private final Collection<Session> sessions;

    private final Collection<Speaker> speakers;

    private final Collection<Schedule> schedules;

    BootstrapData(final Collection<Session> sessions, final Collection<Speaker> speakers, final Collection<Schedule> schedules) {
        this.sessions = sessions;
        this.speakers = speakers;
        this.schedules = schedules;
    }

    public Collection<Session> getSessions() {
        return sessions;
    }

    public Collection<Speaker> getSpeaker() {
        return speakers;
    }

    public Collection<Schedule> getSchedules() {
        return schedules;
    }
}
