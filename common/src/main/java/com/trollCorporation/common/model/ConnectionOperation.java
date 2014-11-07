package com.trollCorporation.common.model;


public class ConnectionOperation extends Operation {

	private static final long serialVersionUID = 9078692750717887012L;
	private User user;
	private boolean connected = false;

	public ConnectionOperation(User user) {
		super(OperationType.CONNECTION);
		this.user = user;
	}
	
	public String getUserName() {
		return user.getName();
	}
	
	public String getEncryptedPassword() {
		return user.getPassword();
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void connect(String encryptPassword) {
//TODO		this.connected = user.getPassword().equals(encryptPassword);
		this.connected = user.getName() != null && !user.getName().isEmpty();
	}

}
