package com.fmi.java.cp;

public class CommandResult {
	
	private boolean success;
	private String resultMessage;
	
	public CommandResult() {}
	
	public CommandResult(boolean isSuccess) {
		success = isSuccess;
	}
	
	public CommandResult(String commandResult) {
		resultMessage = commandResult;
	}
	
	public void setResultMessage(String commandResult) {
		resultMessage = commandResult;
	}
	
	public String getResultMessage() {
		return resultMessage;
	}
	
	// TODO: rename pls! Or remove?
	public void setSuccess(boolean result) {
		success = result; 
	}
	
	public boolean isSuccess() {
		return success;
	}
}
