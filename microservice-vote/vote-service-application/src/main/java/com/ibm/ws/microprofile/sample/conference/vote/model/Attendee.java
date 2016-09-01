package com.ibm.ws.microprofile.sample.conference.vote.model;

public class Attendee {

	private final long id;
	private String name;
	

	public Attendee(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
