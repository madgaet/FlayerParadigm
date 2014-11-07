package com.trollCorporation.project.client;

import java.io.IOException;

import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.controllers.ChatboxOperationsControllerImpl;
import com.trollCorporation.project.controllers.ConnectionControllerImpl;
import com.trollCorporation.project.exceptions.ConnectionException;
import com.trollCorporation.project.ihm.HomePage;
import com.trollCorporation.project.ihm.connection.ConnectionPage;
import com.trollCorporation.services.ConnectionToServer;

public class Game {

	private static Game singleton;
	
	private ConnectionPage connPage;
	private HomePage homePage;
	
	private ConnectionToServer connection;
	
	private ChatboxOperationsController chatboxController;
	
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
			connection = new ConnectionToServer();
		}
	}
	
	private void setDownConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (IOException e) {
				//do nothing the connection is closed anyway
			}
			connection = null;
		}
	}
	
	public void connect(String username, String password) throws ConnectionException {
		setUpConnection();
		new ConnectionControllerImpl(username, password, connection);
		chatboxController = new ChatboxOperationsControllerImpl(connection);
		homePage = new HomePage(username, chatboxController);
		connPage.setVisible(false);
	}
	
	public void disconnect() {
		setDownConnection();
		homePage.dispose();
		homePage = null;
		connPage.setVisible(true);
	}
}
