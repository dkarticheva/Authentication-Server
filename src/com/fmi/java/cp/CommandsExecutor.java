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
	
	final static int USERNAME_INDEX = 2;
	final static int PASSWORD_INDEX = 4;
	final static int NEWPASSWORD_INDEX = 6;
	final static int OLDPASSWORD_INDEX = 4;
	final static int FIRSTNAME_INDEX = 6;
	final static int LASTNAME_INDEX = 8;
	final static int EMAIL_INDEX = 10;
	
	private static Map<String, User> users;
	private static Map<String, User> loggedIn;
	private static Map<Session, User> sessions;
	
	static File usersInfo;
	
	public CommandsExecutor(File usersInfoFile) {
		users = new ConcurrentHashMap<String, User>();
		loggedIn = new ConcurrentHashMap<String, User>();
		sessions = new ConcurrentHashMap<Session, User>();
		usersInfo = usersInfoFile;
	}
	
	// getters
	public static Map<String, User> getUsers() {
		return users;
	}
	public static Map<Session, User> getSessions() {
		return sessions;
	}
	public static Map<String, User> getLoggedIn() {
		return loggedIn;
	}
	static void removeFromSessions(String userName, String sesId) {
		Set<Session> ss = sessions.keySet();
		if (sesId != null) {
			for (Session s : ss) {
				s.getID().equals(sesId);
				sessions.remove(s);
			}
			return;
		}
		User u = loggedIn.get(userName);
		for (Session s : ss) {
			if (sessions.get(s).equals(u)) {
				sessions.remove(s);
			}
		}
	}
	static void updateFile() {
		Collection<User> usrs = users.values();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(usersInfo));
			for (User u : usrs) {
				bw.write(u.getUsername());
				bw.write(" ");
				bw.write(u.getPassword());
				bw.write(" ");
				bw.write(u.getFirstName());
				bw.write(" ");
				bw.write(u.getLastName());
				bw.write(" ");
				bw.write(u.getEmail());
				bw.newLine();
			}
			bw.close();
			
		} catch (IOException e) {
			System.out.println("Issue while opening the database");
		}
	}
	static void findOutOption(String[] words, int index, User u) {
		String curOpt = words[index];
		String nextWord = words[index + 1];
		switch (curOpt) {
			case "-–new-username"   : u.setUsername(nextWord); 
									  break;
			case "--new-first-name" : u.setFirstName(nextWord); 
									  break;
			case "--new-last-name"  : u.setLastName(nextWord); 
									  break;
			default                 : u.setEmail(nextWord);
		}
	}
	public static void sendToSocket(String message, OutputStream outputStream) {
		int sizeOfMesg = message.length();
		PrintWriter writer = new PrintWriter(outputStream, true);
		writer.println(sizeOfMesg);
		writer.print(message);
		writer.flush();
	}
	
	public boolean register(String[] words, OutputStream outputStream) {
		return new RegisterCommand().execute(words, outputStream);
	}
	
	public boolean logIn(String[] words, OutputStream outputStream) {
		return new LogInCommand().execute(words, outputStream);
	}
	
	public boolean resetPassword(String[] words, OutputStream outputStream) {
		return new ResetPasswordCommand().execute(words, outputStream);
	}
	
	public boolean deleteUser(String[] words, OutputStream outputStream) {
		return new DeleteUserCommand().execute(words, outputStream);
	}
	
	public boolean logInSesId(String[] words, OutputStream outputStream) {
		return new LogInWithSessionCommand().execute(words, outputStream);
	}
	
	public boolean logOut(String[] words, OutputStream outputStream) {
		return new LogOutCommand().execute(words, outputStream);
	} 
	
	public boolean updateUser(String[] words, OutputStream outputStream) {
		return new UpdateUserCommand().execute(words, outputStream);
	}
}
