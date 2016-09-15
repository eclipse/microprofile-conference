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
package io.microprofile.showcase.web;

import java.io.Serializable;
import java.util.Set;

/**
 * Simple wrapper for named application endpoints
 */
public class Endpoints implements Serializable {

    private static final long serialVersionUID = -7130557896231134141L;

    private Set<Endpoint> endpoints;
    private String application;

    public Set<Endpoint> getEndpoints() {
        return this.endpoints;
    }

    public void setEndpoints(final Set<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public void setApplication(final String application) {
        this.application = application;
    }

    public String getApplication() {
        return this.application;
    }
}
