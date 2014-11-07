package com.trollCorporation.project.controllers;

import com.trollCorporation.common.model.User;
import com.trollCorporation.project.exceptions.ConnectionException;
import com.trollCorporation.services.ConnectionToServer;

public class ConnectionControllerImpl implements ConnectionController {
	
	private ConnectionToServer connection;
	private User user;
	
	public ConnectionControllerImpl(final String username, final String password,
			final ConnectionToServer connection) throws ConnectionException {
		this.user = new User(username);
		this.user.setPassword(password);
		this.connection = connection;
		connect();
	}
	
	private void connect() throws ConnectionException {
		if (!connection.connectUserToServer(user)) {
			throw new ConnectionException("Fail to connect!");
		}
	}
	
	public User getUser() {
		return user;
	}

}
