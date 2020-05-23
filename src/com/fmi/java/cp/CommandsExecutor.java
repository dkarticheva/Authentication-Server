package com.fmi.java.cp;

import java.io.OutputStream;

public class CommandsExecutor {
	
	public boolean register(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new RegisterCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean logIn(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new LogInCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean resetPassword(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new ResetPasswordCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean deleteUser(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new DeleteUserCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean logInSesId(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new LogInWithSessionCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean logOut(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new LogOutCommand().execute(commandOptions, communicationSocketOutputStream);
	} 
	
	public boolean updateUser(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new UpdateUserCommand().execute(commandOptions, communicationSocketOutputStream);
	}
}
