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
import com.ibm.ws.microprofile.sample.conference.vote.model.SessionRating;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.Persistent;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.SessionRatingDAO;

@ApplicationScoped
@Persistent
public class CouchDBSessionRatingDAO implements SessionRatingDAO{

	@Override
	public SessionRating rateSession(SessionRating sessionRating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionRating updateRating(SessionRating sessionRating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRating(String id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Collection<SessionRating> getRatingsBySession(String session) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Collection<SessionRating> getRatingsByAttendee(Attendee attendee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<SessionRating> getAllRatings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearAllRatings() {
		// TODO Auto-generated method stub
		
	}



}
