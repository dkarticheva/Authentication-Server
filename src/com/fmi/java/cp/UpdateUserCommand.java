package com.fmi.java.cp;

import java.io.OutputStream;

public class UpdateUserCommand implements Command {

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String sessionId = commandOptions[2];
		if (!SessionOperations.isSessionValid(sessionId)) {
			ServerThread.sendServerMessageToSocket("Invalid session id!\n", communicationSocketOutputStream);
			return false;
		}
		
		User userToUpdate = SessionOperations.getUserBySessionId(sessionId);
		if (SessionOperations.isSessionExpiredForUser(userToUpdate)) {
			ServerThread.sendServerMessageToSocket("Your session has expired! Please log in again\n", communicationSocketOutputStream);
			SessionOperations.removeExpiredSessionWithSessionId(sessionId);
			return false;
		}
		
		SessionOperations.removeSessionForUser(userToUpdate);
		UserOperations.removeUser(userToUpdate);
		
		// TODO: remove those!
		int sizeLine = commandOptions.length;
		final int oneOptionLenght = 5;
		final int twoOptionLenght = 7;
		final int threeOptionLenght = 9;
		final int fourOptionLenght = 11;
		final int threeWordsLength = 3;
		final int fiveWordsLength = 5;
		final int sevenWordsLength = 7;
		final int nineWordsLength = 9;
		if (sizeLine >= oneOptionLenght) {
			UserOperations.setNewUsersDetailsAccordingOption(commandOptions, threeWordsLength, userToUpdate);
		}
		if (sizeLine >= twoOptionLenght) {
			UserOperations.setNewUsersDetailsAccordingOption(commandOptions, fiveWordsLength, userToUpdate);
		}
		if (sizeLine >= threeOptionLenght) {
			UserOperations.setNewUsersDetailsAccordingOption(commandOptions, sevenWordsLength, userToUpdate);
		}
		if (sizeLine == fourOptionLenght) {
			UserOperations.setNewUsersDetailsAccordingOption(commandOptions, nineWordsLength, userToUpdate);
		}
		UserOperations.addUser(userToUpdate);
		UserOperations.logInUser(userToUpdate);
		
		Session newSession = new Session();
		SessionOperations.addSessionForUser(newSession, userToUpdate);
		UserOperations.updateUsersDetails();
		
		String confirmationMessage = "User %s has successfully updated their information\n Session with id %s and ttl %s has been created\n";
		ServerThread.sendServerMessageToSocket(String.format(confirmationMessage, userToUpdate.getUsername(), newSession.getID(), newSession.getTimeToLive()), communicationSocketOutputStream);
	
		return true;
	}

}
