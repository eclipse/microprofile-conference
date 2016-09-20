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
package io.microprofile.showcase.speaker.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.microprofile.showcase.speaker.model.Speaker;
import org.apache.commons.io.IOUtils;
import org.codehaus.swizzle.stream.IncludeFilterInputStream;
import org.codehaus.swizzle.stream.StreamLexer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VenueJavaOne2016 extends Venue {

    private final String name = "JavaOne2016";
    private final Logger log = Logger.getLogger(VenueJavaOne2016.class.getName());

    private final URL url = URI.create(System.getProperty("venue.url" + this.name, "https://www.oracle.com/javaone/speakers.html")).toURL();

    VenueJavaOne2016() throws MalformedURLException {
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Set<Speaker> getSpeakers() {

        final Set<Speaker> speakers = this.getSpeakersOnline();

        if (speakers.isEmpty()) {
            try {
                speakers.addAll(this.getSpeakersFile());
            } catch (final IOException e) {
                this.log.log(Level.SEVERE, "Failed to read fallback json: " + this.url, e);
            }
        }

        return speakers;
    }

    private Set<Speaker> getSpeakersOnline() {

        final Set<Speaker> speakers = new TreeSet<>((left, that) -> {

            final String nameFirst = left.getNameFirst().toLowerCase();
            final String thatNameFirst = that.getNameFirst().toLowerCase();

            if (nameFirst.compareTo(thatNameFirst) < 0) {
                return -1;
            } else if (nameFirst.compareTo(thatNameFirst) > 0) {
                return 1;
            }

            final String nameLast = left.getNameLast().toLowerCase();
            final String thatNameLast = that.getNameLast().toLowerCase();

            if (nameLast.compareTo(thatNameLast) < 0) {
                return -1;
            } else if (nameLast.compareTo(thatNameLast) > 0) {
                return 1;
            }

            if (null != left.getOrganization()) {
                if (left.getOrganization().compareTo(that.getOrganization()) < 0) {
                    return -1;
                } else if (left.getOrganization().compareTo(that.getOrganization()) > 0) {
                    return 1;
                }
            }

            if (null != left.getTwitterHandle()) {
                if (left.getTwitterHandle().compareTo(that.getTwitterHandle()) < 0) {
                    return -1;
                } else if (left.getTwitterHandle().compareTo(that.getTwitterHandle()) > 0) {
                    return 1;
                }
            }

            if (left.getId().compareTo(that.getId()) < 0) {
                return -1;
            } else if (left.getId().compareTo(that.getId()) > 0) {
                return 1;
            }
            return 0;
        });

        if (null == System.getProperty("microprofile.speaker.scrape")) {
            return speakers;
        }

        InputStream is = null;

        try {
            is = this.url.openStream();
            is = new IncludeFilterInputStream(is, "<body", "</body>");
            final StreamLexer lexer = new StreamLexer(is);

            //Read all speaker <div> blocks
            String token;
            while (null != (token = lexer.readToken("<div class=\"cw16 cw16v1 cwidth\" ", "!-- CN"))) {
                speakers.add(this.processSpeakerToken(token));
            }

            //<div class="cw16 cw16v1 cwidth"

            //System.out.println(new String(readBytes(is)));

        } catch (final Exception e) {
            this.log.log(Level.SEVERE, "Failed to parse: " + this.url, e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (final Exception e) {
                    //no-op
                }
            }
        }
        return speakers;
    }

    private Set<Speaker> getSpeakersFile() throws IOException {
        final ObjectMapper om = new ObjectMapper();
        final InputStream is = this.getClass().getResourceAsStream("/JavaOne2016.json");
        return om.readValue(is, new TypeReference<Set<Speaker>>() {
        });
    }

    private Speaker processSpeakerToken(final String token) {
        final Speaker speaker = new Speaker();
        speaker.setId(UUID.randomUUID().toString());
        //System.out.println("token = " + token);

        try (InputStream is = IOUtils.toInputStream(token, Charset.forName("UTF-8"))) {
            final StreamLexer lexer = new StreamLexer(is);
            speaker.setPicture(this.getImage(lexer));

            final String fullName = lexer.readToken("<h4>", "</h4>");
            speaker.setNameFirst(fullName.substring(0, fullName.indexOf(" ")));
            speaker.setNameLast(fullName.substring(fullName.indexOf(" ") + 1));

            speaker.setBiography(lexer.readToken("<p>", "</p>"));
            speaker.setTwitterHandle(lexer.readToken("href=\"https://twitter.com/", "\">@"));

        } catch (final Exception e) {
            this.log.log(Level.SEVERE, "Failed to parse token: " + token, e);
        }

        this.log.log(Level.FINE, "Found:" + speaker);

        return speaker;
    }

    private String getImage(final StreamLexer lexer) throws Exception {
        String imgToken = lexer.readToken("<div class=\"cw16w1\"><img", "</div>");
        imgToken = imgToken.substring(imgToken.indexOf("src=\"//") + 7);
        imgToken = "http://" + imgToken.substring(0, imgToken.indexOf("\""));
        return imgToken;
    }
}
