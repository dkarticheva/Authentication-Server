package com.fmi.java.cp;

public class LogInWithSessionCommand implements Command{

	@Override
	public CommandResult execute(String[] commandOptions) {
		
		String sessionId = commandOptions[2];
		
		CommandResult logInWithSessionResult = new CommandResult();
		
		if (!SessionOperations.isSessionValid(sessionId)) {
			logInWithSessionResult.setResultMessage("Invalid session id!\n");
			return logInWithSessionResult;
		}

		User userToLogIn = SessionOperations.getUserBySessionId(sessionId);
		UserOperations.logInUser(userToLogIn);
		
		String confirmationMessage = "User %s has loged in successfully\n";
		logInWithSessionResult.setResultMessage(String.format(confirmationMessage, userToLogIn.getUsername()));
		return logInWithSessionResult;
	}

}
