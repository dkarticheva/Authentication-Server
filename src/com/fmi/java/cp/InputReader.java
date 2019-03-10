package com.fmi.java.cp;

import java.io.File;
import java.io.OutputStream;

public class InputReader {
	
	static CommandsExecutor cmd = new CommandsExecutor(new File("src/usersInfo.txt"));
	static Validator validator = new Validator();
	private static OutputStream outputStream;
	
	public InputReader(OutputStream output) {
		outputStream = output;
	}
	
	public boolean readInput(String line) {
		String[] lineWords = line.split(" ");
		switch (lineWords[0]) {
			case "register" : return cmd.register(lineWords, outputStream);
			
			case "reset-password" : return cmd.resetPassword(lineWords, outputStream);
			
			case "update-user" : return cmd.updateUser(lineWords, outputStream); 
			
			case "logout" : return cmd.logOut(lineWords, outputStream); 
			
			case "delete-user" : return cmd.deleteUser(lineWords, outputStream); 
		}
		if (lineWords[0].equals("login")) {
			if (lineWords[1].equals("-–username")) {
				return cmd.logIn(lineWords, outputStream);
			}
			else {
				return cmd.logInSesId(lineWords, outputStream);
			}
		}
		return false;
	}
}