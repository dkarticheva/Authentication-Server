package com.fmi.java.cp;

import java.util.Arrays;
import java.util.List;


public class Validator {
	
	private static List<String> validCommandNames = Arrays.asList("register", "login", "reset-password", "update-user", 
			"logout", "delete-user");
	
	// TODO: remove those!
	final static int USERNAME_INDEX = 1;
	final static int PASSWORD_INDEX = 3;
	final static int OLDPASSWORD_INDEX = 3;
	final static int NEWPASSWORD_INDEX = 5;
	final static int FIRSTNAME_INDEX = 5;
	final static int LASTNAME_INDEX = 7;
	final static int EMAIL_INDEX = 9;
	
	// validation of the commands and their options
	public boolean isValidCommand(String commandName) {
		return validCommandNames.contains(commandName);
	}
	public boolean validateRegisterCommand(String[] words) {
		return words[USERNAME_INDEX].equals("--username") && words[PASSWORD_INDEX].equals("--password") 
				&& words[FIRSTNAME_INDEX].equals("--first-name")
				&& words[LASTNAME_INDEX].equals("--last-name") && words[EMAIL_INDEX].equals("--email");
	}
	public boolean validateLogIn(String[] words) {
		return words[1].equals("-–username") && words[PASSWORD_INDEX].equals("--password");
	}
	public boolean validateLogInSesh(String[] words) {
		return words[1].equals("-–session-id");
	}
	public boolean validateResetPassword(String[] words) {
		return words[1].equals("–-username") && words[OLDPASSWORD_INDEX].equals("--old-password") 
				&& words[NEWPASSWORD_INDEX].equals("--new-password");
	}
	public boolean validateUpdateUser(String[] words) {
		return words[1].equals("-–session-id"); 
	}
	public boolean validateLogOut(String[] words) {
		return words[1].equals("–session-id"); 
	}
	public boolean validateDeleteUser(String[] words) {
		return words[1].equals("–username");
	}
}
