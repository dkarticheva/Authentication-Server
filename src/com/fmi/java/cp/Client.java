package com.fmi.java.cp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	private Socket communicationSocket;
	private BufferedReader serverSentDataReader;
	
	// TODO: fix this!
	private Validator validator;
	
	final static int DEFAULT_COMMUNICATION_PORT = 4444;
	
	private boolean validateOptionsAccordingCommandName(String[] commandLine) {
		
		// TODO: omg what is that - fix it!
		Validator validator = new Validator();
		
		String commandName = commandLine[0];
		switch (commandName) {
			case "register" : return validator.validateRegisterCommand(commandLine);
		
			case "reset-password" : return validator.validateResetPassword(commandLine);
			
			case "update-user" : return validator.validateUpdateUser(commandLine);
			
			case "logout" : return validator.validateLogOut(commandLine);
			
			case "delete-user" : return validator.validateDeleteUser(commandLine);
			
		}
		if (commandName.equals("login")) {
			String firstOption = commandLine[1];
			if (firstOption.equals("-–username")) {
				return validator.validateLogIn(commandLine);
			}
			else {
				return validator.validateLogInSesh(commandLine);
			}
		}
		return false;
	}
	
	public Client(InetAddress socketAddress, int socketPort) {
		
		try {
			communicationSocket = new Socket(socketAddress, socketPort);
			serverSentDataReader = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
			System.out.println("Successfully connected to server");
			
			validator = new Validator();
		} catch (IOException e) {
			System.out.println("Issue while opening socket on address " + socketAddress + " and on port" + socketPort);
		}
	}
	
	private void sendCommandToServer(String command) {
		
		try {
			PrintWriter clientDataWriter = new PrintWriter(communicationSocket.getOutputStream(), true);
			
			int commandSize = command.length();
			
			clientDataWriter.println(commandSize);
			clientDataWriter.println(command);
			clientDataWriter.flush();
			
		} catch (IOException e) {
			System.out.println("Issue while sending the input data to the server!");
		}
		
	}
	public void readUserCommandFromConsole() {
		
		Scanner consoleReader = new Scanner(System.in);
		String commandLine;
		
		while ((commandLine = consoleReader.nextLine()) != null) {
			
			String[] commandNameAndOptions = commandLine.split(" ");
			String commandName = commandNameAndOptions[0];
			
			if (!validator.isValidCommand(commandName)) {
				System.out.println("Invalid command!");
			}
			else if (!validateOptionsAccordingCommandName(commandNameAndOptions)) {
				System.out.println("Incorrect command options!");
			}
			else {
				sendCommandToServer(commandLine);
				readServerReply();
			}
		}
		consoleReader.close();
	}
	public void readServerReply() {
		
		try {
			String serverReply;
			int serverReplySize = Integer.parseInt(serverSentDataReader.readLine());
			
			while (serverReplySize != 0 && (serverReply = serverSentDataReader.readLine()) != null) {
				System.out.println(serverReply);
				serverReplySize -= serverReply.length() + 1;
			}
		} catch (IOException e) {
			System.out.println("Issue while receiving the output data fro the server!");
		}
	}
	
	public static void main(String[] args) {
		
		try {
			
			InetAddress  socketAddress = InetAddress.getByName("localhost");
			Client client = new Client(socketAddress, DEFAULT_COMMUNICATION_PORT);
			client.readUserCommandFromConsole();
			
		} catch (UnknownHostException e) {
			System.out.println("The entered host is unknown");
		}
	}

}
