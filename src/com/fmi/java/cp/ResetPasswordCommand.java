package com.fmi.java.cp;

public class ResetPasswordCommand implements Command {
	
	// reset-password –-username <username> --old-password <oldPassword> --new-password <newPassword>
	
	final static int NEWPASSWORD_STARTING_INDEX = 6;
	final static int OLDPASSWORD_STARTING_INDEX = 4;

	@Override
	public CommandResult execute(String[] commandOptions) {
		
		String userName = Command.getUsername(commandOptions);
		String oldPassword = getOldPassword(commandOptions);
		String newPassword = getNewPassword(commandOptions);
		
		CommandResult resetPasswordResult = new CommandResult();
		
		if (UserOperations.isUsernameIncorrect(userName)) {
			resetPasswordResult.setResultMessage("Wrong username or password!\n");
			return resetPasswordResult;
		}
		
		User userToResetPassword = UserOperations.getUserByUsername(userName);
		if (UserOperations.isPasswordForUserInvalid(oldPassword, userToResetPassword)) {
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
	
	private static String getOldPassword(String[] commandOptions) {
		return commandOptions[OLDPASSWORD_STARTING_INDEX];
	}
	
	private static String getNewPassword(String[] commandOptions) {
		return commandOptions[NEWPASSWORD_STARTING_INDEX];
	}

}
