package com.fmi.java.cp;

public interface Command {
	
	// TODO: just no
	final static int USERNAME_INDEX = 2;
	final static int PASSWORD_INDEX = 4;
	final static int NEWPASSWORD_INDEX = 6;
	final static int OLDPASSWORD_INDEX = 4;
	final static int FIRSTNAME_INDEX = 6;
	final static int LASTNAME_INDEX = 8;
	final static int EMAIL_INDEX = 10;
	
	public abstract CommandResult execute(String[] commandOptions);
	
}
