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
package io.microprofile.showcase.vote.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import io.microprofile.showcase.vote.model.SessionRating;

@ApplicationScoped
@NonPersistent
public class HashMapSessionRatingDAO implements SessionRatingDAO {

    private Map<String, SessionRating> allRatings = new HashMap<String, SessionRating>();

    private Map<String, Collection<String>> ratingIdsBySession = new HashMap<String, Collection<String>>();

    private Map<String, Collection<String>> ratingIdsByAttendee = new HashMap<String, Collection<String>>();

    @Override
    public SessionRating rateSession(SessionRating sessionRating) {

        String ratingId = UUID.randomUUID().toString();
        sessionRating = new SessionRating(ratingId, null, sessionRating.getSession(), sessionRating.getAttendeeId(), sessionRating.getRating());

        String session = sessionRating.getSession();
        String attendeeId = sessionRating.getAttendeeId();

        allRatings.put(sessionRating.getId(), sessionRating);
        Collection<String> sessionRatings = ratingIdsByAttendee.get(attendeeId);
        if (sessionRatings == null) {
            sessionRatings = new HashSet<String>();
            ratingIdsByAttendee.put(attendeeId, sessionRatings);
        }
        sessionRatings.add(sessionRating.getId());

        sessionRatings = ratingIdsBySession.get(session);
        if (sessionRatings == null) {
            sessionRatings = new HashSet<String>();
            ratingIdsBySession.put(session, sessionRatings);
        }
        sessionRatings.add(sessionRating.getId());

        return sessionRating;

    }

    @Override
    public SessionRating updateRating(SessionRating newRating) {
        allRatings.put(newRating.getId(), newRating);
        return newRating;

    }

    @Override
    public void deleteRating(String id) {
        SessionRating ratingToBeDeleted = allRatings.get(id);
        if (ratingToBeDeleted == null) {
            return;
        }
        String sessionId = ratingToBeDeleted.getSession();
        String attendeeId = ratingToBeDeleted.getAttendeeId();
        //Remove the session rating from three maps
        //Remove the rating from ratingIdsBySession
        Collection<String> ratingIdsPerSession = ratingIdsBySession.get(sessionId);
        if (ratingIdsPerSession != null) {
            ratingIdsPerSession.remove(id);
            if (ratingIdsPerSession.isEmpty()) {
                ratingIdsBySession.remove(sessionId);
            } else {
                ratingIdsBySession.put(sessionId, ratingIdsPerSession);
            }
        }

        //Remove the rating from ratingIdsByAttendee
        Collection<String> ratingIdsPerAttendee = ratingIdsByAttendee.get(attendeeId);
        ratingIdsPerAttendee.remove(id);
        if (ratingIdsPerAttendee != null) {
            if (ratingIdsPerAttendee.isEmpty()) {
                ratingIdsByAttendee.remove(attendeeId);
            } else {
                ratingIdsByAttendee.put(attendeeId, ratingIdsPerAttendee);
            }
        }

        //Remove the rating from the allRating
        allRatings.remove(id);

    }

    @Override
    public Collection<SessionRating> getRatingsBySession(String sessionId) {
        System.out.println("> allSessionVotes() " + sessionId);
        for (Map.Entry<String, Collection<String>> entry : ratingIdsBySession.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        Set<SessionRating> allSessionVotes = new HashSet<SessionRating>();
        Collection<String> ids = ratingIdsBySession.get(sessionId);
        if ((ids != null) && (!ids.isEmpty())) {
            for (String id : ids) {
                allSessionVotes.add(allRatings.get(id));
            }
        }
        return allSessionVotes;
    }

    @Override
    public Collection<SessionRating> getRatingsByAttendee(String attendeeId) {
        Set<SessionRating> allAttendeeVotes = new HashSet<SessionRating>();
        Collection<String> ids = ratingIdsByAttendee.get(attendeeId);
        if ((ids != null) && (!ids.isEmpty())) {
            for (String id : ids) {
                allAttendeeVotes.add(allRatings.get(id));
            }
        }
        return allAttendeeVotes;
    }

    @Override
    public Collection<SessionRating> getAllRatings() {

        return allRatings.values();
    }

    @Override
    public void clearAllRatings() {
        allRatings.clear();
        ratingIdsBySession.clear();
        ratingIdsByAttendee.clear();

    }

    @Override
    public SessionRating getRating(String id) {
        return allRatings.get(id);
    }

}
