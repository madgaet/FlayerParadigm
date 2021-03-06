package com.trollCorporation.project.ihm.connection;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.trollCorporation.common.exceptions.AuthenticationException;
import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.TimeoutException;
import com.trollCorporation.common.exceptions.UserAlreadyConnectedException;
import com.trollCorporation.project.client.Game;

public class ConnectionView {

	private ConnectionPage parentPage;
	private Box view;

	private JTextField jtfUsername;
	private JTextField jtfPassword;
	private JButton loginButton;

	public ConnectionView(ConnectionPage page) {
		this.parentPage = page;
		this.view = createConnexionView();
	}
	
	public JButton getButton() {
		return loginButton;
	}

	public Box getView() {
		return this.view;
	}

	public Box createConnexionView() {
		Box content = Box.createVerticalBox();

		// Name field
		Box nameField = Box.createHorizontalBox();
		nameField.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		JLabel nameLabel = new JLabel("Username : ");
		nameField.add(nameLabel);
		jtfUsername = new JTextField();
		nameField.add(jtfUsername);
		content.add(nameField);

		// Password field
		Box pswdField = Box.createHorizontalBox();
		pswdField.setBorder(BorderFactory.createEmptyBorder(10, 0, 100, 0));
		JLabel pswdLabel = new JLabel("Password : ");
		pswdField.add(pswdLabel);
		jtfPassword = new JPasswordField();
		pswdField.add(jtfPassword);
		content.add(pswdField);

		// Buttons
		Box buttonsPanel = Box.createHorizontalBox();
		loginButton = new JButton("Login");
		loginButton.addActionListener((e) -> loginAction(e));
		buttonsPanel.add(loginButton);
		content.add(buttonsPanel);

		return content;
	}

	public void loginAction(ActionEvent arg0) {
		if (!jtfUsername.getText().trim().isEmpty()	&& !jtfPassword.getText().isEmpty()) {
			parentPage.enableTabChange(false);
			loginButton.setEnabled(false);
			setLoginMessage();
			new Thread(() -> login()).start();
		} else {
			setUserErrorMessage();
		}
	}

	private void login() {
		try {
			Game.getInstance().connect(jtfUsername.getText(), jtfPassword.getText());
		} catch (TimeoutException t) {
			setTimeoutErrorMessage();
		} catch (ConnectionException e) {
			setInternetErrorMessage();
		} catch (AuthenticationException a) {
			setAuthenticationErrorMessage();
		} catch (UserAlreadyConnectedException u) {
			setUserAlreadyConnectedErrorMessage();
		} catch (Exception e) {
			// PROBLEM
		} finally {
			reset();
		}
	}
	
	private void setLoginMessage() {
		parentPage.setInfoMessage("Trying to connect, please wait!");
	}

	private void setTimeoutErrorMessage() {
		parentPage.setErrorMessage("The server is currently overloaded! Please try again.");
	}
	
	private void setInternetErrorMessage() {
		parentPage.setErrorMessage("Verify your internet connection!");
	}
	
	private void setUserAlreadyConnectedErrorMessage() {
		parentPage.setErrorMessage("User already connected");
	}

	private void setUserErrorMessage() {
		reset();
		if (jtfUsername.getText().trim().isEmpty()) {
			jtfUsername.setBorder(BorderFactory.createLineBorder(Color.RED));
			jtfUsername.setToolTipText("Username can't be empty!");
		}
		if (jtfPassword.getText().isEmpty()) {
			jtfPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			jtfPassword.setToolTipText("Password can't be empty!");
		}
		parentPage.setErrorMessage("Some field(s) cannot be empty!");
		parentPage.enableTabChange(true);
	}

	private void setAuthenticationErrorMessage() {
		parentPage.setErrorMessage("Wrong combinaison login/password");
	}

	public void reset() {
		loginButton.setEnabled(true);
		parentPage.enableTabChange(true);
		jtfUsername.setBorder(null);
		jtfPassword.setBorder(null);
	}
}
