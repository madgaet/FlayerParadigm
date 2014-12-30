package com.trollCorporation.security;

import com.trollCorporation.common.model.User;
import com.trollCorporation.common.model.operations.ConnectionOperation;
import com.trollCorporation.common.model.operations.MessageOperation;
import com.trollCorporation.common.model.operations.Operation;
import com.trollCorporation.common.model.operations.RegisterOperation;

public final class OperationValidator {
	
	public static boolean isValidConnectionOperation(Operation operation) {
		if (operation instanceof ConnectionOperation) {
			ConnectionOperation connOpe = (ConnectionOperation) operation;
			if (connOpe.getUserName() != null && connOpe.getEncryptedPassword() != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidRegisterOperation(Operation operation) {
		if (operation instanceof RegisterOperation) {
			RegisterOperation registerOpe = (RegisterOperation) operation;
			if (registerOpe.getUser() != null) {
				User user = registerOpe.getUser();
				if (user.getName() != null && user.getPassword() != null && user.getEmail() != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isValidMessageOperation(Operation operation) {
		if (operation instanceof MessageOperation) {
			MessageOperation messageOpe = (MessageOperation) operation;
			if (messageOpe.getMessage() != null) {
				return true;
			}
		}
		return false;
	}
}
