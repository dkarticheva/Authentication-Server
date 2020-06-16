package com.fmi.java.cp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class ValidatorTest {

	@Test
	void testRegiterCommandWithMissingUsename() {
		
		String registerCommandLine = "register --password somePass --first-name John --last-name Dork --email johnDork@abv.bg";
		assertFalse(Validator.isValidCommand(registerCommandLine));
	}
	
	@Test
	void testRegiterCommandWithMissingPassword() {
		
		String registerCommandLine = "register --username jDork --first-name John --last-name Dork --email johnDork@abv.bg";
		assertFalse(Validator.isValidCommand(registerCommandLine));
	}
	
	@Test
	void testRegiterCommandWithMissingFirstName() {
		
		String registerCommandLine = "register --username jDork --password somePass --last-name Dork --email johnDork@abv.bg";
		assertFalse(Validator.isValidCommand(registerCommandLine));
	}
	
	@Test
	void testRegiterCommandWithMissingLastName() {
		
		String registerCommandLine = "register --username jDork --password somePass --first-name John --email johnDork@abv.bg";
		assertFalse(Validator.isValidCommand(registerCommandLine));
	}
	
	@Test
	void testRegiterCommandWithMissingEmail() {
		
		String registerCommandLine = "register --username jDork --password somePass --first-name John --last-name Dork";
		assertFalse(Validator.isValidCommand(registerCommandLine));
	}
	
	@Test
	void testRegiterCommandWithCorrectOptions() {
		
		String registerCommandLine = "register --username jDork --password somePass --first-name John --last-name Dork --email johnDork@abv.bg";
		assertTrue(Validator.isValidCommand(registerCommandLine));
	}
	
	@Test
	void testLoginCommandWithMissingUsername() {
		
		String loginCommandLine = "login --password somePass";
		assertFalse(Validator.isValidCommand(loginCommandLine));
	}
	
	@Test
	void testLoginCommandWithMissingPassword() {
		
		String loginCommandLine = "login -–username jDork";
		assertFalse(Validator.isValidCommand(loginCommandLine));
	}
	
	@Test
	void testLoginCommandWithCorrectOptions() {
		
		String loginCommandLine = "login -–username jDork --password somePass";
		assertTrue(Validator.isValidCommand(loginCommandLine));
	}
	
	@Test
	void testLoginWithNoOptions() {
		
		String loginCommandLine = "login";
		assertFalse(Validator.isValidCommand(loginCommandLine));
	}
	
	@Test
	void testLoginWithNoSessionIdValue() {
		
		String loginCommandLine = "login -–session-id";
		assertFalse(Validator.isValidCommand(loginCommandLine));
	}
	
	@Test
	void testLoginWithCorrectOptions() {
		
		String loginCommandLine = "login -–session-id correctSessionId";
		assertTrue(Validator.isValidCommand(loginCommandLine));
	}
	
	@Test
	void testResetPasswordCommandWithMissingUsername() {
		
		String resetPasswordCommandLine = "reset-password --old-password somePass --new-password someNewPass";
		assertFalse(Validator.isValidCommand(resetPasswordCommandLine));
	}
	
	@Test
	void testResetPasswordCommandWithMissingOldPassword() {
		
		String resetPasswordCommandLine = "reset-password –-username jDork --new-password someNewPass";
		assertFalse(Validator.isValidCommand(resetPasswordCommandLine));
	}
	
	@Test
	void testResetPasswordCommandWithMissingNewPassword() {
		
		String resetPasswordCommandLine = "reset-password –-username jDork --old-password somePass";
		assertFalse(Validator.isValidCommand(resetPasswordCommandLine));
	}
	
	@Test
	void testResetPasswordCommandWithCorrectOptions() {
		
		String resetPasswordCommandLine = "reset-password –-username jDork --old-password somePass --new-password someNewPass";
		assertTrue(Validator.isValidCommand(resetPasswordCommandLine));
	}
	
	@Test
	void testUpdateUserCommandWithNoOptions() {
		String updateUserCommandLine = "update-user";
		assertFalse(Validator.isValidCommand(updateUserCommandLine));
	}
	
	@Test
	void testUpdateUserCommandWithNoSessionIdValue() {
		String updateUserCommandLine = "update-user -–session-id";
		assertFalse(Validator.isValidCommand(updateUserCommandLine));
	}
	
	@Test
	void testUpdateUserCommandWithCorrectOptionSessionId() {
		String updateUserCommandLine = "update-user -–session-id correctSessionId";
		assertTrue(Validator.isValidCommand(updateUserCommandLine));
	}
	
	@Test
	void testUpdateUserCommandWithSessionIdAndNoValueForNewUsername() {
		String updateUserCommandLine = "update-user -–session-id correctSessionId -–new-username";
		assertFalse(Validator.isValidCommand(updateUserCommandLine));
	}
	
	@Test
	void testUpdateUserCommandWithCorrectOptionSessionIdAndNewUsername() {
		String updateUserCommandLine = "update-user -–session-id correctSessionId -–new-username bigDork";
		assertTrue(Validator.isValidCommand(updateUserCommandLine));
	}
	
	@Test
	void testLogoutCommandWithNoOptions() {
		
		String logoutCommandLine = "logout";
		assertFalse(Validator.isValidCommand(logoutCommandLine));
	}
	
	@Test
	void testLogoutCommandWithNoSessionIdValue() {
		
		String logoutCommandLine = "logout -–session-id";
		assertFalse(Validator.isValidCommand(logoutCommandLine));
	}
	
	@Test
	void testLogoutCommandWithCorrectOptions() {
		
		String logoutCommandLine = "logout --session-id correctSessionId";
		assertTrue(Validator.isValidCommand(logoutCommandLine));
	}
	
	@Test
	void testDeleteUserCommandWithNoOptions() {
		
		String deleteUserCommandLine = "delete-user";
		assertFalse(Validator.isValidCommand(deleteUserCommandLine));
	}
	
	@Test
	void testDeleteUserCommandWithNoSessionIdValue() {
		
		String deleteUserCommandLine = "delete-user -–session-id";
		assertFalse(Validator.isValidCommand(deleteUserCommandLine));
	}
	
	@Test
	void testDeleteUserCommandWithCorrectOptions() {
		
		String deleteUserCommandLine = "delete-user –username jDork";
		assertTrue(Validator.isValidCommand(deleteUserCommandLine));
	}

}
