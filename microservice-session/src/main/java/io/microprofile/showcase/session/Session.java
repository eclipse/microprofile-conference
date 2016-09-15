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
package io.microprofile.showcase.session;

import java.util.Collection;
import java.util.Collections;

import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Ken Finnigan
 * @author Heiko Braun
 */
public class Session {

    private JsonObject underlying;

    private Collection<SessionSpeaker> speakers = Collections.EMPTY_SET;

    Session(JsonObject underlying) {
        this.underlying = underlying;
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

    public int getLength() {
        return getTimes().getInt("length");
    }

    public String getDate() {
        return getTimes().getString("date");
    }

    public String getStartTime() {
        return getTimes().getString("startTime");
    }

    private JsonObject getTimes() {
        return underlying.getJsonArray("times").getJsonObject(0);
    }

    @JsonIgnore
    JsonObject getUnderlying() {
        return underlying;
    }

    void setSpeakers(Collection<SessionSpeaker> speakers) {
        this.speakers = speakers;
    }

    public Collection<SessionSpeaker> getSpeakers() {
        return speakers;
    }

    @Override
    public String toString() {
        return "Session { code="+getCode() + ", title="+getTitle()+"}";
    }
}
