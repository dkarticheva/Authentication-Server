package com.fmi.java.cp;

public class CommandResult {
	
	private String resultMessage;
	
	public CommandResult() {}
	
	public CommandResult(String commandResult) {
		resultMessage = commandResult;
	}
	
	public void setResultMessage(String commandResult) {
		resultMessage = commandResult;
	}
	
	public String getResultMessage() {
		return resultMessage;
	}
}
