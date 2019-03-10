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
	private Validator validator;
	
	private File usersInfo;
	
	public CommandsExecutor(File usersInfo) {
		users = new ConcurrentHashMap<String, User>();
		loggedIn = new ConcurrentHashMap<String, User>();
		sessions = new ConcurrentHashMap<Session, User>();
		this.usersInfo = usersInfo;
		validator = new Validator();
	}
	
	public static Map<String, User> getUsers() {
		return users;
	}
	public static Map<Session, User> getSessions() {
		return sessions;
	}
	public static Map<String, User> getLoggedIn() {
		return loggedIn;
	}
	private void removeFromSessions(String userName, String sesId) {
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
	private boolean expiredSession(User user) {
		Set<Session> ss = sessions.keySet();
		for (Session s : ss) {
			if (sessions.get(s).equals(user)) {
				return s.hasExpired();
			}
		}
		return true;
	}
	private void updateFile() {
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
	private void findOutOption(String[] words, int index, User u) {
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
	private void sendToSocket(String message, OutputStream outputStream) {
		int sizeOfMesg = message.length();
		PrintWriter writer = new PrintWriter(outputStream, true);
		writer.println(sizeOfMesg);
		writer.print(message);
		writer.flush();
	}
	
	public boolean register(String[] words, OutputStream outputStream) {
		String userName = words[USERNAME_INDEX];
		String password = words[PASSWORD_INDEX];
		String firstName = words[FIRSTNAME_INDEX];
		String lastName = words[LASTNAME_INDEX];
		String email = words[EMAIL_INDEX];
		if (users.containsKey(userName)) {
			sendToSocket("The username is already taken, please choose a different one\n", outputStream);
			return false;
		}
		User u = new User(userName, Password.hash(password), firstName, lastName, email);
		users.put(userName, u);
		updateFile();
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(userName);
		msg.append(" has been successfully registered\n");
		EmailSender.sendEmail(email);
		sendToSocket(msg.toString(), outputStream);
		
		return true;
	}
	public boolean logIn(String[] words, OutputStream outputStream) {
		String userName = words[USERNAME_INDEX];
		String password = words[PASSWORD_INDEX];
		if (!validator.validateUser(userName)) {
			sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		User u = users.get(userName);
		if (!validator.validatePassword(u, password)) {
			sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		loggedIn.put(userName, u);
		
		// in case of a second log in in a row
		if (validator.alreadyLoggedIn(userName)) {
			removeFromSessions(userName, null);
		}
		
		Session s = new Session();
		sessions.put(s, u);
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(userName);
		msg.append(" has been successfully logged\n");
		msg.append("Session with id ");
		msg.append(s.getID());
		msg.append(" and ttl ");
		msg.append(s.getTimeToLive());
		msg.append(" has been created\n");
		sendToSocket(msg.toString(), outputStream);
		return true;
	}
	public boolean resetPassword(String[] words, OutputStream outputStream) {
		String userName = words[2];
		String oldPassword = words[OLDPASSWORD_INDEX];
		String newPassword = words[NEWPASSWORD_INDEX];
		if (!validator.validateUser(userName)) {
			sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		User u = users.get(userName);
		if (!validator.validatePassword(u, oldPassword)) {
			sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		if (expiredSession(u)) {
			sendToSocket("Your session has expired! Please log in again\n", outputStream);
			removeFromSessions(userName, null);
			return false;
		}
		u.setPassword(Password.hash(newPassword));
		users.put(userName, u);
		updateFile();
		
		StringBuilder msg = new StringBuilder("Password for user ");
		msg.append(userName);
		msg.append(" has been successfully modified\n");
		sendToSocket(msg.toString(), outputStream);
		return true;
	}
	public boolean deleteUser(String[] words, OutputStream outputStream) {
		String userName = words[2];
		if (!validator.validateUser(userName)) {
			sendToSocket("Wrong username or password!\n", outputStream);
			return false;
		}
		User u = users.get(userName);
		if (expiredSession(u)) {
			sendToSocket("Your session has expired! Please log in again\n", outputStream);
			removeFromSessions(userName, null);
			return false;
		}
		removeFromSessions(userName, null);
		users.remove(userName);
		loggedIn.remove(userName);
		updateFile();
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(userName);
		msg.append(" has been deleted\n");
		sendToSocket(msg.toString(), outputStream);
		return true;
	}
	public boolean logInSesId(String[] words, OutputStream outputStream) {
		String curId = words[2];
		if (!validator.validateSession(curId)) {
			sendToSocket("Invalid session id!\n", outputStream);
			return false;
		}

		Session s = validator.findSessionFromID(curId);
		User u = sessions.get(s);
		loggedIn.put(u.getUsername(), u);
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(u.getUsername());
		msg.append(" has loged in successfully\n");
		sendToSocket(msg.toString(), outputStream);
		return true;
	}
	public boolean logOut(String[] words, OutputStream outputStream) {
		String curId = words[2];
		
		if (!validator.validateSession(curId)) {
			sendToSocket("Invalid session id!\n", outputStream);
			return false;
		}
		
		Session s = validator.findSessionFromID(curId);
		User u = sessions.get(s);
		if (expiredSession(u)) {
			sendToSocket("Your session has expired! Please log in again\n", outputStream);
			removeFromSessions(u.getUsername(), null);
			return false;
		}
		removeFromSessions(u.getUsername(), curId);
		loggedIn.remove(u.getUsername());
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(u.getUsername());
		msg.append(" has logged out and session with id ");
		msg.append(curId);
		msg.append(" has been terminated\n");
		sendToSocket(msg.toString(), outputStream);
		return true;
	} 
	public boolean updateUser(String[] words, OutputStream outputStream) {
		String curId = words[2];
		if (!validator.validateSession(curId)) {
			sendToSocket("Invalid session id!\n", outputStream);
			return false;
		}
		Session s = validator.findSessionFromID(curId);
		User u = sessions.get(s);
		if (expiredSession(u)) {
			sendToSocket("Your session has expired! Please log in again\n", outputStream);
			removeFromSessions(u.getUsername(), curId);
			return false;
		}
		String userName = u.getUsername();
		removeFromSessions(userName, null);
		users.remove(userName);
		loggedIn.remove(userName);
		
		int sizeLine = words.length;
		final int oneOptionLenght = 5;
		final int twoOptionLenght = 7;
		final int threeOptionLenght = 9;
		final int fourOptionLenght = 11;
		final int threeWordsLength = 3;
		final int fiveWordsLength = 5;
		final int sevenWordsLength = 7;
		final int nineWordsLength = 9;
		if (sizeLine >= oneOptionLenght) {
			findOutOption(words, threeWordsLength, u);
		}
		if (sizeLine >= twoOptionLenght) {
			findOutOption(words, fiveWordsLength, u);
		}
		if (sizeLine >= threeOptionLenght) {
			findOutOption(words, sevenWordsLength, u);
		}
		if (sizeLine == fourOptionLenght) {
			findOutOption(words, nineWordsLength, u);
		}
 		users.put(u.getUsername(), u);
		loggedIn.put(u.getUsername(), u);
		
		Session sesh = new Session();
		sessions.put(sesh, u);
		updateFile();
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(u.getUsername());
		msg.append(" has successfully updated their information\n");
		msg.append("Session with id ");
		msg.append(sesh.getID());
		msg.append(" and ttl ");
		msg.append(sesh.getTimeToLive());
		msg.append(" has been created\n");
		sendToSocket(msg.toString(), outputStream);
	
		return true;
	}

}
