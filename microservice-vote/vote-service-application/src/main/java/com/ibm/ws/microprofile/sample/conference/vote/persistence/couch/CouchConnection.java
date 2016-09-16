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
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.ibm.ws.microprofile.sample.conference.vote.api.AttendeeProvider;
import com.ibm.ws.microprofile.sample.conference.vote.api.SessionRatingProvider;

@Dependent
public class CouchConnection {

	@Inject
	private Credentials credentials;
	
	public enum RequestType {
		GET, POST, PUT, DELETE
	}
	
	private String usernameAndPassword;
	private String authorizationHeaderName = "Authorization";
	private String authorizationHeaderValue;

	private String ifMatchHeaderName = "If-Match";
    
	private String dbName = "attendees";
	private Client client;
	
	boolean connected = false;
	private String url;
	
	public boolean connect(String dbName){
		if(!connected){
			this.dbName = dbName;
			this.url = credentials.getUrl();
			this.usernameAndPassword = credentials.getUsername() + ":" + credentials.getPassword();
			this.authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );

			try{
		        this.client = ClientBuilder.newClient();
		        this.client.register(CouchIDProvider.class);
		        this.client.register(AllDocsProvider.class);
		        this.client.register(AttendeeProvider.class);
		        this.client.register(SessionRatingProvider.class);
		        
		        WebTarget databaseTarget = client.target(url);
		        databaseTarget = databaseTarget.path(dbName);
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
					MultivaluedMap<String, Object> headers = response.getHeaders();
					for(Map.Entry<String, List<Object>> entry:headers.entrySet()){
						for(Object value:entry.getValue()){
							System.out.println(entry.getKey() + " : " + value);
						}
					}
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
		return connected;
	}
	
	public <T, R> R request(String path, RequestType requestType, T payload, Class<R> returnType, String etag, int expectedStatus) {
		return request(path, null, null, requestType, payload, returnType, etag, expectedStatus);
	}
	
	public <T, R> R request(String path, String queryName, String queryValue, RequestType requestType, T payload, Class<R> returnType, String etag, int expectedStatus) {
		
		WebTarget target = client.target(url);
		target = target.path(dbName);
        if(path != null && !path.equals("")){
        	target = target.path(path);
        }
        
        if(queryName != null && !queryName.equals("")){
        	target = target.queryParam(queryName, queryValue);
        }
        
        System.out.println(requestType +" : "+target.getUri());
        
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
	    if(code != expectedStatus){
	    	String error = response.readEntity(String.class);
	    	throw new RequestStatusException("Unable to execute request: "+code+" : "+error, code);
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
