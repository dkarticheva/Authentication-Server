package com.fmi.java.cp;

import java.io.OutputStream;

public class DeleteUserCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String userName = commandOptions[2];
		if (!UserOperations.confirmExistenceOfUserWithUsername(userName)) {
			ServerThread.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		User userToDelete = UserOperations.getUserByUsername(userName);
		if (SessionOperations.isSessionExpiredForUser(userToDelete)) {
			ServerThread.sendServerMessageToSocket("Your session has expired! Please log in again\n", communicationSocketOutputStream);
			SessionOperations.removeSessionForUser(userToDelete);
			return false;
		}
		
		SessionOperations.removeSessionForUser(userToDelete);
		UserOperations.removeUser(userToDelete);
		UserOperations.updateUsersDetails();
		
		String confirmationMessage = "User %s has been deleted\n";
		ServerThread.sendServerMessageToSocket(String.format(confirmationMessage, userName), communicationSocketOutputStream);
		return true;
	}

}
