package com.fmi.java.cp;

import java.io.OutputStream;

public class UpdateUserCommand implements Command {

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String sessionId = commandOptions[2];
		if (!validator.validateSession(sessionId)) {
			CommandsExecutor.sendServerMessageToSocket("Invalid session id!\n", communicationSocketOutputStream);
			return false;
		}
		
		Session currentSession = validator.getSessionFromId(sessionId);
		// TODO: no chains
		User userToUpdate = CommandsExecutor.getSessions().get(currentSession);
		if (validator.isSessionExpiredForUser(userToUpdate)) {
			CommandsExecutor.sendServerMessageToSocket("Your session has expired! Please log in again\n", communicationSocketOutputStream);
			CommandsExecutor.removeExpiredSessionForUser(userToUpdate.getUsername(), sessionId);
			return false;
		}
		
		String userName = userToUpdate.getUsername();
		CommandsExecutor.removeExpiredSessionForUser(userName, null);
		// TODO: chains!
		CommandsExecutor.getUsers().remove(userName);
		CommandsExecutor.getLoggedIn().remove(userName);
		
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
			CommandsExecutor.setNewUsersDetailsAccordingOption(commandOptions, threeWordsLength, userToUpdate);
		}
		if (sizeLine >= twoOptionLenght) {
			CommandsExecutor.setNewUsersDetailsAccordingOption(commandOptions, fiveWordsLength, userToUpdate);
		}
		if (sizeLine >= threeOptionLenght) {
			CommandsExecutor.setNewUsersDetailsAccordingOption(commandOptions, sevenWordsLength, userToUpdate);
		}
		if (sizeLine == fourOptionLenght) {
			CommandsExecutor.setNewUsersDetailsAccordingOption(commandOptions, nineWordsLength, userToUpdate);
		}
		CommandsExecutor.getUsers().put(userToUpdate.getUsername(), userToUpdate);
		CommandsExecutor.getLoggedIn().put(userToUpdate.getUsername(), userToUpdate);
		
		Session newSession = new Session();
		// TODO: chains!
		CommandsExecutor.getSessions().put(newSession, userToUpdate);
		CommandsExecutor.updateUsersDetails();
		
		String confirmationMessage = "User %s has successfully updated their information\n Session with id %s and ttl %s has been created\n";
		CommandsExecutor.sendServerMessageToSocket(String.format(confirmationMessage, userToUpdate.getUsername(), newSession.getID(), newSession.getTimeToLive()), communicationSocketOutputStream);
	
		return true;
	}

}
