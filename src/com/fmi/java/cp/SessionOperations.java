package com.fmi.java.cp;

import java.util.Map;
import java.util.Map.Entry;

// TODO: change the name!
public class SessionOperations {
	
	private static Map<Session, User> activeSessionsOfUsers;
	
	public static void addSessionForUser(Session session, User user) {
		activeSessionsOfUsers.put(session, user);
	}
	
	public static boolean isSessionValid(String sessionId) {
		
		for (Session session : activeSessionsOfUsers.keySet()) {
			if (session.getID().equals(sessionId)) {
				return true;
			}
		}
		return false;
	}
	
	public static Session getSessionFromId(String sessionId) {
		
		for (Session session : activeSessionsOfUsers.keySet()) {
			if (session.getID().equals(sessionId)) {
				return session;
			}
		}
		return null;
	}
	
	public static User getUserBySessionId(String sessionId) {
		
		Session userSession = getSessionFromId(sessionId);
		return activeSessionsOfUsers.get(userSession);
	}
	
	public static boolean isSessionExpiredForUser(User user) {
		
		return activeSessionsOfUsers.containsValue(user);
	}
	
	public static void removeExpiredSessionWithSessionId(String sessionId) {
		
		Session expiredSession = getSessionFromId(sessionId);
		activeSessionsOfUsers.remove(expiredSession);
	}
	
	public static void removeSessionForUser(User user) {
		
		for (Entry<Session, User> sessionAndUser : activeSessionsOfUsers.entrySet()) {
			
			Session oldSession = sessionAndUser.getKey();
			User currentUser = sessionAndUser.getValue();
			
			if (currentUser.equals(user)) {
				activeSessionsOfUsers.remove(oldSession, currentUser);
			}
		}
	}

}
