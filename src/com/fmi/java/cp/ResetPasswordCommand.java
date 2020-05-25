package com.fmi.java.cp;

public class ResetPasswordCommand implements Command{

	@Override
	public CommandResult execute(String[] commandOptions) {
		
		// Fix those in every command class!
		String userName = commandOptions[2];
		String oldPassword = commandOptions[OLDPASSWORD_INDEX];
		String newPassword = commandOptions[NEWPASSWORD_INDEX];
		
		CommandResult resetPasswordResult = new CommandResult();
		
		if (!UserOperations.confirmExistenceOfUserWithUsername(userName)) {
			resetPasswordResult.setResultMessage("Wrong username or password!\n");
			return resetPasswordResult;
		}
		
		User userToResetPassword = UserOperations.getUserByUsername(userName);
		if (!UserOperations.validatePasswordForUser(userToResetPassword, oldPassword)) {
			resetPasswordResult.setResultMessage("Wrong username or password!\n");
			return resetPasswordResult; 
		}
		
		if (SessionOperations.isSessionExpiredForUser(userToResetPassword)) {
			SessionOperations.removeSessionForUser(userToResetPassword);
			resetPasswordResult.setResultMessage("Your session has expired! Please log in again\n");
			return resetPasswordResult;
		}
		
		userToResetPassword.setPassword(newPassword);
		UserOperations.addUser(userToResetPassword);
		UserOperations.updateUsersDetails();
		
		String confirmationMessage = "Password for user %s has been successfully modified\n";
		resetPasswordResult.setResultMessage(String.format(confirmationMessage, userName));
		return resetPasswordResult;
	}

}
