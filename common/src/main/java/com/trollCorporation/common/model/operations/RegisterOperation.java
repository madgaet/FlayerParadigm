package com.trollCorporation.common.model.operations;

import com.trollCorporation.common.model.User;
import com.trollCorporation.common.model.enums.OperationType;

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
