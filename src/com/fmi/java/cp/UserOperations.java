package com.fmi.java.cp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class UserOperations {
	
	private static Map<String, User> allUsers;
	private static Map<String, User> loggedInUsers;
	private static File usersDetails;
	
	public static User getUserByUsername(String username) {
		return allUsers.get(username);
	}
	
	public static void addUser(User user) {
		allUsers.put(user.getUsername(), user);
	}
	
	public static void removeUser(User user) {
		loggedInUsers.remove(user.getUsername());
		allUsers.remove(user.getUsername());
	}
	
	public static void logInUser(User user) {
		loggedInUsers.put(user.getUsername(), user);
	}
	
	public static void logOutUser(User user) {
		loggedInUsers.remove(user.getUsername());
	}
	
	public static boolean isUsernameIncorrect(String username) {
		
		return !allUsers.containsKey(username);
	}
	
	public static boolean isUsernameTaken(String username) {
		return allUsers.containsKey(username);
	}
	
	public static boolean isPasswordForUserInvalid(String password, User user) {
		
		return !Password.authenticate(password, user.getPassword());
	}
	
	public static boolean isUserAlreadyLoggedIn(String userName) {
		return loggedInUsers.containsKey(userName);
	}
	
	public static void updateUsersDetails() {
		
		Collection<User> users = allUsers.values();
		
		try (BufferedWriter userDetailsWriter = new BufferedWriter(new FileWriter(usersDetails))) {
			for (User user : users) {
				userDetailsWriter.write(user.getUsername() + " ");
				userDetailsWriter.write(user.getPassword() + " ");
				userDetailsWriter.write(user.getFirstName() + " ");
				userDetailsWriter.write(user.getLastName() + " ");
				userDetailsWriter.write(user.getEmail());
				userDetailsWriter.newLine();
			}
		} catch (IOException e) {
			System.out.println("Issue while opening the database");
		}
	}

}
