package com.fmi.java.cp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread {
	
	private InputStream inputStream;
	private OutputStream outputStream;
	private BufferedReader reader;
	
	public ServerThread(Socket socket) {
		try {
			this.inputStream = socket.getInputStream();
			this.outputStream = socket.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));
		} catch (IOException e) {
			System.out.println("Issue while accessing the socket streams!");
		}
	}
	
	private String receiveClientRequest() {
		String input = null;
		try {
			input = reader.readLine();
		} catch (SocketException e) {
			System.out.println("Client closed his socket!");
			return null;
		} catch (IOException e1) {
			System.out.println("Issue while receiving data from client");
		}
		return input;
	}
	
	private void sendServerMessageToSocket(String message) {
		
		PrintWriter socketMessageWriter = new PrintWriter(outputStream, true);
		int messageSize = message.length();
		
		socketMessageWriter.println(messageSize);
		socketMessageWriter.print(message);
		socketMessageWriter.flush();
	}
	
	private CommandResult executeParsedClientCommand(String command) {
		String[] commandOptions = command.split(" ");
		String commandName = commandOptions[0];
		String firstOption = commandOptions[1];
		
		switch (commandName) {
			case "register" : return CommandsExecutor.register(commandOptions);
			
			case "reset-password" : return CommandsExecutor.resetPassword(commandOptions);
			
			case "update-user" : return CommandsExecutor.updateUser(commandOptions); 
			
			case "logout" : return CommandsExecutor.logOut(commandOptions); 
			
			case "delete-user" : return CommandsExecutor.deleteUser(commandOptions); 
		}
		if (commandName.equals("login")) {
			if (firstOption.equals("-–username")) {
				return CommandsExecutor.logIn(commandOptions);
			}
			else {
				return CommandsExecutor.logInSesId(commandOptions);
			}
		}
		return new CommandResult("Invalid command name");
	}

	public void run() {
		while (true) {
			String input = receiveClientRequest();
			if (input == null) {
				break;
			}
			
			// TODO : how to use the file?
			String dataBaseFileName = "src/usersInfo.txt";
			
			CommandResult result = executeParsedClientCommand(input);
			sendServerMessageToSocket(result.getResultMessage());
		}
	}
}
