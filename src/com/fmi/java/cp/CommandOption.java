package com.fmi.java.cp;

import java.util.ArrayList;
import java.util.List;

public enum CommandOption {
	USERNAME("--username"), PASSWORD("--password"), OLD_PASSWORD("--old-password"), NEW_PASSWORD(
			"--new-password"), FIRST_NAME(
					"--first-name"), LAST_NAME("--last-name"), EMAIL("--email"), SESSION_ID("-–session-id");

	public final String label;

	private CommandOption(String label) {
		this.label = label;
	}

	public static List<String> getAllCommandsOptions() {
		List<String> allLabels = new ArrayList<>();

		CommandOption[] allCommandOptionValues = values();
		for (int i = 0; i < allCommandOptionValues.length; i++) {
			allLabels.add(allCommandOptionValues[i].label);
		}

		return allLabels;
	}

}
