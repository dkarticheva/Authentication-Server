package com.fmi.java.cp;

public interface Command {

	public final static int USERNAME_STARTING_INDEX = 2;
	public final static int PASSWORD_STARTING_INDEX = 4;
	public final static int SESSION_ID_STARTING_INDEX = 2;

	public abstract CommandResult execute(String[] commandOptions);

	public static String getUsername(String[] commandOptions) {
		return commandOptions[USERNAME_STARTING_INDEX];
	}

	public static String getPassword(String[] commandOptions) {
		return commandOptions[PASSWORD_STARTING_INDEX];
	}

	public static String getSessionID(String[] commandOptions) {
		return commandOptions[SESSION_ID_STARTING_INDEX];
	}

}
