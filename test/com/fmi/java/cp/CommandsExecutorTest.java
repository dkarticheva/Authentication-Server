package com.fmi.java.cp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CommandsExecutorTest {
	
	private int sessionPastExpirationSeconds = 100;
	
	private static void loginDefaultTestUser() {
		String loginCommandLine = "login -–username jDork --password somePass";
		String[] loginCommandSeparatedWords = loginCommandLine.split(" ");
		
		CommandsExecutor.logIn(loginCommandSeparatedWords);
	}
	
	private static String getSessionIdForDefaultTestUser() {
		User user = UserOperations.getUserByUsername("jDork");
		Session session = SessionOperations.getSessionForUser(user);
		return session.getID();
	}

	@BeforeAll
	public static void registerUserWithCorrectInformation() {
		String registerCommandLine = "register --username jDork --password somePass --first-name John --last-name Dork --email johnDork@abv.bg";
		String[] registerCommandSeparatedWords = registerCommandLine.split(" ");
		CommandsExecutor.register(registerCommandSeparatedWords);
	}
	
	@Test
	void testLoginWithExistingUserAndCorrectPassword() {
		String loginCommandLine = "login -–username jDork --password somePass";
		String[] loginCommandSeparatedWords = loginCommandLine.split(" ");
		
		CommandResult loginResult = CommandsExecutor.logIn(loginCommandSeparatedWords);
		
		String resultMessage = loginResult.getResultMessage();
		assertTrue(resultMessage.startsWith("User jDork has been successfully logged"));
	}
	
	@Test
	void testLoginWithExistingUserAndInCorrectPassword() {
		String loginCommandLine = "login -–username jDork --password someincorrectPass";
		String[] loginCommandSeparatedWords = loginCommandLine.split(" ");
		
		CommandResult loginResult = CommandsExecutor.logIn(loginCommandSeparatedWords);
		
		String resultMessage = loginResult.getResultMessage();
		assertTrue(resultMessage.startsWith("Wrong username or password!"));
	}
	
	 
	void testResetPasswordWithCorrectCredentials() {
		loginDefaultTestUser();
		String resetPasswordLine = "reset-password –-username jDork --old-password somePass --new-password someNewPass";
		String[] resetPasswordSeparateWords = resetPasswordLine.split(" ");
		
		CommandResult resetPasswordResult = CommandsExecutor.resetPassword(resetPasswordSeparateWords);
		
		String resultMessage = resetPasswordResult.getResultMessage();
		assertEquals("Password for user jDork has been successfully modified\n", resultMessage);
	}
	
	@Test 
	void testResetPasswordWithIncorrectUsername() {
		loginDefaultTestUser();
		String resetPasswordLine = "reset-password –-username johnDork --old-password somePass --new-password someNewPass";
		String[] resetPasswordSeparateWords = resetPasswordLine.split(" ");
		
		CommandResult resetPasswordResult = CommandsExecutor.resetPassword(resetPasswordSeparateWords);
		
		String resultMessage = resetPasswordResult.getResultMessage();
		assertEquals("Wrong username or password!\n", resultMessage);
	}
	
	@Test
	void testResetPasswordWithExpiredSession() throws InterruptedException {
		loginDefaultTestUser();
		TimeUnit.SECONDS.sleep(sessionPastExpirationSeconds);
		
		String resetPasswordLine = "reset-password –-username jDork --old-password somePass --new-password someNewPass";
		String[] resetPasswordSeparateWords = resetPasswordLine.split(" ");
		
		CommandResult resetPasswordResult = CommandsExecutor.resetPassword(resetPasswordSeparateWords);
		
		String resultMessage = resetPasswordResult.getResultMessage();
		assertEquals("Your session has expired! Please log in again\n", resultMessage);
	}
	
	@Test 
	void testDeleteUserWithCorrectUsername() {
		loginDefaultTestUser();
		String deleteUserCommandLine = "delete-user -–username jDork";
		String[] deleteUserCommandSeparatedWords = deleteUserCommandLine.split(" ");
		
		CommandResult deleteUserResult = CommandsExecutor.deleteUser(deleteUserCommandSeparatedWords);
		
		String resultMessage = deleteUserResult.getResultMessage();
		assertEquals("User jDork has been deleted\n", resultMessage);
	}
	
	@Test 
	void testDeleteUserWithIncorrectUsername() {
		loginDefaultTestUser();
		String deleteUserCommandLine = "delete-user -–username johnDork";
		String[] deleteUserCommandSeparatedWords = deleteUserCommandLine.split(" ");
		
		CommandResult deleteUserResult = CommandsExecutor.deleteUser(deleteUserCommandSeparatedWords);
		
		String resultMessage = deleteUserResult.getResultMessage();
		assertEquals("Wrong username or password!\n", resultMessage);
	}
	
	@Test
	void testDeleteUserWithExpiredSession() throws InterruptedException {
		loginDefaultTestUser();
		TimeUnit.SECONDS.sleep(sessionPastExpirationSeconds);
		
		String deleteUserCommandLine = "delete-user –-username jDork";
		String[] deleteUserSeparateWords = deleteUserCommandLine.split(" ");
		
		CommandResult deleteUserResult = CommandsExecutor.deleteUser(deleteUserSeparateWords);
		
		String resultMessage = deleteUserResult.getResultMessage();
		assertEquals("Your session has expired! Please log in again\n", resultMessage);
	}
	
	void testLoginWithValidSessionId() {
		loginDefaultTestUser();
		String sessionId = getSessionIdForDefaultTestUser();
		
		String loginWithSessionIdCommandLine = "login -–session-id " + sessionId;
		String[] loginWithSessionIdSeparateWords = loginWithSessionIdCommandLine.split(" ");
		
		CommandResult loginResult = CommandsExecutor.logInSesId(loginWithSessionIdSeparateWords);
		
		String resultMessage = loginResult.getResultMessage();
		assertEquals("User jDork has loged in successfully\n", resultMessage);
	}
	
	@Test
	void testLoginWithInvalidSessionId() {
		loginDefaultTestUser();
		String loginWithSessionIdCommandLine = "login --sesion-id someIdThatMeansNothing";
		String[] loginWithSessionIdSeparateWords = loginWithSessionIdCommandLine.split(" ");
		
		CommandResult loginResult = CommandsExecutor.logInSesId(loginWithSessionIdSeparateWords);
		
		String resultMessage = loginResult.getResultMessage();
		assertEquals("Invalid session id!\n", resultMessage);
	}
	
	void testLogoutWithValidSessionId() {
		loginDefaultTestUser();
		String sessionId = getSessionIdForDefaultTestUser();
		
		String logoutWithSessionIdCommandLine = "logout -–session-id " + sessionId;
		String[] logoutWithSessionIdSeparateWords = logoutWithSessionIdCommandLine.split(" ");
		
		CommandResult logoutResult = CommandsExecutor.logOut(logoutWithSessionIdSeparateWords);
		
		String resultMessage = logoutResult.getResultMessage();
		assertEquals("User jDork has logged out and session with id " + sessionId + " has been terminated\n", resultMessage);
	}
	
	@Test
	void testLogoutWithInvalidSessionId() {
		loginDefaultTestUser();
		String logoutWithSessionIdCommandLine = "logout --sesion-id someIdThatMeansNothing";
		String[] logoutWithSessionIdSeparateWords = logoutWithSessionIdCommandLine.split(" ");
		
		CommandResult logoutResult = CommandsExecutor.logInSesId(logoutWithSessionIdSeparateWords);
		
		String resultMessage = logoutResult.getResultMessage();
		assertEquals("Invalid session id!\n", resultMessage);
	}

}
