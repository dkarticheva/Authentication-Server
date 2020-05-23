package com.fmi.java.cp;

import java.io.OutputStream;

public class LogInWithSessionCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String sessionId = commandOptions[2];
		if (!validator.validateSession(sessionId)) {
			CommandsExecutor.sendServerMessageToSocket("Invalid session id!\n", communicationSocketOutputStream);
			return false;
		}

		Session session = validator.getSessionFromId(sessionId);
		User userToLogIn = CommandsExecutor.getSessions().get(session);
		// TODO : remove the chain!
		CommandsExecutor.getLoggedIn().put(userToLogIn.getUsername(), userToLogIn);
		
		String confirmationMessage = "User %s has loged in successfully\n";
		CommandsExecutor.sendServerMessageToSocket(String.format(confirmationMessage, userToLogIn.getUsername()), communicationSocketOutputStream);
		return true;
	}

}
