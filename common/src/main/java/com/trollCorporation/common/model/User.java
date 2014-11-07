package com.trollCorporation.common.model;

import java.io.Serializable;

import com.trollCorporation.common.utils.EncryptionUtils;

public class User implements Serializable{
	
	private static final long serialVersionUID = 3804073669997133589L;
	private String name;
	private String encryptedPassword;
	
	public User(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPassword(String password) {
		this.encryptedPassword = encryptPassword(password);
	}
	
	private String encryptPassword(String password) {
		return EncryptionUtils.encrypt(password, name);
	}
	
	public String getPassword() {
		return encryptedPassword;
	}
}
