package com.fmi.java.cp;


public class CommandsExecutor {
	
	public static CommandResult register(String[] commandOptions) {
		return new RegisterCommand().execute(commandOptions);
	}
	
	public static CommandResult logIn(String[] commandOptions) {
		return new LogInCommand().execute(commandOptions);
	}
	
	public static CommandResult resetPassword(String[] commandOptions) {
		return new ResetPasswordCommand().execute(commandOptions);
	}
	
	public static CommandResult deleteUser(String[] commandOptions) {
		return new DeleteUserCommand().execute(commandOptions);
	}
	
	public static CommandResult logInSesId(String[] commandOptions) {
		return new LogInWithSessionCommand().execute(commandOptions);
	}
	
	public static CommandResult logOut(String[] commandOptions) {
		return new LogOutCommand().execute(commandOptions);
	} 
	
	public static CommandResult updateUser(String[] commandOptions) {
		return new UpdateUserCommand().execute(commandOptions);
	}
}
