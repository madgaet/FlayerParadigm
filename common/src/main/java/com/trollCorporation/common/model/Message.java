package com.trollCorporation.common.model;

import java.io.Serializable;

public class Message implements Serializable {
	
	private static final long serialVersionUID = -424109028164710773L;
	private String message;
	private String sender;
	
	public Message(final String message, final String sender) {
		this.message = message;
		this.sender = sender;
	}
	
	public String getMessageValue() {
		return message;
	}
	
	public String getSender() {
		return sender;
	}
}
