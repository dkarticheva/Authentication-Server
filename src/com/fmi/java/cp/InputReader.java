package com.fmi.java.cp;

import java.io.File;
import java.io.OutputStream;

public class InputReader {
	
	static CommandsExecutor cmd;
	static OutputStream outputStream;
	//static Validator validator = new Validator();
	
	public InputReader(OutputStream output, String fileName) {
		outputStream = output;
		cmd = new CommandsExecutor(new File(fileName));
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