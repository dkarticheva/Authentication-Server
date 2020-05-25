package com.fmi.java.cp;

public class DeleteUserCommand implements Command{

	@Override
	public CommandResult execute(String[] commandOptions) {
		
		String userName = commandOptions[2];
		CommandResult deleteUserResult = new CommandResult();
		
		if (!UserOperations.confirmExistenceOfUserWithUsername(userName)) {
			deleteUserResult.setResultMessage("Wrong username or password!\n");
			return deleteUserResult;
		}
		
		// TODO: check existence then get the user!
		User userToDelete = UserOperations.getUserByUsername(userName);
		
		if (SessionOperations.isSessionExpiredForUser(userToDelete)) {
			deleteUserResult.setResultMessage("Your session has expired! Please log in again\n");
			SessionOperations.removeSessionForUser(userToDelete);
			return deleteUserResult;
		}
		
		SessionOperations.removeSessionForUser(userToDelete);
		UserOperations.removeUser(userToDelete);
		UserOperations.updateUsersDetails();
		
		String confirmationMessage = "User %s has been deleted\n";
		deleteUserResult.setResultMessage(String.format(confirmationMessage, userName));
		return deleteUserResult;
	}
	
}
