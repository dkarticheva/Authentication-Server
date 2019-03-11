package com.fmi.java.cp;

import java.io.OutputStream;

public class DeleteUserCommand implements Command{

	@Override
	public boolean execute(String[] words, OutputStream outputStream) {
		String userName = words[2];
		if (!validator.validateUser(userName)) {
			CommandsExecutor.sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		User u = CommandsExecutor.getUsers().get(userName);
		if (validator.isSessionExpired(u)) {
			CommandsExecutor.sendToSocket("Your session has expired! Please log in again\n", outputStream);
			CommandsExecutor.removeFromSessions(userName, null);
			return false;
		}
		CommandsExecutor.removeFromSessions(userName, null);
		CommandsExecutor.getUsers().remove(userName);
		CommandsExecutor.getLoggedIn().remove(userName);
		CommandsExecutor.updateFile();
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(userName);
		msg.append(" has been deleted\n");
		CommandsExecutor.sendToSocket(msg.toString(), outputStream);
		return true;
	}

}
