package com.fmi.java.cp;

public class UpdateUserCommand implements Command {
	
	// update-user  -–session-id <session-id>  -–new-username <newUsername> --new-first-name <newFirstName> --new-last-name <newLastName> --new-email <email>. Everything except --session-id is optional.

	@Override
	public CommandResult execute(String[] commandOptions) {
		
		String sessionId = Command.getSessionID(commandOptions);
		
		CommandResult updateUserResult = new CommandResult();
		
		if (SessionOperations.isSessionInvalid(sessionId)) {
			updateUserResult.setResultMessage("Invalid session id!\n");
			return updateUserResult;
		}
		
		User userToUpdate = SessionOperations.getUserBySessionId(sessionId);
		if (SessionOperations.isSessionExpiredForUser(userToUpdate)) {
			SessionOperations.removeExpiredSessionWithSessionId(sessionId);
			updateUserResult.setResultMessage("Your session has expired! Please log in again\n");
			return updateUserResult;
		}
		
		SessionOperations.removeSessionForUser(userToUpdate);
		UserOperations.removeUser(userToUpdate);
		
		updateUserDetailsForUserAccordingOptions(userToUpdate, commandOptions);
		
		UserOperations.addUser(userToUpdate);
		UserOperations.logInUser(userToUpdate);
		
		Session newSession = new Session();
		SessionOperations.addSessionForUser(newSession, userToUpdate);
		UserOperations.updateUsersDetails();
		
		String confirmationMessage = "User %s has successfully updated their information\n Session with id %s and ttl %s has been created\n";
		updateUserResult.setResultMessage(String.format(confirmationMessage, userToUpdate.getUsername(), newSession.getID(), newSession.getTimeToLive()));
		return updateUserResult;
	}
	
	private static void updateUserDetailsForUserAccordingOptions(User userToUpdate, String[] commandOptions) {
		
		for (int i=0; i<commandOptions.length; i+=2) {
			
			String optionName = commandOptions[i];
			String optionValue = commandOptions[i+1];
			switch(optionName) {
			
			case "-–new-username" : userToUpdate.setUsername(optionValue); break;
			case "--new-first-name": userToUpdate.setFirstName(optionValue); break;
			case "--new-last-name": userToUpdate.setLastName(optionValue); break;
			case "--new-email": userToUpdate.setEmail(optionValue); break;
			  				
			}
		}
	}

}
