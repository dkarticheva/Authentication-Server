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
	
	final static int DEFAULT_COMMUNICATION_PORT = 4444;
	
	public Client(InetAddress socketAddress, int socketPort) {
		
		try {
			communicationSocket = new Socket(socketAddress, socketPort);
			serverSentDataReader = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
			System.out.println("Successfully connected to server");
			
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
			
			if (Validator.isValidCommand(commandLine)) {
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
