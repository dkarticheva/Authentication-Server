package com.fmi.java.cp;

import java.io.OutputStream;

public class UpdateUserCommand implements Command {

	@Override
	public boolean execute(String[] words, OutputStream outputStream) {
		String curId = words[2];
		if (!validator.validateSession(curId)) {
			CommandsExecutor.sendToSocket("Invalid session id!\n", outputStream);
			return false;
		}
		Session s = validator.findSessionFromID(curId);
		User u = CommandsExecutor.getSessions().get(s);
		if (validator.isSessionExpired(u)) {
			CommandsExecutor.sendToSocket("Your session has expired! Please log in again\n", outputStream);
			CommandsExecutor.removeFromSessions(u.getUsername(), curId);
			return false;
		}
		String userName = u.getUsername();
		CommandsExecutor.removeFromSessions(userName, null);
		CommandsExecutor.getUsers().remove(userName);
		CommandsExecutor.getLoggedIn().remove(userName);
		
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
			CommandsExecutor.findOutOption(words, threeWordsLength, u);
		}
		if (sizeLine >= twoOptionLenght) {
			CommandsExecutor.findOutOption(words, fiveWordsLength, u);
		}
		if (sizeLine >= threeOptionLenght) {
			CommandsExecutor.findOutOption(words, sevenWordsLength, u);
		}
		if (sizeLine == fourOptionLenght) {
			CommandsExecutor.findOutOption(words, nineWordsLength, u);
		}
		CommandsExecutor.getUsers().put(u.getUsername(), u);
		CommandsExecutor.getLoggedIn().put(u.getUsername(), u);
		
		Session sesh = new Session();
		CommandsExecutor.getSessions().put(sesh, u);
		CommandsExecutor.updateFile();
		
		StringBuilder msg = new StringBuilder("User ");
		msg.append(u.getUsername());
		msg.append(" has successfully updated their information\n");
		msg.append("Session with id ");
		msg.append(sesh.getID());
		msg.append(" and ttl ");
		msg.append(sesh.getTimeToLive());
		msg.append(" has been created\n");
		CommandsExecutor.sendToSocket(msg.toString(), outputStream);
	
		return true;
	}

}
