package com.trollCorporation.common.model;

public class RegisterOperation  extends Operation {

	private static final long serialVersionUID = 9078694550717887012L;
	private User user;
	private boolean registered;
	
	public RegisterOperation() {
		super(OperationType.REGISTRATION);
	}
	
	public void setIsRegistered(boolean registered) {
		this.registered = registered;
	}
	
	public boolean isRegistered() {
		return registered;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
}
