package com.trollCorporation.common.model;

import java.io.Serializable;

public class Message implements Serializable {
	
	private static final long serialVersionUID = -424109028164710773L;
	private String message;
	
	public Message(String message) {
		this.message = message;
	}
	
	public String getMessageValue() {
		return message;
	}
}
