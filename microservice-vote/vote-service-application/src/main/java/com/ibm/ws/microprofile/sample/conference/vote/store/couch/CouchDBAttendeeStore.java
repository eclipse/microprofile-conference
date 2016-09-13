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

package com.ibm.ws.microprofile.sample.conference.vote.store.couch;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.store.AttendeeStore;
import com.ibm.ws.microprofile.sample.conference.vote.store.Persistent;

@ApplicationScoped
@Persistent
public class CouchDBAttendeeStore implements AttendeeStore {

	@Override
	public Attendee createNewAttendee(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attendee updateAttendee(Attendee attendee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Attendee> getAllAttendees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearAllAttendees() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Attendee getAttendee(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attendee deleteAttendee(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
