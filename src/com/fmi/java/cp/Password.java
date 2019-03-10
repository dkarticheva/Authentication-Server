package com.fmi.java.cp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {
	
	public static String hash(String passwordToHash)
    {
        String generatedPassword = null;
        try {
        	//Returns a MessageDigest object that implements the specified digest algorithm - in this case MD5.
            MessageDigest md = MessageDigest.getInstance("MD5"); 
            //The update method is used for processing the data through the MessageDigest object.
            md.update(passwordToHash.getBytes());
            //The digest method completes the hash computation
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length ; i++) {
            	final int hexaNumber = 0xff;
            	final int offset = 16;
                sb.append(Integer.toString((bytes[i] & hexaNumber) + hexaNumber, offset).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Issue with tha hashing algorithm");
        }
        return generatedPassword;
    }
	public static boolean authenticate(String password, String hashedPassword) {
		String hPassword = hash(password);
		return hPassword.equals(hashedPassword);
	}
}
