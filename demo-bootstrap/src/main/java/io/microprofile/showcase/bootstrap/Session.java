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
package io.microprofile.showcase.bootstrap;

import java.util.Collection;
import java.util.Collections;

import javax.json.JsonObject;

/**
 * @author Ken Finnigan
 * @author Heiko Braun
 */
public class Session extends JsonWrapper {

    private int schedule;

    private Collection<Integer> speakers = Collections.EMPTY_SET;

    Session(JsonObject underlying) {
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

    void setSpeakers(Collection<Integer> speakers) {
        this.speakers = speakers;
    }

    public Collection<Integer> getSpeakers() {
        return speakers;
    }

    void setSchedule(int schedule) {
        this.schedule = schedule;
    }

    public int getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return getId() + "::"+getCode();
    }

}
