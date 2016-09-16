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

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;

public class Credentials {

	private String username;
	private String password;
	private String url;

	public Credentials(String username, String password, String url, String vcapServices) {
		if (username != null && password != null && url != null) {
			this.url = url;
			this.username = username;
			this.password = password;
		} else {
			parseVcapServices(vcapServices);
			if (this.username == null || this.password == null || this.url == null) {
				throw new RuntimeException(
						"Database cannot be accessed at this time, something is null. Passed in variables were "
								+ "username=" + username + ", password="
								+ ((password == null) ? "null" : "(non-null password)") + ", url=" + url
								+ ". VCAP_SERVICES values were parsed out as " + "username=" + this.username
								+ ", password=" + ((this.password == null) ? "null" : "(non null password)") + ", url="
								+ this.url);
			}
		}
	}

	private void parseVcapServices(String vcapServicesEnv) {
		if (vcapServicesEnv == null) {
			return;
		}
		JsonObject vcapServices = Json.createReader(new StringReader(vcapServicesEnv)).readObject();
		JsonArray cloudantObjectArray = vcapServices.getJsonArray("cloudantNoSQLDB");
		JsonObject cloudantObject = cloudantObjectArray.getJsonObject(0);
		JsonObject cloudantCredentials = cloudantObject.getJsonObject("credentials");
		JsonString cloudantUsername = cloudantCredentials.getJsonString("username");
		username = cloudantUsername.getString();
		JsonString cloudantPassword = cloudantCredentials.getJsonString("password");
		password = cloudantPassword.getString();
		JsonString cloudantUrl = cloudantCredentials.getJsonString("url");
		url = cloudantUrl.getString();
	}

	public String getUrl() {
		return url;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

}