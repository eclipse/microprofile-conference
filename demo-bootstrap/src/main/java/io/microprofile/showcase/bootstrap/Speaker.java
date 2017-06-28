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
 * @author Ken Finnigan
 * @author Heiko Braun
 */
public class Speaker extends JsonWrapper {

    public Speaker(final JsonObject underlying) {
        super(underlying);
    }

    public String getFirstName() {
        return underlying.getString("first-name", "n/a");
    }

    public String getLastName() {
        return underlying.getString("last-name", "n/a");
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getJobTitle() {
        return underlying.getString("title", "n/a");
    }

    public String getCompany() {
        return underlying.getString("company", "n/a");
    }

    @Override
    public String toString() {
        return getId() + "::"+getFullName();
    }
}
