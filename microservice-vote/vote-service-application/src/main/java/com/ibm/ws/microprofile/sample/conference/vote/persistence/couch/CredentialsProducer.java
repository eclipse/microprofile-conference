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

import java.io.StringReader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;

@ApplicationScoped
public class CredentialsProducer {

	@Produces
	public Credentials newCredentials(){
		Credentials credentials = null;
		String vcap = System.getenv("VCAP_SERVICES");
		if(vcap != null){
			credentials = parseVcap(vcap);
		}
		else{
			credentials = useEnv();
		}
		
		return credentials;
	}
	
	private Credentials parseVcap(String vcapServices) {
		
		JsonObject vcapServicesJson = Json.createReader(new StringReader(vcapServices)).readObject();
		JsonArray cloudantObjectArray = vcapServicesJson.getJsonArray("cloudantNoSQLDB");
		JsonObject cloudantObject = cloudantObjectArray.getJsonObject(0);
		JsonObject cloudantCredentials = cloudantObject.getJsonObject("credentials");
		JsonString cloudantUsername = cloudantCredentials.getJsonString("username");
		
		JsonString cloudantPassword = cloudantCredentials.getJsonString("password");
		JsonString cloudantUrl = cloudantCredentials.getJsonString("url");
		
		String username = cloudantUsername.getString();
		String password = cloudantPassword.getString();
		String url = cloudantUrl.getString();
		
		return new Credentials(username, password, url);		
	}
	
	private Credentials useEnv() {
		
		String username = System.getenv("dbUsername");
		String password = System.getenv("dbPassword");
		String url = System.getenv("dbUrl");
		
		//TEMP for testing only
				if(url == null){
					username="0cbcf4ec-681d-4267-b8b2-63495750b883-bluemix";
					password="b2701582580bff41f9c9912b3c320f2407b2819a1704b0996543148abed1a52a";
					url="https://0cbcf4ec-681d-4267-b8b2-63495750b883-bluemix.cloudant.com";
				}
		
		return new Credentials(username, password, url);		
	}
	
}