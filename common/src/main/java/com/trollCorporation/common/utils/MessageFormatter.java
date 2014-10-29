package com.trollCorporation.common.utils;

public class MessageFormatter {
	
	public static String formatSentMessage(final String message, final String name) {
		String formatedMessage = "";
		formatedMessage += "[" + name + "] : ";
		formatedMessage += message;
		return formatedMessage;
	}
}
