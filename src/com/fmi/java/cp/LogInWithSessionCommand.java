package com.fmi.java.cp;

import java.io.OutputStream;

public class LogInWithSessionCommand implements Command{

	@Override
	public boolean execute(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		
		String sessionId = commandOptions[2];
		if (!SessionOperations.isSessionValid(sessionId)) {
			ServerThread.sendServerMessageToSocket("Invalid session id!\n", communicationSocketOutputStream);
			return false;
		}

		User userToLogIn = SessionOperations.getUserBySessionId(sessionId);
		UserOperations.logInUser(userToLogIn);
		
		String confirmationMessage = "User %s has loged in successfully\n";
		ServerThread.sendServerMessageToSocket(String.format(confirmationMessage, userToLogIn.getUsername()), communicationSocketOutputStream);
		return true;
	}

}
