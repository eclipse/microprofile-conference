package io.microprofile.showcase.session;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.json.JsonObject;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Ken Finnigan
 * @author Heiko Braun
 */
public class Session {

    private int schedule;

    protected JsonObject underlying;

    private String id;

    private Collection<String> speakers = Collections.EMPTY_SET;

    public Session(final String id, final JsonObject underlying) {
        this.id = id;
        this.underlying = underlying;
    }

    @JsonIgnore
    JsonObject getUnderlying() {
        return underlying;
    }

    public String getId() {
        return id;
    }

    void setId(final String id) {
        this.id = id;
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

    public void setSpeakers(final Collection<String> speakers) {
        this.speakers = speakers;
    }

    public Collection<String> getSpeakers() {
        return speakers;
    }

    public void setSchedule(final int schedule) {
        this.schedule = schedule;
    }

    public int getSchedule() {
        return schedule;
    }

}

