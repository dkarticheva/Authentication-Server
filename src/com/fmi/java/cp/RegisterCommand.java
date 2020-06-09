package com.fmi.java.cp;

public class RegisterCommand implements Command {
	
		// register --username <username> --password <password> --first-name <firstName> --last-name <lastName> --email <email> 
	
		final static int FIRSTNAME_STARTING_INDEX = 6;
		final static int LASTNAME_STARTING_INDEX = 8;
		final static int EMAIL_STARTING_INDEX = 10;
	
		private static String getFirstName(String[] commandOptions) {
			return commandOptions[FIRSTNAME_STARTING_INDEX];
		}
		
		private static String getLastName(String[] commandOptions) {
			return commandOptions[LASTNAME_STARTING_INDEX];
		}
		
		private static String getEmail(String[] commandOptions) {
			return commandOptions[EMAIL_STARTING_INDEX];
		}

	@Override
	public CommandResult execute(String[] commandOptions) {
		
		String userName = Command.getUsername(commandOptions);
		String password = Command.getPassword(commandOptions);
		String firstName = getFirstName(commandOptions);
		String lastName = getLastName(commandOptions);
		String email = getEmail(commandOptions);
		
		CommandResult registerResult = new CommandResult();
		
		if (UserOperations.doesUserExistWithUsername(userName)) {
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
