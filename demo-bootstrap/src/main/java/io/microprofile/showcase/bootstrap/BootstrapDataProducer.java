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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
@ApplicationScoped
public class BootstrapDataProducer {

    @Produces
    public BootstrapData load() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL schedule = loader.getResource("schedule.json");
        if (schedule == null) {
            schedule = BootstrapDataProducer.class.getResource("/schedule.json");
            if (schedule == null) {
                throw new IllegalStateException("Failed to load 'schedule.json'");
            }
        }

        URL speaker = loader.getResource("speaker.json");
        if (speaker == null) {
            speaker = BootstrapDataProducer.class.getResource("/speaker.json");
            if (speaker == null) {
                throw new IllegalStateException("Failed to load 'speaker.json'");
            }
        }

        final Parser parser = new Parser();
        final BootstrapData data = parser.parse(schedule, speaker);

        Logger.getLogger(BootstrapData.class.getName()).log(Level.INFO, "Schedule contains "+data.getSessions().size() + " sessions");

        return data;
    }

}

