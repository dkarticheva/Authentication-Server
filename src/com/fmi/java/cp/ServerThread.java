package com.fmi.java.cp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
	private String receiveRequest() {
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
	public void run() {
		while (true) {
			String input = receiveRequest();
			if (input == null) {
				break;
			}
			InputReader reader = new InputReader(outputStream);
			reader.readInput(input);
		}
	}
}
