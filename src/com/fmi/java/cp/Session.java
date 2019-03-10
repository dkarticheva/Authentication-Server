package com.fmi.java.cp;

import java.util.concurrent.TimeUnit;
import java.util.UUID;

public class Session {
	
	private String id;
	private long createdAt;
	private final long ttl = 90_000;
	
	public Session() {
		createdAt = System.currentTimeMillis();
		id = UUID.randomUUID().toString();
	}
	
	public boolean hasExpired() {
		long timeNow = System.currentTimeMillis();
		return (createdAt + ttl) < timeNow;
	}
	
	public String getID() {
		return id;
	}
	
	public String getTimeToLive() {
		return String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(ttl), 
				TimeUnit.MILLISECONDS.toSeconds(ttl) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ttl)));
	}

}
