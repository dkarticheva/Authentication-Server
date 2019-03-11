package com.fmi.java.cp;

import java.io.OutputStream;

public class LogInCommand implements Command{

	@Override
	public boolean execute(String[] words, OutputStream outputStream) {
		String userName = words[USERNAME_INDEX];
		String password = words[PASSWORD_INDEX];
		if (!validator.validateUser(userName)) {
			CommandsExecutor.sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		User u = CommandsExecutor.getUsers().get(userName);
		if (!validator.validatePassword(u, password)) {
			CommandsExecutor.sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		CommandsExecutor.getLoggedIn().put(userName, u);
		
		// in case of a second log in in a row
		if (validator.alreadyLoggedIn(userName)) {
			CommandsExecutor.removeFromSessions(userName, null);
		}
		
		Session s = new Session();
		CommandsExecutor.getSessions().put(s, u);
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(userName);
		msg.append(" has been successfully logged\n");
		msg.append("Session with id ");
		msg.append(s.getID());
		msg.append(" and ttl ");
		msg.append(s.getTimeToLive());
		msg.append(" has been created\n");
		CommandsExecutor.sendToSocket(msg.toString(), outputStream);
		return true;
	}

}
