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
	private PrintWriter socketMessageWriter;

	public ServerThread(Socket socket) {

		try {
			this.inputStream = socket.getInputStream();
			this.outputStream = socket.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));
			socketMessageWriter = new PrintWriter(outputStream, true);

		} catch (IOException e) {
			System.out.println("There has been an issue while accessing the socket streams!");
		}
	}

	public void run() {
		while (true) {
			String input = receiveClientRequest();
			if (input == null) {
				break;
			}

			CommandResult result = executeParsedClientCommand(input);
			sendServerMessageToSocket(result.getResultMessage());
		}
	}

	private String receiveClientRequest() {

		try {
			return reader.readLine();

		} catch (SocketException e) {
			System.out.println("The client closed their socket!");

		} catch (IOException e) {
			System.out.println("There has been an issue while receiving data from client");
		}

		return null;
	}

	private CommandResult executeParsedClientCommand(String command) {
		String[] commandOptions = command.split(" ");
		String commandName = commandOptions[0];
		String firstOption = commandOptions[1];

		switch (commandName) {
		case "register":
			return CommandsExecutor.register(commandOptions);

		case "reset-password":
			return CommandsExecutor.resetPassword(commandOptions);

		case "update-user":
			return CommandsExecutor.updateUser(commandOptions);

		case "logout":
			return CommandsExecutor.logOut(commandOptions);

		case "delete-user":
			return CommandsExecutor.deleteUser(commandOptions);

		case "login":
			return firstOption.equals("-–username") ? CommandsExecutor.logIn(commandOptions)
					: CommandsExecutor.logInSesId(commandOptions);
		}

		return new CommandResult("Invalid command name");
	}

	private void sendServerMessageToSocket(String message) {
		int messageSize = message.length();

		socketMessageWriter.println(messageSize);
		socketMessageWriter.print(message);
		socketMessageWriter.flush();
	}
}
