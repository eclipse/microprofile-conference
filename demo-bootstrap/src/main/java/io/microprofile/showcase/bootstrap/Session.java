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

import javax.json.JsonObject;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Ken Finnigan
 * @author Heiko Braun
 */
public class Session extends JsonWrapper {

    private String schedule;

    private Collection<String> speakers = Collections.EMPTY_SET;

    Session(final JsonObject underlying) {
        super(underlying);
    }

    public String getAbstract() {
        return underlying.getString("abstract");
    }

    public String getCode() {
        return underlying.getString("code");
    }

    public String getTitle() {
        return underlying.getString("title");
    }

    public String getType() {
        return underlying.getString("type");
    }

    void setSpeakers(final Collection<String> speakers) {
        this.speakers = speakers;
    }

    public Collection<String> getSpeakers() {
        return speakers;
    }

    void setSchedule(final String schedule) {
        this.schedule = schedule;
    }

    public String getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return getId() + "::" + getCode();
    }
}
