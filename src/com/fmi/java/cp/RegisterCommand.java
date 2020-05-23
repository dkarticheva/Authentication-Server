package com.fmi.java.cp;

import java.io.OutputStream;

public class RegisterCommand implements Command {

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String userName = commandOptions[USERNAME_INDEX];
		String password = commandOptions[PASSWORD_INDEX];
		String firstName = commandOptions[FIRSTNAME_INDEX];
		String lastName = commandOptions[LASTNAME_INDEX];
		String email = commandOptions[EMAIL_INDEX];
		
		if (CommandsExecutor.getUsers().containsKey(userName)) {
			CommandsExecutor.sendServerMessageToSocket("The username is already taken, please choose a different one\n", communicationSocketOutputStream);
			return false;
		}
		
		User userToRegister = new User(userName, Password.hash(password), firstName, lastName, email);
		// TODO: remove the chain
		CommandsExecutor.getUsers().put(userName, userToRegister);
		CommandsExecutor.updateUsersDetails();
		
		EmailSender.sendEmail(email);
		
		String confirmationMessage = "User %s has been successfully registered\n";
		CommandsExecutor.sendServerMessageToSocket(String.format(confirmationMessage, userName), communicationSocketOutputStream);
		
		return true;
		
	}

}
