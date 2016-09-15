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
@NonPersistent
public class HashMapAttendeeStore implements AttendeeStore {

	private AtomicLong nextAttendeeId = new AtomicLong(0);
	private Map<String,Attendee> attendees = new HashMap<String,Attendee>();
	
	@Override
	public Attendee createNewAttendee(Attendee attendee) {
		String id = ""+nextAttendeeId.incrementAndGet();
		attendee = new Attendee(id, attendee.getName());
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
	public Attendee getAttendee(String id) {
		return attendees.get(id);
	}

	@Override
	public Attendee deleteAttendee(String id) {
		return attendees.remove(id);
	}

}
