package com.fmi.java.cp;

import java.io.OutputStream;

public class DeleteUserCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String userName = commandOptions[2];
		if (!validator.confirmExistenceOfUserWithUsername(userName)) {
			CommandsExecutor.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		User userToDelete = CommandsExecutor.getUsers().get(userName);
		if (validator.isSessionExpiredForUser(userToDelete)) {
			CommandsExecutor.sendServerMessageToSocket("Your session has expired! Please log in again\n", communicationSocketOutputStream);
			CommandsExecutor.removeExpiredSessionForUser(userName, null);
			return false;
		}
		
		// TODO: remove the chains 
		CommandsExecutor.removeExpiredSessionForUser(userName, null);
		CommandsExecutor.getUsers().remove(userName);
		CommandsExecutor.getLoggedIn().remove(userName);
		CommandsExecutor.updateUsersDetails();
		
		String confirmationMessage = "User %s has been deleted\n";
		CommandsExecutor.sendServerMessageToSocket(String.format(confirmationMessage, userName), communicationSocketOutputStream);
		return true;
	}

}
