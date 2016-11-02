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

package com.ibm.ws.microprofile.sample.conference.vote.model;

public class Attendee {

	private String id;
	private final String revision;
	private String name;
	
	public Attendee(String name) {
		this(null, null, name);
	}

	public Attendee(String id, String name) {
		this(id, null, name);
	}
	
	public Attendee(String id, String revision, String name) {
		this.id = id;
		this.revision = revision;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getRevision() {
		return revision;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Attendee) {
			Attendee other = (Attendee) o;
			return id == other.getId() && name.equals(other.getName());
		}
		return false;
	}
}
