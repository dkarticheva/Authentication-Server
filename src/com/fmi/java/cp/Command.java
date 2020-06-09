package com.fmi.java.cp;

public interface Command {
	
    final static int USERNAME_STARTING_INDEX = 2;
	final static int PASSWORD_STARTING_INDEX = 4;
	final static int SESSION_ID_STARTING_INDEX = 2;
	
	public static String getUsername(String[] commandOptions) {
		return commandOptions[USERNAME_STARTING_INDEX];
	}
	
	public static String getPassword(String[] commandOptions) {
		return commandOptions[PASSWORD_STARTING_INDEX];
	}
	
	public static String getSessionID(String[] commandOptions) {
		return commandOptions[SESSION_ID_STARTING_INDEX];
	}
	
	public abstract CommandResult execute(String[] commandOptions);
	
}
