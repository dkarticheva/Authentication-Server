package com.fmi.java.cp;

import java.io.OutputStream;

public class LogInCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String userName = commandOptions[USERNAME_INDEX];
		String password = commandOptions[PASSWORD_INDEX];
		
		if (!validator.confirmExistenceOfUserWithUsername(userName)) {
			CommandsExecutor.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		User userToLogIn = CommandsExecutor.getUsers().get(userName);
		if (!validator.validatePasswordForUser(userToLogIn, password)) {
			CommandsExecutor.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		// TODO: don't do it like that!
		CommandsExecutor.getLoggedIn().put(userName, userToLogIn);
		
		// in case of a second log in in a row
		if (validator.isUserAlreadyLoggedIn(userName)) {
			CommandsExecutor.removeExpiredSessionForUser(userName, null);
		}
		
		Session newSession = new Session();
		
		// TODO: don't do it like that!
		CommandsExecutor.getSessions().put(newSession, userToLogIn);
		
		String confirmationMessage = "User %s has been successfully logged\n Session with id %s and ttl %s has been created\n";
		CommandsExecutor.sendServerMessageToSocket(String.format(confirmationMessage, userName, newSession.getID(), newSession.getTimeToLive()), communicationSocketOutputStream);
		return true;
	}

}
