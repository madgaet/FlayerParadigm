package com.trollCorporation.common.model.operations;

public enum OperationType {
	
	CONNECTION,
	CHATBOX_MAILING,
	CHATBOX_USERS_LISTING,
	REGISTRATION;
	
	public static boolean isADisconnectedOperation(final OperationType type) {
		if (type.equals(CONNECTION) || type.equals(REGISTRATION)) {
			return true;
		}
		return false;
	}
}
