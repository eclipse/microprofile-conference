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

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.ws.microprofile.sample.conference.vote.api.AttendeeProvider;
import com.ibm.ws.microprofile.sample.conference.vote.model.Attendee;
import com.ibm.ws.microprofile.sample.conference.vote.store.AttendeeStore;
import com.ibm.ws.microprofile.sample.conference.vote.store.Persistent;

@ApplicationScoped
@Persistent
public class CouchDBAttendeeStore implements AttendeeStore {

	
	String username="0cbcf4ec-681d-4267-b8b2-63495750b883-bluemix";
	String password="b2701582580bff41f9c9912b3c320f2407b2819a1704b0996543148abed1a52a";
	String host="0cbcf4ec-681d-4267-b8b2-63495750b883-bluemix.cloudant.com";
	String url="https://0cbcf4ec-681d-4267-b8b2-63495750b883-bluemix.cloudant.com";
	
	String usernameAndPassword = username + ":" + password;
    String authorizationHeaderName = "Authorization";
    String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );

    String ifMatchHeaderName = "If-Match";
    
	String dbName = "attendees";
	String dbURL = url + "/" + dbName;
	private Client client;
	
	public CouchDBAttendeeStore() {
		
		
	}
	
	@PostConstruct
	public void connect(){
		try{
	        this.client = ClientBuilder.newClient();
	        this.client.register(CouchIDProvider.class);
	        this.client.register(CouchAllDocsProvider.class);
	        this.client.register(AttendeeProvider.class);
	        
	        WebTarget target = client.target(url);
	        target = target.path(dbName);
	        Invocation.Builder builder = target.request( "application/json" );
	        builder = builder.header( authorizationHeaderName, authorizationHeaderValue );
	        Response response = builder.get();
	        
			int code = response.getStatus();
			
			if(code != 200){
		    	if(code == 404){
		    		
		    		Response putResponse = builder.put(Entity.json(""));
		    		
		    		code = putResponse.getStatus();
		    		
		    		if(code != 201){
		    			throw new RuntimeException("Unable to create couch database: "+putResponse.readEntity(String.class));
		    		}
		    		else{
		    			System.out.println("Couch DB Created: "+dbName);
		    		}
		    	}
		    	else{
		    		throw new RuntimeException("Unable to connect to couch database: "+response.readEntity(String.class));
		    	}
		    }
			System.out.println("Connected to Couch DB: "+dbName);
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	@Override
	public Attendee createNewAttendee(Attendee attendee) {
		
        WebTarget target = client.target(url);
        target = target.path(dbName);
        Invocation.Builder builder = target.request( "application/json" );
        builder = builder.header( authorizationHeaderName, authorizationHeaderValue );
        Response postResponse = builder.post(Entity.json(attendee));
		
		int code = postResponse.getStatus();
	    if(code != 201){
			throw new RuntimeException("Unable to create attendee: "+code);
		}
	    
	    CouchID attendeeID = postResponse.readEntity(CouchID.class);
	    
	    attendee = getAttendee(attendeeID.getId());
		
		return attendee;
	}

	@Override
	public Attendee updateAttendee(Attendee attendee) {
		
		Attendee original = getAttendee(attendee.getId());
		
		WebTarget target = client.target(url);
        target = target.path(dbName);
        target = target.path(attendee.getId());
        
        Invocation.Builder builder = target.request( "application/json" );
        builder = builder.header( authorizationHeaderName, authorizationHeaderValue );
        builder = builder.header( ifMatchHeaderName, original.getRevision() );
        Response response = builder.put(Entity.json(attendee));
		
		int code = response.getStatus();
	    if(code != 201){
			throw new RuntimeException("Unable to update attendee: "+code);
		}
	    
	    CouchID attendeeID = response.readEntity(CouchID.class);
	    
	    attendee = getAttendee(attendeeID.getId());
		
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
	    
	    CouchAllDocs ids = postResponse.readEntity(CouchAllDocs.class);
	    
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
	public Attendee deleteAttendee(String id) {
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
		
		return attendee;
	}

}
