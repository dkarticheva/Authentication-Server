package com.fmi.java.cp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private final static int DEFAULT_COMMUNICATION_PORT = 4444;
		
	public static void main(String[] args) {
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(DEFAULT_COMMUNICATION_PORT);
			System.out.println("Server is running");
			
			while (true) {
				Socket socket = serverSocket.accept();
				ServerThread thread = new ServerThread(socket);
				thread.start();
			}
		} 
		
		catch (IOException e) {
			System.out.println("There has been an issue while opening the server socket");
		}
		
		finally {
			if (serverSocket != null ) {
				closeServerSocket(serverSocket);
			}
		}
	}
	
	private static void closeServerSocket(ServerSocket socketToClose) {
		
		try {
			socketToClose.close();
			System.out.println("Closing the server socket");
			
		} catch (IOException e) {
			System.out.println("There has been an issue while closing the server socket");
		}
	}
}
