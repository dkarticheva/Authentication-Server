package com.fmi.java.cp;


public class DeleteUserCommand implements Command {
	
	// delete-user –username <username>

	@Override
	public CommandResult execute(String[] commandOptions) {
		
		String userName = Command.getUsername(commandOptions);
		CommandResult deleteUserResult = new CommandResult();
		
		if (!UserOperations.doesUserExistWithUsername(userName)) {
			deleteUserResult.setResultMessage("Wrong username or password!\n");
			return deleteUserResult;
		}
		
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
