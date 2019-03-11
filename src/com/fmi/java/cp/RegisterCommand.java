package com.fmi.java.cp;

import java.io.OutputStream;

public class RegisterCommand implements Command {

	@Override
	public boolean execute(String[] words, OutputStream outputStream) {
		String userName = words[USERNAME_INDEX];
		String password = words[PASSWORD_INDEX];
		String firstName = words[FIRSTNAME_INDEX];
		String lastName = words[LASTNAME_INDEX];
		String email = words[EMAIL_INDEX];
		if (CommandsExecutor.getUsers().containsKey(userName)) {
			CommandsExecutor.sendToSocket("The username is already taken, please choose a different one\n", outputStream);
			return false;
		}
		User u = new User(userName, Password.hash(password), firstName, lastName, email);
		CommandsExecutor.getUsers().put(userName, u);
		CommandsExecutor.updateFile();
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(userName);
		msg.append(" has been successfully registered\n");
		EmailSender.sendEmail(email);
		CommandsExecutor.sendToSocket(msg.toString(), outputStream);
		
		return true;
		
	}

}
