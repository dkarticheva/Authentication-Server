package com.fmi.java.cp;

import java.io.OutputStream;

public class LogOutCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String sessionId = commandOptions[2];
		
		if (!validator.validateSession(sessionId)) {
			CommandsExecutor.sendServerMessageToSocket("Invalid session id!\n", communicationSocketOutputStream);
			return false;
		}
		
		Session session = validator.getSessionFromId(sessionId);
		User userToLogOut = CommandsExecutor.getSessions().get(session);
		
		if (validator.isSessionExpiredForUser(userToLogOut)) {
			CommandsExecutor.sendServerMessageToSocket("Your session has expired! Please log in again\n", communicationSocketOutputStream);
			CommandsExecutor.removeExpiredSessionForUser(userToLogOut.getUsername(), null);
			return false;
		}
		// TODO: handle these better!
		CommandsExecutor.removeExpiredSessionForUser(userToLogOut.getUsername(), sessionId);
		CommandsExecutor.getLoggedIn().remove(userToLogOut.getUsername());
		
		String confirmationMessage = "User %s has logged out and session with id %s has been terminated\n";
		CommandsExecutor.sendServerMessageToSocket(String.format(confirmationMessage, userToLogOut.getUsername(), sessionId), communicationSocketOutputStream);
		return true;
	}

}
