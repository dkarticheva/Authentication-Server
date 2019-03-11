package com.fmi.java.cp;

import java.io.OutputStream;

public class LogOutCommand implements Command{

	@Override
	public boolean execute(String[] words, OutputStream outputStream) {
String curId = words[2];
		
		if (!validator.validateSession(curId)) {
			CommandsExecutor.sendToSocket("Invalid session id!\n", outputStream);
			return false;
		}
		
		Session s = validator.findSessionFromID(curId);
		User u = CommandsExecutor.getSessions().get(s);
		if (validator.isSessionExpired(u)) {
			CommandsExecutor.sendToSocket("Your session has expired! Please log in again\n", outputStream);
			CommandsExecutor.removeFromSessions(u.getUsername(), null);
			return false;
		}
		CommandsExecutor.removeFromSessions(u.getUsername(), curId);
		CommandsExecutor.getLoggedIn().remove(u.getUsername());
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(u.getUsername());
		msg.append(" has logged out and session with id ");
		msg.append(curId);
		msg.append(" has been terminated\n");
		CommandsExecutor.sendToSocket(msg.toString(), outputStream);
		return true;
	}

}
