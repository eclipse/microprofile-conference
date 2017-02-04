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

package io.microprofile.showcase.session;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.microprofile.showcase.bootstrap.BootstrapData;
import io.microprofile.showcase.bootstrap.SessionFactory;

/**
 * @author Heiko Braun
 * @since 16/09/16
 */
@ApplicationScoped
public class SessionStore {

    @Inject
    BootstrapData bootstrapData;

    private final ConcurrentHashMap<String, Session> storage = new ConcurrentHashMap<>();

    public Session save(final Session session) {
        session.setId(UUID.randomUUID().toString());
        storage.put(session.getId(), session);
        return session;
    }

    @PostConstruct
    private void initStore() {
        Logger.getLogger(SessionStore.class.getName()).log(Level.INFO, "Initialise sessions from bootstrap data");

        bootstrapData.getSessions()
            .forEach(bootstrap -> storage.put(bootstrap.getId(), SessionFactory.fromBootstrap(bootstrap)));

    }

    public Collection<Session> getSessions() {
        return storage.values();
    }

    public Optional<Session> find(final String sessionId) {
        final Session result = storage.get(sessionId);
        return result!=null ? Optional.of(result) : Optional.empty();
    }

    public Optional<Session> update(final String sessionId, final Session session) {
        final Optional<Session> existing = find(sessionId);
        if(existing.isPresent()) {
            session.setId(sessionId);
            storage.put(sessionId, session);
        }
        return existing;
    }

    public Optional<Session> remove(final String sessionId) {
        final Optional<Session> existing = find(sessionId);
        if(existing.isPresent()) {
            storage.remove(existing.get().getId());
        }
        return existing;
    }
}
