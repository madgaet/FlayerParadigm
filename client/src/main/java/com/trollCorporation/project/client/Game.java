package com.trollCorporation.project.client;

import com.trollCorporation.project.controllers.ConnectionControllerImpl;
import com.trollCorporation.project.exceptions.AuthenticationException;
import com.trollCorporation.project.exceptions.ConnectionException;
import com.trollCorporation.project.ihm.HomePage;
import com.trollCorporation.project.ihm.connection.ConnectionPage;
import com.trollCorporation.services.ConnectionToServer;

public class Game {

	private static Game singleton;
	
	private ConnectionPage connPage;
	private HomePage homePage;
	
	private ConnectionToServer connection;
	
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
	
	public void connect(String username, String password) 
			throws ConnectionException, AuthenticationException {
		setUpConnection();
		try {
			new ConnectionControllerImpl(username, password, connection);
		} catch (AuthenticationException a) {
			setDownConnection();
			throw a;
		}
		homePage = new HomePage(username);
		connPage.setVisible(false);
	}
	
	public void disconnect() {
		setDownConnection();
		homePage.dispose();
		homePage = null;
		connPage.setVisible(true);
	}
}
