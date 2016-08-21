/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Speaker {

    private String id;
    private String nameFirst;
    private String nameLast;
    private String organization;
    private String biography;
    private String picture;
    private String twitterHandle;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || this.getClass() != o.getClass()) return false;

        final Speaker speaker = (Speaker) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
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
        return "Speaker{" +
                "id='" + this.id + '\'' +
                ", nameFirst='" + this.nameFirst + '\'' +
                ", nameLast='" + this.nameLast + '\'' +
                ", organization='" + this.organization + '\'' +
                ", biography='" + this.biography + '\'' +
                ", picture='" + this.picture + '\'' +
                ", twitterHandle='" + this.twitterHandle + '\'' +
                '}';
    }
}
