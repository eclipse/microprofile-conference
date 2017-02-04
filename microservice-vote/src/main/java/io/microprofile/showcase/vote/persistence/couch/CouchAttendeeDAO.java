/*
 * Copyright (c) 2016 IBM, and others
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
package io.microprofile.showcase.vote.persistence.couch;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.microprofile.showcase.vote.model.Attendee;
import io.microprofile.showcase.vote.persistence.AttendeeDAO;
import io.microprofile.showcase.vote.persistence.Persistent;
import io.microprofile.showcase.vote.persistence.couch.CouchConnection.RequestType;

@ApplicationScoped
@Persistent
public class CouchAttendeeDAO implements AttendeeDAO {

    @Inject
    CouchConnection couch;

    private String allView = "function (doc) {emit(doc._id, 1)}";

    private String designDoc = "{\"views\":{"
                               + "\"all\":{\"map\":\"" + allView + "\"}}}";

    private boolean connected;

    @PostConstruct
    public void connect() {
        this.connected = couch.connect("attendees");

        if (this.connected) {
            String design = couch.request("_design/attendees", RequestType.GET, null, String.class, null, 200, true);
            if (design == null) {
                couch.request("_design/attendees", RequestType.PUT, designDoc, null, null, 201);
            }
        }
    }

    @Override
    public Attendee createNewAttendee(Attendee attendee) {

        CouchID attendeeID = couch.request(null, RequestType.POST, attendee, CouchID.class, null, 201);
        attendee = getAttendee(attendeeID.getId());

        return attendee;
    }

    @Override
    public Attendee updateAttendee(Attendee attendee) {

        Attendee original = getAttendee(attendee.getId());

        couch.request(attendee.getId(), RequestType.PUT, attendee, null, null, 201);

        attendee = getAttendee(attendee.getId());

        return attendee;
    }

    @Override
    public Collection<Attendee> getAllAttendees() {

        AllDocs allDocs = couch.request("_design/attendees/_view/all", RequestType.GET, null, AllDocs.class, null, 200);

        Collection<Attendee> attendees = new ArrayList<Attendee>();
        for (String id : allDocs.getIds()) {
            Attendee attendee = getAttendee(id);
            attendees.add(attendee);
        }

        return attendees;
    }

    @Override
    public void clearAllAttendees() {
        AllDocs allDocs = couch.request("_design/attendees/_view/all", RequestType.GET, null, AllDocs.class, null, 200);

        for (String id : allDocs.getIds()) {
            deleteAttendee(id);
        }
    }

    @Override
    public Attendee getAttendee(String id) {
        Attendee attendee = couch.request(id, RequestType.GET, null, Attendee.class, null, 200, true);
        return attendee;
    }

    @Override
    public void deleteAttendee(String id) {
        Attendee attendee = getAttendee(id);

        couch.request(id, RequestType.DELETE, null, null, null, 200);
    }

    @Override
    public boolean isAccessible() {
        return connected;
    }

}
