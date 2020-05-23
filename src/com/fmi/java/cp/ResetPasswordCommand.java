package com.fmi.java.cp;

import java.io.OutputStream;

public class ResetPasswordCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		// Fix those in every command class!
		String userName = commandOptions[2];
		String oldPassword = commandOptions[OLDPASSWORD_INDEX];
		String newPassword = commandOptions[NEWPASSWORD_INDEX];
		
		if (!UserOperations.confirmExistenceOfUserWithUsername(userName)) {
			ServerThread.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		User userToResetPassword = UserOperations.getUserByUsername(userName);
		if (!UserOperations.validatePasswordForUser(userToResetPassword, oldPassword)) {
			ServerThread.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		if (SessionOperations.isSessionExpiredForUser(userToResetPassword)) {
			ServerThread.sendServerMessageToSocket("Your session has expired! Please log in again\n", communicationSocketOutputStream);
			SessionOperations.removeSessionForUser(userToResetPassword);
			return false;
		}
		
		userToResetPassword.setPassword(newPassword);
		UserOperations.addUser(userToResetPassword);
		UserOperations.updateUsersDetails();
		
		String confirmationMessage = "Password for user %s has been successfully modified\n";
		ServerThread.sendServerMessageToSocket(String.format(confirmationMessage, userName), communicationSocketOutputStream);
		return true;
	}

}
