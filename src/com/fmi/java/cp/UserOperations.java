package com.fmi.java.cp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

// TODO: change this name
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
	
	// TODO: those 2 funcs - do something!
	public static boolean confirmExistenceOfUserWithUsername(String username) {
		
		return allUsers.containsKey(username);
	}
	
	public static boolean isUsernameAlreadyTaken(String username) {
		return confirmExistenceOfUserWithUsername(username);
	}
	
	public static boolean validatePasswordForUser(User user, String password) {
		
		return Password.authenticate(password, user.getPassword());
	}
	
	public static boolean isUserAlreadyLoggedIn(String userName) {
		return loggedInUsers.containsKey(userName);
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

}
