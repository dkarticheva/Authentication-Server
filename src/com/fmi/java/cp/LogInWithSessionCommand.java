package com.fmi.java.cp;

import java.io.OutputStream;

public class LogInWithSessionCommand implements Command{

	@Override
	public boolean execute(String[] words, OutputStream outputStream) {
		String curId = words[2];
		if (!validator.validateSession(curId)) {
			CommandsExecutor.sendToSocket("Invalid session id!\n", outputStream);
			return false;
		}

		Session s = validator.findSessionFromID(curId);
		User u = CommandsExecutor.getSessions().get(s);
		CommandsExecutor.getLoggedIn().put(u.getUsername(), u);
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(u.getUsername());
		msg.append(" has loged in successfully\n");
		CommandsExecutor.sendToSocket(msg.toString(), outputStream);
		return true;
	}

}
