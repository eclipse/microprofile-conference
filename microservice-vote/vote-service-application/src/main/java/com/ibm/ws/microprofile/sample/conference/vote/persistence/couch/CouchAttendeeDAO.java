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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.ws.microprofile.sample.conference.vote.api.AttendeeProvider;
import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.AttendeeDAO;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.Persistent;
import com.ibm.ws.microprofile.sample.conference.vote.persistence.couch.CouchConnection.RequestType;

@ApplicationScoped
@Persistent
public class CouchAttendeeDAO implements AttendeeDAO {

	@Inject
	CouchConnection couch;
	
	@Override
	public Attendee createNewAttendee(Attendee attendee) {
		
		CouchID attendeeID = couch.request(null, RequestType.POST, attendee, CouchID.class, null, 201);
		attendee = getAttendee(attendeeID.getId());
		
		return attendee;
	}

	@Override
	public Attendee updateAttendee(Attendee attendee) {
		
		Attendee original = getAttendee(attendee.getId());
		
		couch.request(attendee.getId(), RequestType.PUT, attendee, null, original.getRevision(), 201);
		
		attendee = getAttendee(attendee.getId());
		
		return attendee;
	}

	@Override
	public Collection<Attendee> getAllAttendees() {
		WebTarget target = client.target(url);
        target = target.path(dbName);
        target = target.path("_all_docs");
        
        Invocation.Builder builder = target.request( "application/json" );
        builder = builder.header( authorizationHeaderName, authorizationHeaderValue );
        Response postResponse = builder.get();
		
		int code = postResponse.getStatus();
	    if(code != 200){
			throw new RuntimeException("Unable to retrieve all docs: "+code);
		}
	    
	    AllDocs ids = postResponse.readEntity(AllDocs.class);
	    
	    Collection<Attendee> attendees = new ArrayList<Attendee>();
	    for(String id:ids.getIds()){
	    	Attendee attendee  = getAttendee(id);
	    	attendees.add(attendee);
	    }
	    
		return attendees;
	}

	@Override
	public void clearAllAttendees() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Attendee getAttendee(String id) {
        WebTarget target = client.target(url);
        target = target.path(dbName);
        target = target.path(id);
        
        Invocation.Builder builder = target.request( "application/json" );
        builder = builder.header( authorizationHeaderName, authorizationHeaderValue );
        Response postResponse = builder.get();
		
		int code = postResponse.getStatus();
	    if(code != 200){
			throw new RuntimeException("Unable to retrieve attendee: "+code);
		}
	    
	    Attendee attendee = postResponse.readEntity(Attendee.class);
	    
		return attendee;
	}

	@Override
	public void deleteAttendee(String id) {
		Attendee attendee = getAttendee(id);
		
		WebTarget target = client.target(url);
        target = target.path(dbName);
        target = target.path(id);
        target = target.queryParam("rev", attendee.getRevision());
        
        Invocation.Builder builder = target.request( "application/json" );
        builder = builder.header( authorizationHeaderName, authorizationHeaderValue );
        Response postResponse = builder.delete();
		
		int code = postResponse.getStatus();
	    if(code != 200){
			throw new RuntimeException("Unable to delete attendee: "+code);
		}
	}

	@Override
	public boolean isAccessible() {
		return couch.isAccessible();
	}

}
