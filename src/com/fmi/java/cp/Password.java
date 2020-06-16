package com.fmi.java.cp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {

	private static final String algorithmForHashing = "MD5";

	public static String hashPassword(String plainTextPassword) {
		return hash(plainTextPassword);
	}

	public static boolean authenticate(String plainTextPassword, String storedHashedPassword) {

		String hashedPassword = hash(plainTextPassword);
		return hashedPassword.equals(storedHashedPassword);
	}

	private static String hash(String plainTextPassword) {

		try {
			MessageDigest mdInstance = MessageDigest.getInstance(algorithmForHashing);
			mdInstance.update(plainTextPassword.getBytes());

			byte[] bytes = mdInstance.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				final int hexaNumber = 0xff;
				final int offset = 16;
				sb.append(Integer.toString((bytes[i] & hexaNumber) + hexaNumber, offset).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Issue with hashing algorithm " + algorithmForHashing);
		}
		return "";
	}

}
