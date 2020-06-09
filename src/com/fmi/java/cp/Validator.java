package com.fmi.java.cp;

import java.util.Arrays;
import java.util.List;

import com.fmi.java.cp.Validator;


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
	
	private static boolean isCommandNameValid(String commandName) {
		return validCommandNames.contains(commandName);
	}
	
	private static boolean areCommandOptionsValidAccordingCommandName(String[] commandLine) {
		
		String commandName = commandLine[0];
		String firstOption = commandLine[1];
		switch (commandName) {
			case "register" : return Validator.validateRegisterCommand(commandLine);
		
			case "reset-password" : return Validator.validateResetPassword(commandLine);
			
			case "update-user" : return Validator.validateUpdateUser(commandLine);
			
			case "logout" : return Validator.validateLogOut(commandLine);
			
			case "delete-user" : return Validator.validateDeleteUser(commandLine);
			
			case "login" : return firstOption.equals("-–username") ?  Validator.validateLogIn(commandLine) : Validator.validateLogInSesh(commandLine);
		}
		
		return false;
	}
	
	public static boolean isValidCommand(String commandLine) {
		
		String[] commandNameAndOptions = commandLine.split(" ");
		String commandName = commandNameAndOptions[0];
		
		if (!isCommandNameValid(commandName)) {
			System.out.println("Invalid command!");
			return false;
			
		} else if(!areCommandOptionsValidAccordingCommandName(commandNameAndOptions)) {
			System.out.println("Incorrect command options!");
			return false;
		}
		
		return true;
	}
	
	public static boolean validateRegisterCommand(String[] words) {
		return words[USERNAME_INDEX].equals("--username") && words[PASSWORD_INDEX].equals("--password") 
				&& words[FIRSTNAME_INDEX].equals("--first-name")
				&& words[LASTNAME_INDEX].equals("--last-name") && words[EMAIL_INDEX].equals("--email");
	}
	public static boolean validateLogIn(String[] words) {
		return words[1].equals("-–username") && words[PASSWORD_INDEX].equals("--password");
	}
	public static boolean validateLogInSesh(String[] words) {
		return words[1].equals("-–session-id");
	}
	public static boolean validateResetPassword(String[] words) {
		return words[1].equals("–-username") && words[OLDPASSWORD_INDEX].equals("--old-password") 
				&& words[NEWPASSWORD_INDEX].equals("--new-password");
	}
	public static boolean validateUpdateUser(String[] words) {
		return words[1].equals("-–session-id"); 
	}
	public static boolean validateLogOut(String[] words) {
		return words[1].equals("–session-id"); 
	}
	public static boolean validateDeleteUser(String[] words) {
		return words[1].equals("–username");
	}
}
