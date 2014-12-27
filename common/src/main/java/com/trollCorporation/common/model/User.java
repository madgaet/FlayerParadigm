package com.trollCorporation.common.model;

import java.io.Serializable;

import com.trollCorporation.common.utils.EncryptionUtils;

public class User implements Serializable{
	
	private static final long serialVersionUID = 3804073669997133589L;
	private String name;
	private byte[] encryptedPassword;
	private String email;
	
	public User(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPassword(String password) {
		this.encryptedPassword = encryptPassword(password);
	}
	
	private byte[] encryptPassword(String password) {
		return EncryptionUtils.encrypt(password, name);
	}
	
	public void setEncryptedPassword(byte[] encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}	
	
	public byte[] getPassword() {
		return encryptedPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
