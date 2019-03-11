package com.fmi.java.cp;

import java.io.OutputStream;

public class ResetPasswordCommand implements Command{

	@Override
	public boolean execute(String[] words, OutputStream outputStream) {
		String userName = words[2];
		String oldPassword = words[OLDPASSWORD_INDEX];
		String newPassword = words[NEWPASSWORD_INDEX];
		if (!validator.validateUser(userName)) {
			CommandsExecutor.sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		User u = CommandsExecutor.getUsers().get(userName);
		if (!validator.validatePassword(u, oldPassword)) {
			CommandsExecutor.sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		if (validator.isSessionExpired(u)) {
			CommandsExecutor.sendToSocket("Your session has expired! Please log in again\n", outputStream);
			CommandsExecutor.removeFromSessions(userName, null);
			return false;
		}
		u.setPassword(Password.hash(newPassword));
		CommandsExecutor.getUsers().put(userName, u);
		CommandsExecutor.updateFile();
		
		StringBuilder msg = new StringBuilder("Password for user ");
		msg.append(userName);
		msg.append(" has been successfully modified\n");
		CommandsExecutor.sendToSocket(msg.toString(), outputStream);
		return true;
	}

}
