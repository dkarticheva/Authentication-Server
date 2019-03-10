package com.fmi.java.cp;

import java.util.Map;
import java.util.Set;


public class Validator {
	
	final static int USERNAME_INDEX = 1;
	final static int PASSWORD_INDEX = 3;
	final static int OLDPASSWORD_INDEX = 3;
	final static int NEWPASSWORD_INDEX = 5;
	final static int FIRSTNAME_INDEX = 5;
	final static int LASTNAME_INDEX = 7;
	final static int EMAIL_INDEX = 9;
	
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
	
	public boolean validateUser(String userName) {
		Map<String, User> usrs = CommandsExecutor.getUsers();
		return usrs.containsKey(userName);
	}
	public boolean validatePassword(User user, String password) {
		return Password.authenticate(password, user.getPassword());
	}
	public boolean validateSession(String sID) {
		Map<Session, User> seshs = CommandsExecutor.getSessions();
		Set<Session> sesh = seshs.keySet();
		for (Session s : sesh) {
			if (s.getID().equals(sID)) {
				return true;
			}
		}
		return false;
	}
	public Session findSessionFromID(String sID) {
		Map<Session, User> seshs = CommandsExecutor.getSessions();
		Set<Session> sesh = seshs.keySet();
		for (Session s : sesh) {
			if (s.getID().equals(sID)) {
				return s;
			}
		}
		return null;
	}
	public boolean alreadyLoggedIn(String userName) {
		return (CommandsExecutor.getLoggedIn().containsKey(userName));
	}
	public boolean isValidCommand(String cmd) {
		return cmd.equals("register") || cmd.equals("login") || cmd.equals("reset-password") 
				|| cmd.equals("update-user") || cmd.equals("logout") || cmd.equals("delete-user");
	}
	public boolean validOptions(String[] words) {
		Validator validator = new Validator();
		switch (words[0]) {
			case "register" : return validator.validateRegisterCommand(words);
		
			case "reset-password" : return validator.validateResetPassword(words);
			
			case "update-user" : return validator.validateUpdateUser(words);
			
			case "logout" : return validator.validateLogOut(words);
			
			case "delete-user" : return validator.validateDeleteUser(words);
			
		}
		if (words[0].equals("login")) {
			if (words[1].equals("-–username")) {
				return validator.validateLogIn(words);
			}
			else {
				return validator.validateLogInSesh(words);
			}
		}
		return false;
	}
}
