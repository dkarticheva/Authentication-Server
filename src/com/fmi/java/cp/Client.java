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
	
	private Socket socket;
	private BufferedReader reader;
	private Validator validator;
	
	final static int DEFAULT_PORT = 4444;
	
	public Client(InetAddress adr, int port) {
		try {
			socket = new Socket(adr, port);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Successfully connected to server");
			
			validator = new Validator();
		} catch (IOException e) {
			System.out.println("Issue while opening socket on address " + adr + " and on port" + port);
		}
	}
	private void sendInput(String line) {
		try {
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			int sizeOfMsg = line.length();
			writer.println(sizeOfMsg);
			writer.println(line);
			writer.flush();
		} catch (IOException e) {
			System.out.println("Issue while sending the input data to the server!");
		}
		
	}
	public void retrieveInput() {
		Scanner consoleReader = new Scanner(System.in);
		String inputLine;
		while ((inputLine = consoleReader.nextLine()) != null) {
			String[] inputWords = inputLine.split(" ");
			if (!validator.isValidCommand(inputWords[0])) {
				System.out.println("Invalid command!");
			}
			else if (!validator.validOptions(inputWords)) {
				System.out.println("Incorrect command options!");
			}
			else {
				sendInput(inputLine);
				receiveOutput();
			}
		}
		consoleReader.close();
	}
	public void receiveOutput() {
		try {
			String input;
			int sizeOfMsg = Integer.parseInt(reader.readLine());
			while (sizeOfMsg != 0 && (input = reader.readLine()) != null) {
				System.out.println(input);
				sizeOfMsg -= input.length() + 1;
			}
		} catch (IOException e) {
			System.out.println("Issue while receiving the output data fro the server!");
		}
	}
	
	public static void main(String[] args) {
		try {
			InetAddress adr = InetAddress.getByName("localhost");
			Client c = new Client(adr, DEFAULT_PORT);
			c.retrieveInput();
		} catch (UnknownHostException e) {
			System.out.println("The entered host is unknown");
		}
	}

}
