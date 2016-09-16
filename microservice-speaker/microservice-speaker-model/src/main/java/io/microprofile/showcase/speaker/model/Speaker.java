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
package io.microprofile.showcase.speaker.model;


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

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Speaker implements Serializable {

    private static final long serialVersionUID = -8693770048623415961L;
    private String id;
    private String nameFirst;
    private String nameLast;
    private String organization;
    private String biography;
    private String picture;
    private String twitterHandle;

    //TODO @XmlElement(name = "_links") Who cam up with this name? It just causes a whole world of serialization and mapper config issues
    private Map<String, URI> links = new HashMap<>();

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getNameFirst() {
        return this.nameFirst;
    }

    public void setNameFirst(final String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return this.nameLast;
    }

    public void setNameLast(final String nameLast) {
        this.nameLast = nameLast;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(final String organization) {
        this.organization = organization;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(final String biography) {
        this.biography = biography;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(final String picture) {
        this.picture = picture;
    }

    public String getTwitterHandle() {
        return this.twitterHandle;
    }

    public void setTwitterHandle(final String twitterHandle) {
        this.twitterHandle = twitterHandle;
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

        final Speaker speaker = (Speaker) o;

        return new EqualsBuilder()
                .append(this.id, speaker.id)
                .append(this.nameFirst, speaker.nameFirst)
                .append(this.nameLast, speaker.nameLast)
                .append(this.organization, speaker.organization)
                .append(this.biography, speaker.biography)
                .append(this.picture, speaker.picture)
                .append(this.twitterHandle, speaker.twitterHandle)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.id)
                .append(this.nameFirst)
                .append(this.nameLast)
                .append(this.organization)
                .append(this.biography)
                .append(this.picture)
                .append(this.twitterHandle)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("nameFirst", this.nameFirst)
                .append("nameLast", this.nameLast)
                .append("organization", this.organization)
                .append("biography", this.biography)
                .append("picture", this.picture)
                .append("twitterHandle", this.twitterHandle)
                .toString();
    }
}