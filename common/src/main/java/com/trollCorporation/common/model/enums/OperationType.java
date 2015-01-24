package com.trollCorporation.common.model.enums;

public enum OperationType {
	
	CONNECTION,
	CHATBOX_MAILING,
	USERS_LISTING,
	FRIENDS_REQUEST,
	REGISTRATION;
	
	public static boolean isADisconnectedOperation(final OperationType type) {
		if (type.equals(CONNECTION) || type.equals(REGISTRATION)) {
			return true;
		}
		return false;
	}
}
