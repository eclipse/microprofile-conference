package com.ibm.ws.microprofile.sample.conference.vote.utils;

public class Debug {

	private static boolean debug = Boolean.getBoolean("vote.debug");
	
	public static boolean isDebugEnabled() {
		return debug;
	}
}
