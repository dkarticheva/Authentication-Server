package com.fmi.java.cp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
	
	// TODO : change this to private
	public static String hash(String plainTextPassword) {
		
        String hashedPassword = null;
        try {
        	//Returns a MessageDigest object that implements the specified digest algorithm - in this case MD5.
            MessageDigest mdInstance = MessageDigest.getInstance("MD5"); 
            //The update method is used for processing the data through the MessageDigest object.
            mdInstance.update(plainTextPassword.getBytes());
            //The digest method completes the hash computation
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
            System.out.println("Issue with tha hashing algorithm");
        }
        return hashedPassword;
    }
	public static boolean authenticate(String plainTextPassword, String storedHashedPassword) {
		
		String hashedPassword = hash(plainTextPassword);
		return hashedPassword.equals(storedHashedPassword);
	}
}
