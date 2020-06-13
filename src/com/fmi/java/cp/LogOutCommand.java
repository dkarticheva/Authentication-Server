package com.fmi.java.cp;

public class LogOutCommand implements Command {
	
	// logout –session-id <sessionId>

	@Override
	public CommandResult execute(String[] commandOptions) {
		
		String sessionId = Command.getSessionID(commandOptions);
		
		CommandResult logOutResult = new CommandResult();
		
		if (SessionOperations.isSessionInvalid(sessionId)) {
			logOutResult.setResultMessage("Invalid session id!\n");
			return logOutResult;
		}
		
		User userToLogOut = SessionOperations.getUserBySessionId(sessionId);
		
		if (SessionOperations.isSessionExpiredForUser(userToLogOut)) {
			SessionOperations.removeSessionForUser(userToLogOut);
			logOutResult.setResultMessage("Your session has expired! Please log in again\n");
			return logOutResult;
		}
		
		SessionOperations.removeExpiredSessionWithSessionId(sessionId);
		UserOperations.logOutUser(userToLogOut);
		
		String confirmationMessage = "User %s has logged out and session with id %s has been terminated\n";
		logOutResult.setResultMessage(String.format(confirmationMessage, userToLogOut.getUsername(), sessionId));
		return logOutResult;
	}

}
