/*
 * (C) Copyright IBM Corporation 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.ws.microprofile.sample.conference.vote.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;

import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;

@ApplicationScoped
@Persistent
public class CouchDBAttendeeStore implements AttendeeStore {

	private AtomicLong nextAttendeeId = new AtomicLong(0);
	private Map<Long,Attendee> attendees = new HashMap<Long,Attendee>();
	
	@Override
	public Attendee createNewAttendee(String name) {
		Long id = nextAttendeeId.incrementAndGet();
		Attendee attendee = new Attendee(id, name);
		attendees.put(id, attendee);
		return attendee;
	}

	@Override
	public Attendee updateAttendee(Attendee attendee) {
		attendees.put(attendee.getId(), attendee);
		return attendee;
	}

	@Override
	public Collection<Attendee> getAllAttendees() {
		return attendees.values();
	}

	@Override
	public void clearAllAttendees() {
		attendees.clear();
	}

	@Override
	public Attendee getAttendee(Long id) {
		return attendees.get(id);
	}

}
