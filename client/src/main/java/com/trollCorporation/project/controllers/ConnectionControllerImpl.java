package com.trollCorporation.project.controllers;

import com.trollCorporation.common.exceptions.AuthenticationException;
import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.TimeoutException;
import com.trollCorporation.common.exceptions.UserAlreadyConnectedException;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.model.enums.ErrorType;
import com.trollCorporation.common.model.operations.ConnectionOperation;
import com.trollCorporation.common.model.operations.Operation;
import com.trollCorporation.services.ConnectionToServer;

public class ConnectionControllerImpl implements ConnectionController {
	
	private ConnectionToServer connection;
	private User user;
	
	public ConnectionControllerImpl(final User user, final ConnectionToServer connection) {
		this.user = user;
		this.connection = connection;
	}
	
	public void connect() throws AuthenticationException, ConnectionException, TimeoutException,
			UserAlreadyConnectedException {
		ConnectionOperation operation = new ConnectionOperation(user);
		Operation response = connection.sendUnconnectedOperation(operation);
		if (response != null && response.getErrorType() != null) {
			if (response.getErrorType().equals(ErrorType.DB_ERROR)) {
				throw new TimeoutException("Server probably overloaded!");
			} else if (response.getErrorType().equals(ErrorType.USER_ALREADY_CONNECTED)) {
				throw new UserAlreadyConnectedException("User already in use!");
			}
		} else if (response == null || !((ConnectionOperation)response).isConnected()) {
			throw new AuthenticationException("Fail to authenticate!");
		} 
	}
	
	public User getUser() {
		return user;
	}

}
