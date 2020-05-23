package com.fmi.java.cp;

import java.io.OutputStream;

public class LogOutCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String sessionId = commandOptions[2];
		
		if (!SessionOperations.isSessionValid(sessionId)) {
			ServerThread.sendServerMessageToSocket("Invalid session id!\n", communicationSocketOutputStream);
			return false;
		}
		
		User userToLogOut = SessionOperations.getUserBySessionId(sessionId);
		
		if (SessionOperations.isSessionExpiredForUser(userToLogOut)) {
			ServerThread.sendServerMessageToSocket("Your session has expired! Please log in again\n", communicationSocketOutputStream);
			SessionOperations.removeSessionForUser(userToLogOut);
			return false;
		}
		
		SessionOperations.removeExpiredSessionWithSessionId(sessionId);
		UserOperations.logOutUser(userToLogOut);
		
		String confirmationMessage = "User %s has logged out and session with id %s has been terminated\n";
		ServerThread.sendServerMessageToSocket(String.format(confirmationMessage, userToLogOut.getUsername(), sessionId), communicationSocketOutputStream);
		return true;
	}

}
