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

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.ws.microprofile.sample.conference.vote.api.AttendeeProvider;

@ApplicationScoped
public class CouchConnection {

	public enum RequestType {
		GET, POST, PUT, DELETE
	}
	
	String username="0cbcf4ec-681d-4267-b8b2-63495750b883-bluemix";
	String password="b2701582580bff41f9c9912b3c320f2407b2819a1704b0996543148abed1a52a";
	String host="0cbcf4ec-681d-4267-b8b2-63495750b883-bluemix.cloudant.com";
	String url="https://0cbcf4ec-681d-4267-b8b2-63495750b883-bluemix.cloudant.com";
	
	Credentials credentials = new Credentials(username, password, url, null);
	
	String usernameAndPassword = credentials.getUsername() + ":" + credentials.getPassword();
    String authorizationHeaderName = "Authorization";
    String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );

    String ifMatchHeaderName = "If-Match";
    
	String dbName = "attendees";
	String dbURL = url + "/" + dbName;
	private Client client;
	
	boolean connected = false;
	private WebTarget databaseTarget;
	
	@PostConstruct
	public void connect(){
		try{
	        this.client = ClientBuilder.newClient();
	        this.client.register(CouchIDProvider.class);
	        this.client.register(AllDocsProvider.class);
	        this.client.register(AttendeeProvider.class);
	        
	        this.databaseTarget = client.target(url);
	        this.databaseTarget = this.databaseTarget.path(dbName);
	        Invocation.Builder builder = databaseTarget.request( "application/json" );
	        builder = builder.header( authorizationHeaderName, authorizationHeaderValue );
	        Response response = builder.get();
	        
			int code = response.getStatus();
			
			if(code != 200){
		    	if(code == 404){
		    		
		    		Response putResponse = builder.put(Entity.json(""));
		    		
		    		code = putResponse.getStatus();
		    		
		    		if(code != 201){
		    			System.out.println("Unable to create couch database: "+putResponse.readEntity(String.class));
		    		}
		    		else{
		    			System.out.println("Couch DB Created: "+dbName);
		    			connected = true;
		    		}
		    	}
		    	else{
		    		System.out.println("Unable to connect to couch database: "+response.readEntity(String.class));
		    	}
		    }
			else{
				connected = true;
			}
			
			if(connected){
				System.out.println("Connected to Couch DB: "+dbName);
			}
		}
		catch(Throwable t){
			t.printStackTrace();
			System.out.println("Unable to connect to couch database: "+dbName);
		}
	}
	
	public <T, R> R request(String path, RequestType requestType, T payload, Class<R> returnType, String etag, int...expectedStatus) {
		
        WebTarget target = databaseTarget.path(dbName);
        if(path != null && !path.equals("")){
        	target = databaseTarget.path(path);
        }
        Invocation.Builder builder = target.request( "application/json" );
        builder = builder.header( authorizationHeaderName, authorizationHeaderValue );
        if(etag != null){
        	builder = builder.header( ifMatchHeaderName, etag );
        }
        
        Entity<T> entity = Entity.json(payload);
        Response response = null;
        switch(requestType){
		case PUT:
			response = builder.put(entity);
			break;
		case POST:
			response = builder.post(entity);
			break;
		case GET:
			response = builder.get();
			break;
		case DELETE:
			response = builder.delete();
			break;
		}
        
		int code = response.getStatus();
	    if(!Arrays.asList(expectedStatus).contains(code)){
			throw new RuntimeException("Unable to execute request: "+code);
		}
	    
	    R returnedPayload = null;
	    if(returnType != null){
	    	returnedPayload = response.readEntity(returnType);
	    }
		
		return returnedPayload;
	}

	public boolean isAccessible() {
		return connected;
	}

}
