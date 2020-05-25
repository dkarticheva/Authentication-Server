package com.fmi.java.cp;

public class RegisterCommand implements Command {

	@Override
	public CommandResult execute(String[] commandOptions) {
		
		String userName = commandOptions[USERNAME_INDEX];
		String password = commandOptions[PASSWORD_INDEX];
		String firstName = commandOptions[FIRSTNAME_INDEX];
		String lastName = commandOptions[LASTNAME_INDEX];
		String email = commandOptions[EMAIL_INDEX];
		
		CommandResult registerResult = new CommandResult();
		
		if (UserOperations.isUsernameAlreadyTaken(userName)) {
			registerResult.setResultMessage("The username is already taken, please choose a different one\n");
			return registerResult;
		}
		
		User userToRegister = new User(userName, password, firstName, lastName, email);
		UserOperations.addUser(userToRegister);
		UserOperations.updateUsersDetails();
		
		EmailSender.sendEmail(email);
		
		String confirmationMessage = "User %s has been successfully registered\n";
		registerResult.setResultMessage(String.format(confirmationMessage, userName));
		return registerResult;
	}

}
