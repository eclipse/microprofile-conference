package io.microprofile.showcase.session;

import java.util.Collection;
import java.util.Optional;
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

    private ConcurrentHashMap<Integer, Session> storage = new ConcurrentHashMap<>();

    public Session save(Session session) {
        session.setId(nextKey());
        storage.put(session.getId(), session);
        return session;
    }

    private int nextKey() {
        Optional<Integer> max = storage.keySet().stream()
            .max(Integer::compareTo);

        if(!max.isPresent())
            throw new IllegalStateException("Session store not initialised");

        return max.get();
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

    public Optional<Session> find(Integer sessionId) {
        Session result = storage.get(sessionId);
        return result!=null ? Optional.of(result) : Optional.empty();
    }

    public Optional<Session> update(Integer sessionId, Session session) {
        Optional<Session> exisiting = find(sessionId);
        if(exisiting.isPresent()) {
            session.setId(sessionId);
            storage.put(sessionId, session);
        }
        return exisiting;
    }

    public Optional<Session> remove(Integer sessionId) {
        Optional<Session> exisiting = find(sessionId);
        if(exisiting.isPresent()) {
            storage.remove(exisiting.get().getId());
        }
        return exisiting;
    }
}
