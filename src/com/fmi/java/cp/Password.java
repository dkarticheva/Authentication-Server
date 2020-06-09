package com.fmi.java.cp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
	
	private static String hash(String plainTextPassword) {
		
		// TODO: this null!
        String hashedPassword = null;
        try {
            MessageDigest mdInstance = MessageDigest.getInstance("MD5"); 
            mdInstance.update(plainTextPassword.getBytes());
            byte[] bytes = mdInstance.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length ; i++) {
            	final int hexaNumber = 0xff;
            	final int offset = 16;
                sb.append(Integer.toString((bytes[i] & hexaNumber) + hexaNumber, offset).substring(1));
            }
            hashedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Issue with that hashing algorithm");
        }
        return hashedPassword;
    }
	
	public static String hashPassword(String plainTextPassword) {
		return hash(plainTextPassword);
	}
	
	public static boolean authenticate(String plainTextPassword, String storedHashedPassword) {
		
		String hashedPassword = hash(plainTextPassword);
		return hashedPassword.equals(storedHashedPassword);
	}
}
