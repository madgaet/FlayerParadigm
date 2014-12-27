package com.trollCorporation.common.model.operations;

import com.trollCorporation.common.model.User;
import com.trollCorporation.common.model.enums.OperationType;
import com.trollCorporation.common.utils.EncryptionUtils;


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
	
	public byte[] getEncryptedPassword() {
		return user.getPassword();
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void connect(byte[] encryptPassword) {
		if (encryptPassword == null) {
			this.connected = false;
		} else {
			this.connected = EncryptionUtils.compare(getEncryptedPassword(),encryptPassword);
		}
	}

}
