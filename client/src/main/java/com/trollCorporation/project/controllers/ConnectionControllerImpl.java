package com.trollCorporation.project.controllers;

import com.trollCorporation.common.exceptions.AuthenticationException;
import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.model.ConnectionOperation;
import com.trollCorporation.common.model.Operation;
import com.trollCorporation.common.model.User;
import com.trollCorporation.services.ConnectionToServer;

public class ConnectionControllerImpl implements ConnectionController {
	
	private ConnectionToServer connection;
	private User user;
	
	public ConnectionControllerImpl(final User user, final ConnectionToServer connection) {
		this.user = user;
		this.connection = connection;
	}
	
	public void connect() throws AuthenticationException, ConnectionException {
		ConnectionOperation operation = new ConnectionOperation(user);
		Operation response = connection.sendUnconnectedOperation(operation);
		if (response == null || !((ConnectionOperation)response).isConnected()) {
			throw new AuthenticationException("Fail to authenticate!");
		}
	}
	
	public User getUser() {
		return user;
	}

}
