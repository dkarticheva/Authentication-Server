package com.fmi.java.cp;

public class LogInCommand implements Command {

	// login -–username <username> --password <password>

	@Override
	public CommandResult execute(String[] commandOptions) {

		String userName = Command.getUsername(commandOptions);
		String password = Command.getPassword(commandOptions);

		CommandResult logInResult = new CommandResult();

		if (UserOperations.isUsernameIncorrect(userName)) {
			logInResult.setResultMessage("Wrong username or password!\n");
			return logInResult;
		}

		User userToLogIn = UserOperations.getUserByUsername(userName);
		if (UserOperations.isPasswordForUserInvalid(password, userToLogIn)) {
			logInResult.setResultMessage("Wrong username or password!\n");
			return logInResult;
		}

		UserOperations.logInUser(userToLogIn);

		// in case of a second log in in a row
		if (UserOperations.isUserAlreadyLoggedIn(userName)) {
			SessionOperations.removeSessionForUser(userToLogIn);
		}

		Session newSession = new Session();
		SessionOperations.addSessionForUser(newSession, userToLogIn);

		String confirmationMessage = "User %s has been successfully logged\n Session with id %s and ttl %s has been created\n";
		logInResult.setResultMessage(
				String.format(confirmationMessage, userName, newSession.getID(), newSession.getTimeToLive()));
		return logInResult;
	}

}
