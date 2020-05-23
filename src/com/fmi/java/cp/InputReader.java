package com.fmi.java.cp;

import java.io.File;
import java.io.OutputStream;

public class InputReader {
	
	// TODO : manage better cmd!
	static CommandsExecutor commandExecutor;
	static OutputStream clientSocketOutputStream;
	
	public InputReader(OutputStream socketOutputStream, String usersDetailsFileName) {
		clientSocketOutputStream = socketOutputStream;
		commandExecutor = new CommandsExecutor(new File(usersDetailsFileName));
	}
	
	public boolean readClientSentCommand(String command) {
		
		String[] commandOptions = command.split(" ");
		String commandName = commandOptions[0];
		String firstOption = commandOptions[1];
		
		switch (commandName) {
			case "register" : return commandExecutor.register(commandOptions, clientSocketOutputStream);
			
			case "reset-password" : return commandExecutor.resetPassword(commandOptions, clientSocketOutputStream);
			
			case "update-user" : return commandExecutor.updateUser(commandOptions, clientSocketOutputStream); 
			
			case "logout" : return commandExecutor.logOut(commandOptions, clientSocketOutputStream); 
			
			case "delete-user" : return commandExecutor.deleteUser(commandOptions, clientSocketOutputStream); 
		}
		if (commandName.equals("login")) {
			if (firstOption.equals("-–username")) {
				return commandExecutor.logIn(commandOptions, clientSocketOutputStream);
			}
			else {
				return commandExecutor.logInSesId(commandOptions, clientSocketOutputStream);
			}
		}
		return false;
	}
}