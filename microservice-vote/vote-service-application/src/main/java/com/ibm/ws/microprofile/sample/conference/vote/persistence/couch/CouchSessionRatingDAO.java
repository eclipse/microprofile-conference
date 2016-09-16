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
package com.ibm.ws.microprofile.sample.conference.vote.persistence.couch;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.model.SessionRating;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.Persistent;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.SessionRatingDAO;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.couch.CouchConnection.RequestType;

@ApplicationScoped
@Persistent
public class CouchSessionRatingDAO implements SessionRatingDAO{

	@Inject
	CouchConnection couch;
	
	private String allView = "function (doc) {emit(doc._id, 1)}";

	private String sessionView = "function (doc) {emit(doc.session, doc._id)}";
	private String attendeeView = "function (doc) {emit(doc.attendeeId, doc._id)}";
	
	private String designDoc = "{\"views\":{"
							    + "\"all\":{\"map\":\""+allView+"\"},"
							  	+ "\"session\":{\"map\":\""+sessionView+"\"},"
							  	+ "\"attendee\":{\"map\":\""+attendeeView+"\"}}}";

	
	private boolean connected;
	
	@PostConstruct
	public void connect(){
		this.connected = couch.connect("ratings");
		
		try{
			couch.request("_design/ratings", RequestType.GET, null, null, null, 200);
		}
		catch(RequestStatusException e){
			if(e.getCode() == 404){
				couch.request("_design/ratings", RequestType.PUT, designDoc, null, null, 201);
			}
			else{
				throw e;
			}
		}
	}
	
	@Override
	public SessionRating rateSession(SessionRating sessionRating) {
		
		CouchID ratingID = couch.request(null, RequestType.POST, sessionRating, CouchID.class, null, 201);
		sessionRating = getSessionRating(ratingID.getId());

		return sessionRating;
		
	}

	private SessionRating getSessionRating(String id) {
		SessionRating sessionRating = couch.request(id, RequestType.GET, null, SessionRating.class, null, 200);
		return sessionRating;
	}

	@Override
	public SessionRating updateRating(SessionRating newRating) {
		SessionRating original = getSessionRating(newRating.getId());
		
		couch.request(newRating.getId(), RequestType.PUT, newRating, null, original.getRevision(), 201);
		
		newRating = getSessionRating(newRating.getId());
		return newRating;
	}

  	@Override
	public void deleteRating(String id) {
  		
  		SessionRating original = getSessionRating(id);
		
  		couch.request(id, RequestType.DELETE, null, null, original.getRevision(), 200);
  	}

	

	@Override
	public Collection<SessionRating> getRatingsBySession(String sessionId) {
		return querySessionRating("session", sessionId);
	}
	
	@Override
	public Collection<SessionRating> getRatingsByAttendee(String attendeeId) {
		return querySessionRating("attendee", attendeeId);
	}

	private Collection<SessionRating> querySessionRating(String query, String value) {
		
		AllDocs allDocs = couch.request("_design/ratings/_view/"+query,"key","\""+value+"\"", RequestType.GET, null, AllDocs.class, null, 200);
	    
	    Collection<SessionRating> ratings = new ArrayList<SessionRating>();
	    for(String id:allDocs.getIds()){
	    	SessionRating rating  = getSessionRating(id);
	    	ratings.add(rating);
	    }
	    
		return ratings;
	}
	
	
	@Override
	public Collection<SessionRating> getAllRatings() {
		
		AllDocs allDocs = couch.request("_design/ratings/_view/all", RequestType.GET, null, AllDocs.class, null, 200);
	    
	    Collection<SessionRating> sessionRatings = new ArrayList<SessionRating>();
	    for(String id:allDocs.getIds()){
	    	SessionRating sessionRating  = getSessionRating(id);
	    	sessionRatings.add(sessionRating);
	    }
	    
		return sessionRatings;
	}

	@Override
	public void clearAllRatings() {
		AllDocs allDocs = couch.request("_design/ratings/_view/all", RequestType.GET, null, AllDocs.class, null, 200);
	    
	    for(String id:allDocs.getIds()){
	    	deleteSessionRating(id);
	    }
		
	}

	private void deleteSessionRating(String id) {
		SessionRating sessionRating = getSessionRating(id);
		
		couch.request(id, RequestType.DELETE, null, null, sessionRating.getRevision(), 200);
	}

}
