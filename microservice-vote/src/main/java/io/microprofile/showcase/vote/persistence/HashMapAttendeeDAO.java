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

package io.microprofile.showcase.vote.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import io.microprofile.showcase.vote.model.Attendee;

@ApplicationScoped
@NonPersistent
public class HashMapAttendeeDAO implements AttendeeDAO {

	private Map<String,Attendee> attendees = new HashMap<String,Attendee>();
	
	@Override
	public Attendee createNewAttendee(Attendee attendee) {
		String attendeeId = UUID.randomUUID().toString();
		attendee = new Attendee(attendeeId, attendee.getName());
		attendees.put(attendeeId, attendee);
		return attendee;
	}

	@Override
	public Attendee updateAttendee(Attendee attendee) {
		attendees.put(String.valueOf(attendee.getId()), attendee);
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
	public void deleteAttendee(String id) {
		 attendees.remove(id);
	}

	@Override
	public boolean isAccessible() {
		// TODO Auto-generated method stub
		return true;
	}

}
