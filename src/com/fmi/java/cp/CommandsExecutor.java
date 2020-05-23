package com.fmi.java.cp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CommandsExecutor {
	
	private static Map<String, User> allUsers;
	private static Map<String, User> loggedInUsers;
	private static Map<Session, User> activeSessionsOfUsers;
	
	private static File usersDetails;
	
	public CommandsExecutor(File usersInfoFile) {
		allUsers = new ConcurrentHashMap<String, User>();
		loggedInUsers = new ConcurrentHashMap<String, User>();
		activeSessionsOfUsers = new ConcurrentHashMap<Session, User>();
		usersDetails = usersInfoFile;
	}
	
	public static Map<String, User> getUsers() {
		return allUsers;
	}
	
	public static Map<Session, User> getSessions() {
		return activeSessionsOfUsers;
	}
	
	public static Set<Session> getSessionsKeySet() {
		return activeSessionsOfUsers.keySet();
	}
	
	public static Map<String, User> getLoggedIn() {
		return loggedInUsers;
	}
	
	public static void removeExpiredSessionForUser(String userName, String sessionId) {
		
		Set<Session> activeSessions = activeSessionsOfUsers.keySet();
		
		if (sessionId != null) {
			for (Session session : activeSessions) {
				session.getID().equals(sessionId);
				activeSessionsOfUsers.remove(session);
			}
			return;
		}
		
		// TODO: what is the meaning of this? fix it
		User user = loggedInUsers.get(userName);
		for (Session session : activeSessions) {
			if (activeSessionsOfUsers.get(session).equals(user)) {
				activeSessionsOfUsers.remove(session);
			}
		}
	}
	public static void updateUsersDetails() {
		
		Collection<User> users = allUsers.values();
		
		// TODO: use try with resources
		try {
			BufferedWriter userDetailsWriter = new BufferedWriter(new FileWriter(usersDetails));
			for (User user : users) {
				userDetailsWriter.write(user.getUsername());
				userDetailsWriter.write(" ");
				userDetailsWriter.write(user.getPassword());
				userDetailsWriter.write(" ");
				userDetailsWriter.write(user.getFirstName());
				userDetailsWriter.write(" ");
				userDetailsWriter.write(user.getLastName());
				userDetailsWriter.write(" ");
				userDetailsWriter.write(user.getEmail());
				userDetailsWriter.newLine();
			}
			userDetailsWriter.close();
			
		} catch (IOException e) {
			System.out.println("Issue while opening the database");
		}
	}
	
	// TODO : don't pass the whole line - just name and value of the current option
	static void setNewUsersDetailsAccordingOption(String[] commandOptions, int optionNameIndex, User newUser) {
		
		String optionName = commandOptions[optionNameIndex];
		String optionValue = commandOptions[optionNameIndex + 1];
		
		switch (optionName) {
			case "-–new-username"   : newUser.setUsername(optionValue); 
									  break;
			case "--new-first-name" : newUser.setFirstName(optionValue); 
									  break;
			case "--new-last-name"  : newUser.setLastName(optionValue); 
									  break;
			default                 : newUser.setEmail(optionValue);
		}
	}
	
	public static void sendServerMessageToSocket(String message, OutputStream communicationSocketOutputStream) {
		
		PrintWriter socketMessageWriter = new PrintWriter(communicationSocketOutputStream, true);
		int messageSize = message.length();
		
		socketMessageWriter.println(messageSize);
		socketMessageWriter.print(message);
		socketMessageWriter.flush();
	}
	
	public boolean register(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new RegisterCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean logIn(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new LogInCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean resetPassword(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new ResetPasswordCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean deleteUser(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new DeleteUserCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean logInSesId(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new LogInWithSessionCommand().execute(commandOptions, communicationSocketOutputStream);
	}
	
	public boolean logOut(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new LogOutCommand().execute(commandOptions, communicationSocketOutputStream);
	} 
	
	public boolean updateUser(String[] commandOptions, OutputStream communicationSocketOutputStream) {
		return new UpdateUserCommand().execute(commandOptions, communicationSocketOutputStream);
	}
}
