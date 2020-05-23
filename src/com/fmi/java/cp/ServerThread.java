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
	
	
	public static void sendServerMessageToSocket(String message, OutputStream communicationSocketOutputStream) {
		
		PrintWriter socketMessageWriter = new PrintWriter(communicationSocketOutputStream, true);
		int messageSize = message.length();
		
		socketMessageWriter.println(messageSize);
		socketMessageWriter.print(message);
		socketMessageWriter.flush();
	}

	public void run() {
		while (true) {
			String input = receiveClientRequest();
			if (input == null) {
				break;
			}
			String dataBaseFileName = "src/usersInfo.txt";
			InputReader reader = new InputReader(outputStream, dataBaseFileName);
			reader.readClientSentCommand(input);
		}
	}
}
