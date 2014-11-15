package com.trollCorporation.project.controllers;

import com.trollCorporation.common.model.User;
import com.trollCorporation.project.exceptions.AuthenticationException;
import com.trollCorporation.project.exceptions.ConnectionException;
import com.trollCorporation.services.ConnectionToServer;

public class ConnectionControllerImpl implements ConnectionController {
	
	private ConnectionToServer connection;
	private User user;
	
	public ConnectionControllerImpl(final String username, final String password,
			final ConnectionToServer connection) throws ConnectionException, AuthenticationException {
		this.user = new User(username);
		this.user.setPassword(password);
		this.connection = connection;
		connect();
	}
	
	private void connect() throws AuthenticationException, ConnectionException {
		if (!connection.connectUserToServer(user)) {
			throw new AuthenticationException("Fail to authenticate!");
		}
	}
	
	public User getUser() {
		return user;
	}

}
