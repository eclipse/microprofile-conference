package com.ibm.ws.microprofile.sample.conference.vote.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


public class VoteApplication extends Application {

	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        //classes.add(SessionVote().class);
        classes.add(AttendeeProvider.class);
        classes.add(SessionRatingProvider.class);
        classes.add(SessionRatingListProvider.class);
        return classes;
    }
	
	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<Object>();
		singletons.add(new SessionVote());
		return singletons;
	}
}
