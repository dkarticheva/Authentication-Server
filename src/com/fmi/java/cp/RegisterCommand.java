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
		
		if (UserOperations.isUsernameAlreadyTaken(userName)) {
			ServerThread.sendServerMessageToSocket("The username is already taken, please choose a different one\n", communicationSocketOutputStream);
			return false;
		}
		
		User userToRegister = new User(userName, password, firstName, lastName, email);
		UserOperations.addUser(userToRegister);
		UserOperations.updateUsersDetails();
		
		EmailSender.sendEmail(email);
		
		String confirmationMessage = "User %s has been successfully registered\n";
		ServerThread.sendServerMessageToSocket(String.format(confirmationMessage, userName), communicationSocketOutputStream);
		
		return true;
		
	}

}
