package com.fmi.java.cp;

import java.util.Arrays;
import java.util.List;

import com.fmi.java.cp.Validator;


public class Validator {
	
	private static List<String> validCommandNames = Arrays.asList("register", "login", "reset-password", "update-user", 
			"logout", "delete-user");
	
	private final static int USERNAME_INDEX = 1;
	private final static int PASSWORD_INDEX = 3;
	private final static int OLDPASSWORD_INDEX = 3;
	private final static int NEWPASSWORD_INDEX = 5;
	private final static int FIRSTNAME_INDEX = 5;
	private final static int LASTNAME_INDEX = 7;
	private final static int EMAIL_INDEX = 9;
	
	public static boolean isValidCommand(String commandLine) {
		
		String[] commandNameAndOptions = commandLine.split(" ");
		String commandName = commandNameAndOptions[0];
		
		if (isCommandNameInvalid(commandName)) {
			System.out.println("Invalid command!");
			return false;
			
		} else if(areCommandOptionsInvalidAccordingCommandName(commandNameAndOptions)) {
			System.out.println("Incorrect command options!");
			return false;
		}
		
		return true;
	}
	
	private static boolean isCommandNameInvalid(String commandName) {
		return !validCommandNames.contains(commandName);
	}
	
	private static boolean areCommandOptionsInvalidAccordingCommandName(String[] commandLine) {
		
		String commandName = commandLine[0];
		String firstOption = commandLine[1];
		switch (commandName) {
			case "register" : return isRegisterCommandInvalid(commandLine);
		
			case "reset-password" : return isResetPasswordCommandInvalid(commandLine);
			
			case "update-user" : return isUpdateUserCommandInvalid(commandLine);
			
			case "logout" : return isLogoutCommandInvalid(commandLine);
			
			case "delete-user" : return isDeleteUserCommandInvalid(commandLine);
			
			case "login" : return firstOption.equals("-–username") ?  isLoginCommandInvalid(commandLine) : isLoginCommandWithSessionInvalid(commandLine);
		}
		
		return true;
	}
	
	public static boolean isRegisterCommandInvalid(String[] words) {
		return !(words[USERNAME_INDEX].equals("--username") && words[PASSWORD_INDEX].equals("--password") 
				&& words[FIRSTNAME_INDEX].equals("--first-name")
				&& words[LASTNAME_INDEX].equals("--last-name") && words[EMAIL_INDEX].equals("--email"));
	}
	
	public static boolean isLoginCommandInvalid(String[] words) {
		return !(words[1].equals("-–username") && words[PASSWORD_INDEX].equals("--password"));
	}
	
	public static boolean isLoginCommandWithSessionInvalid(String[] words) {
		return !(words[1].equals("-–session-id"));
	}
	
	public static boolean isResetPasswordCommandInvalid(String[] words) {
		return !(words[1].equals("–-username") && words[OLDPASSWORD_INDEX].equals("--old-password") 
				&& words[NEWPASSWORD_INDEX].equals("--new-password"));
	}
	
	public static boolean isUpdateUserCommandInvalid(String[] words) {
		return !(words[1].equals("-–session-id")); 
	}
	
	public static boolean isLogoutCommandInvalid(String[] words) {
		return !(words[1].equals("–session-id")); 
	}
	
	public static boolean isDeleteUserCommandInvalid(String[] words) {
		return !(words[1].equals("–username"));
	}
}
