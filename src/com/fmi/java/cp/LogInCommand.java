package com.fmi.java.cp;

import java.io.OutputStream;

public class LogInCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String userName = commandOptions[USERNAME_INDEX];
		String password = commandOptions[PASSWORD_INDEX];
		
		if (!UserOperations.confirmExistenceOfUserWithUsername(userName)) {
			ServerThread.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		User userToLogIn = UserOperations.getUserByUsername(userName);
		if (!UserOperations.validatePasswordForUser(userToLogIn, password)) {
			ServerThread.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		UserOperations.logInUser(userToLogIn);
		
		// in case of a second log in in a row
		if (UserOperations.isUserAlreadyLoggedIn(userName)) {
			SessionOperations.removeSessionForUser(userToLogIn);
		}
		
		Session newSession = new Session();
		SessionOperations.addSessionForUser(newSession, userToLogIn);
		
		String confirmationMessage = "User %s has been successfully logged\n Session with id %s and ttl %s has been created\n";
		ServerThread.sendServerMessageToSocket(String.format(confirmationMessage, userName, newSession.getID(), newSession.getTimeToLive()), communicationSocketOutputStream);
		return true;
	}

}
