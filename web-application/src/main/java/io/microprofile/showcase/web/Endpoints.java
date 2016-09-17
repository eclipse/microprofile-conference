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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Simple wrapper for named application endpoints
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Endpoints implements Serializable {

    private static final long serialVersionUID = -7130557896231134141L;

    private Set<Endpoint> endpoints;
    private String application;

    //TODO @XmlElement(name = "_links") Who cam up with this name? It just causes a whole world of serialization and config issues
    private Map<String, URI> links = new HashMap<>();

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

    public Map<String, URI> getLinks() {
        return this.links;
    }

    public void setLinks(final Map<String, URI> links) {
        this.links = links;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || this.getClass() != o.getClass()) return false;

        final Endpoints endpoints = (Endpoints) o;

        return new EqualsBuilder()
                .append(this.application, endpoints.application)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.application)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("application", this.application)
                .toString();
    }
}