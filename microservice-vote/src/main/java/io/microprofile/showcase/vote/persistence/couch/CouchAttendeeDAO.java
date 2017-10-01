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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import org.eclipse.microprofile.metrics.annotation.Timed;

import io.microprofile.showcase.vote.model.Attendee;
import io.microprofile.showcase.vote.persistence.AttendeeDAO;
import io.microprofile.showcase.vote.persistence.Persistent;
import io.microprofile.showcase.vote.persistence.couch.CouchConnection.RequestType;

@ApplicationScoped
@Persistent
@Timeout(1000)
@Health
public class CouchAttendeeDAO implements AttendeeDAO, HealthCheck {

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
        couch.request(attendee.getId(), RequestType.PUT, attendee, null, null, 201);
        return getAttendee(attendee.getId());
    }

    @Override
    @Timeout(5000)
    public Collection<Attendee> getAllAttendees() {
        AllDocs allDocs = couch.request("_design/attendees/_view/all", RequestType.GET, null, AllDocs.class, null, 200);

        // Request a future for each Attendee
        List<Future<Attendee>> futureAttendees = new ArrayList<>();
        allDocs.getIds().forEach(id -> futureAttendees.add(getAttendeeAsync(id)));

        // Once all requests have been made, block for results to build list
        Collection<Attendee> attendees = new ArrayList<>();
        futureAttendees.forEach(futureAttendee -> {
			try {
				attendees.add(futureAttendee.get());
			} catch (InterruptedException | ExecutionException ignore) {
			}
		});
        return attendees;
    }
    
    @Asynchronous
    @Bulkhead(3)
    private Future<Attendee> getAttendeeAsync(String id) {
    	return CompletableFuture.completedFuture(getAttendee(id));
    }

    @Override
    @Timeout(5000)
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
        couch.request(id, RequestType.DELETE, attendee, null, attendee.getRev(), 200);
    }

    @Override
    public boolean isAccessible() {
        return connected;
    }

	@Override
	public HealthCheckResponse call() {
		HealthCheckResponseBuilder b = HealthCheckResponse.named(CouchAttendeeDAO.class.getSimpleName());
		return connected  ? b.up().build()  : b.down().build();
	}

}
