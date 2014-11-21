package com.trollCorporation.project.client;

import com.trollCorporation.common.exceptions.AuthenticationException;
import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.model.Operation;
import com.trollCorporation.common.model.RegisterOperation;
import com.trollCorporation.common.model.User;
import com.trollCorporation.project.controllers.ConnectionController;
import com.trollCorporation.project.controllers.ConnectionControllerImpl;
import com.trollCorporation.project.ihm.HomePage;
import com.trollCorporation.project.ihm.connection.ConnectionPage;
import com.trollCorporation.services.ConnectionToServer;
import com.trollCorporation.services.OperationProcessor;

public class Game {

	private static Game singleton;
	
	private ConnectionPage connPage;
	private HomePage homePage;
	
	private ConnectionToServer connection;
	
	private ConnectionController connectionController;
	
	private Game() {
		connPage = new ConnectionPage();
	}
	
	public static Game getInstance(){
		if (singleton == null) {
			singleton = new Game();
		}
		return singleton;
	}
	
	private void setUpConnection() throws ConnectionException {
		if (connection == null) {
			connection = ConnectionToServer.getInstance();
		}
	}
	
	private void setDownConnection() {
		if (connection != null) {
			connection.close();
		}
	}
	
	public void connect(final String username, final String password) 
			throws ConnectionException, AuthenticationException {
		setUpConnection();
		try {
			User user = new User(username);
			user.setPassword(password);
			connectionController = new ConnectionControllerImpl(user, connection);
			connectionController.connect();
		} catch (AuthenticationException a) {
			setDownConnection();
			throw a;
		}
		homePage = new HomePage(username);
		OperationProcessor.getInstance().start();
		connPage.setVisible(false);
	}
	
	public RegisterOperation register(final Operation operation) throws ConnectionException {
		setUpConnection();
		Operation response = connection.sendUnconnectedOperation(operation);
		return (RegisterOperation)response;
	}
	
	public void disconnect() {
		setDownConnection();
		homePage.dispose();
		homePage = null;
		connPage.setVisible(true);
		connPage.reset();
	}
}
