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

import io.microprofile.showcase.session.Session;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Heiko Braun
 * @since 16/09/16
 */
public class SessionFactory {

    public static Session fromBootstrap(final io.microprofile.showcase.bootstrap.Session bootstrapModel) {
        final Session session = new Session(bootstrapModel.getId(), bootstrapModel.getUnderlying());
        session.setSpeakers(bootstrapModel.getSpeakers());
        session.setSchedule(Integer.valueOf(bootstrapModel.getSchedule()));
        return session;
    }
}
