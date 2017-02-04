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

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
public class Schedule extends JsonWrapper {


    private String sessionId;

    public Schedule(final JsonObject underlying) {
        super(underlying);
    }

    public String getDate() {
        return underlying.getString("date");
    }

    public String getStartTime() {
        return underlying.getString("startTime");
    }

    public String getVenue() {
        return underlying.getString("room");
    }

    public double getLength() {
        return underlying.getJsonNumber("length").doubleValue();
    }

    public String getSessionId() {
        return sessionId;
    }

    void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return getId() + "::" + getDate() + "::" + getStartTime();
    }

}
