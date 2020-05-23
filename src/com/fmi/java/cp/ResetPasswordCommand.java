package com.fmi.java.cp;

import java.io.OutputStream;

public class ResetPasswordCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		// Fix those in every command class!
		String userName = commandOptions[2];
		String oldPassword = commandOptions[OLDPASSWORD_INDEX];
		String newPassword = commandOptions[NEWPASSWORD_INDEX];
		
		if (!validator.confirmExistenceOfUserWithUsername(userName)) {
			CommandsExecutor.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		// TODO: no chains pls
		User userToResetPassword = CommandsExecutor.getUsers().get(userName);
		if (!validator.validatePasswordForUser(userToResetPassword, oldPassword)) {
			CommandsExecutor.sendServerMessageToSocket("Wrong username or password!\n", communicationSocketOutputStream);
			return false;
		}
		
		if (validator.isSessionExpiredForUser(userToResetPassword)) {
			CommandsExecutor.sendServerMessageToSocket("Your session has expired! Please log in again\n", communicationSocketOutputStream);
			CommandsExecutor.removeExpiredSessionForUser(userName, null);
			return false;
		}
		
		userToResetPassword.setPassword(Password.hash(newPassword));
		// TODO: no chains pls
		CommandsExecutor.getUsers().put(userName, userToResetPassword);
		CommandsExecutor.updateUsersDetails();
		
		String confirmationMessage = "Password for user %s has been successfully modified\n";
		CommandsExecutor.sendServerMessageToSocket(String.format(confirmationMessage, userName), communicationSocketOutputStream);
		return true;
	}

}
