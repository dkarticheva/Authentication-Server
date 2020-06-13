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
	
	private final static int DEFAULT_COMMUNICATION_PORT = 4444;
	
	private Socket communicationSocket;
	private BufferedReader serverSentDataReader;
	private PrintWriter clientDataWriter;
	
	public Client(InetAddress socketAddress, int socketPort) {
		
		try {
			communicationSocket = new Socket(socketAddress, socketPort);
			serverSentDataReader = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
			clientDataWriter = new PrintWriter(communicationSocket.getOutputStream(), true);
			System.out.println("Successfully connected to server");
			
		} catch (IOException e) {
			System.out.println("There has been an issue while opening socket on address " + socketAddress + " and on port" + socketPort);
		}
	}
	
	
	private void readUserCommandFromConsole() {
		
		Scanner consoleReader = new Scanner(System.in);
		String commandLine;
		
		while ((commandLine = consoleReader.nextLine()) != null) {
			
			if (Validator.isValidCommand(commandLine)) {
				sendCommandToServer(commandLine);
				readServerReply();
			}
		}
		consoleReader.close();
	}
	
	private void sendCommandToServer(String command) {
		
		int commandSize = command.length();
			
		clientDataWriter.println(commandSize);
		clientDataWriter.println(command);
		clientDataWriter.flush();
	}
	
	private void readServerReply() {
		
		try {
			int serverReplySize = Integer.parseInt(serverSentDataReader.readLine());
			
			while (serverReplySize > 0) {
				
				String serverReply = serverSentDataReader.readLine();
				System.out.println(serverReply);
				serverReplySize -= serverReply.length() + 1;
			}
		} catch (IOException e) {
			System.out.println("There has been an issue while receiving the output data from the server!");
		}
	}
	
	public static void main(String[] args) {
		
		createClient();
	}
	
	private static void createClient() {
		
		try {
			InetAddress  socketAddress = InetAddress.getLocalHost();
			Client client = new Client(socketAddress, DEFAULT_COMMUNICATION_PORT);
			client.readUserCommandFromConsole();
			
			
		} catch (UnknownHostException e) {
			System.out.println("The local host name can not be resolved into an address");
		}
	}

}
