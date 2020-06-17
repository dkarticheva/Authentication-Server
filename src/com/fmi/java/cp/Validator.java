package com.fmi.java.cp;

import java.util.Arrays;
import java.util.List;

import com.fmi.java.cp.Validator;

public class Validator {

	private static List<String> validCommandNames = Arrays.asList("register", "login", "reset-password", "update-user",
			"logout", "delete-user");

	private final static int USERNAME_INDEX = 1;
	private final static int SESSIONID_INDEX = 1;
	private final static int PASSWORD_INDEX = 3;
	private final static int OLDPASSWORD_INDEX = 3;
	private final static int NEWPASSWORD_INDEX = 5;
	private final static int FIRSTNAME_INDEX = 5;
	private final static int LASTNAME_INDEX = 7;
	private final static int EMAIL_INDEX = 9;

	private final static int MIN_COMMAND_LEGTH = 1;
	private final static int MIN_UPDATE_USER_COMMAND_LENGTH = 3;
	private final static int REGISTER_COMMAND_LENGTH = 11;
	private final static int LOGIN_COMMAND_LENGTH = 5;
	private final static int LOGIN_SESSION_LENGTH = 3;
	private final static int RESET_PASSWORD_COMMAND_LENGTH = 7;
	private final static int LOGOUT_USER_COMMAND_LENGTH = 3;
	private final static int DELETE_USER_COMMAND_LENGTH = 3;

	public static boolean isValidCommand(String commandLine) {

		String[] commandNameAndOptions = commandLine.split(" ");

		if (commandNameAndOptions.length <= MIN_COMMAND_LEGTH) {
			return false;
		}
		String commandName = commandNameAndOptions[0];

		if (isCommandNameInvalid(commandName)) {
			System.out.println("Invalid command!");
			return false;

		} else if (areCommandOptionsInvalidAccordingCommandName(commandNameAndOptions)) {
			System.out.println("Incorrect command options!");
			return false;
		}

		return true;
	}

	private static boolean isCommandNameInvalid(String commandName) {
		return !validCommandNames.contains(commandName);
	}

	private static boolean areCommandOptionsInvalidAccordingCommandName(String[] commandLine) {

		String commandName = commandLine[0];
		String firstOption = commandLine[1];
		switch (commandName) {
		case "register":
			return isRegisterCommandInvalid(commandLine);

		case "reset-password":
			return isResetPasswordCommandInvalid(commandLine);

		case "update-user":
			return isUpdateUserCommandInvalid(commandLine);

		case "logout":
			return isLogoutCommandInvalid(commandLine);

		case "delete-user":
			return isDeleteUserCommandInvalid(commandLine);

		case "login":
			return firstOption.equals(CommandOption.USERNAME.label) ? isLoginCommandInvalid(commandLine)
					: isLoginCommandWithSessionInvalid(commandLine);
		}

		return true;
	}

	private static boolean isRegisterCommandInvalid(String[] commandLine) {
		return !(commandLine.length == REGISTER_COMMAND_LENGTH && commandLine[USERNAME_INDEX].equals(CommandOption.USERNAME.label)
				&& commandLine[PASSWORD_INDEX].equals(CommandOption.PASSWORD.label)
				&& commandLine[FIRSTNAME_INDEX].equals(CommandOption.FIRST_NAME.label)
				&& commandLine[LASTNAME_INDEX].equals(CommandOption.LAST_NAME.label)
				&& commandLine[EMAIL_INDEX].equals(CommandOption.EMAIL.label));
	}

	private static boolean isLoginCommandInvalid(String[] commandLine) {
		return !(commandLine.length == LOGIN_COMMAND_LENGTH && commandLine[USERNAME_INDEX].equals(CommandOption.USERNAME.label)
				&& commandLine[PASSWORD_INDEX].equals(CommandOption.PASSWORD.label));
	}

	private static boolean isLoginCommandWithSessionInvalid(String[] commandLine) {
		return !(commandLine.length == LOGIN_SESSION_LENGTH && commandLine[SESSIONID_INDEX].equals(CommandOption.SESSION_ID.label));
	}

	private static boolean isResetPasswordCommandInvalid(String[] commandLine) {
		return !(commandLine.length == RESET_PASSWORD_COMMAND_LENGTH
				&& commandLine[USERNAME_INDEX].equals(CommandOption.USERNAME.label)
				&& commandLine[OLDPASSWORD_INDEX].equals(CommandOption.OLD_PASSWORD.label)
				&& commandLine[NEWPASSWORD_INDEX].equals(CommandOption.NEW_PASSWORD.label));
	}

	private static boolean isUpdateUserCommandInvalid(String[] commandLine) {

		if (commandLine.length < MIN_UPDATE_USER_COMMAND_LENGTH) {
			return true;
		}

		List<String> possibleOptionNames = CommandOption.getAllCommandsOptions();

		for (int i = commandLine.length - 1; i >= 1; i -= 2) {

			String optionValue = commandLine[i];
			String optionName = commandLine[i - 1];

			if (optionValue.isEmpty() || !possibleOptionNames.contains(optionName)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isLogoutCommandInvalid(String[] commandLine) {
		return !(commandLine.length == LOGOUT_USER_COMMAND_LENGTH
				&& commandLine[SESSIONID_INDEX].equals(CommandOption.SESSION_ID.label));
	}

	private static boolean isDeleteUserCommandInvalid(String[] commandLine) {
		return !(commandLine.length == DELETE_USER_COMMAND_LENGTH
				&& commandLine[USERNAME_INDEX].equals(CommandOption.USERNAME.label));
	}

}
