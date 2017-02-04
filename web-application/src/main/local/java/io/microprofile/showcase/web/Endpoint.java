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
package io.microprofile.showcase.web;

import java.io.Serializable;

/**
 * Defines a named endpoint
 */
public class Endpoint implements Serializable {

    private static final long serialVersionUID = 3484992987302610677L;

    private String name;
    private String url;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns a Base64 encoded URL
     *
     * @return String
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Set a Base64 encoded URL
     *
     * @param url String
     */
    public void setUrl(final String url) {
        this.url = url;
    }

}
