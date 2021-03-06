package com.trollCorporation.project.controllers;

import com.trollCorporation.common.exceptions.AuthenticationException;
import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.TimeoutException;
import com.trollCorporation.common.exceptions.UserAlreadyConnectedException;
import com.trollCorporation.common.model.User;

public interface ConnectionController {

	void connect() throws AuthenticationException, ConnectionException, TimeoutException,
		UserAlreadyConnectedException;
	User getUser();
}
