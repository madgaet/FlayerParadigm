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

	public Box getView() {
		return this.view;
	}

	public Box createConnexionView() {
		Box content = Box.createVerticalBox();

		// Name field
		Box nameField = Box.createHorizontalBox();
		nameField.setBorder(BorderFactory.createEmptyBorder(50, 0, 25, 0));
		JLabel nameLabel = new JLabel("Username : ");
		nameField.add(nameLabel);
		jtfUsername = new JTextField();
		nameField.add(jtfUsername);
		content.add(nameField);

		// Password field
		Box pswdField = Box.createHorizontalBox();
		pswdField.setBorder(BorderFactory.createEmptyBorder(25, 0, 50, 0));
		JLabel pswdLabel = new JLabel("Password : ");
		pswdField.add(pswdLabel);
		jtfPassword = new JPasswordField();
		pswdField.add(jtfPassword);
		content.add(pswdField);

		// Buttons
		Box buttonsPanel = Box.createHorizontalBox();
		buttonsPanel.setAlignmentX(Box.CENTER_ALIGNMENT);
		loginButton = new JButton("Login");
		loginButton.addActionListener((e) -> loginAction(e));
		buttonsPanel.add(loginButton);
		content.add(buttonsPanel);

		return content;
	}

	public void loginAction(ActionEvent arg0) {
		parentPage.enableTabChange(false);
		loginButton.setEnabled(false);
		setLoginMessage();
		new Thread(() -> login()).start();
	}

	private void login() {
		try {
			if (!jtfUsername.getText().trim().isEmpty()
					& !jtfPassword.getText().isEmpty()) {
				Game.getInstance().connect(jtfUsername.getText(),
						jtfPassword.getText());
			} else {
				setUserErrorMessage();
			}
		} catch (TimeoutException t) {
			setTimeoutErrorMessage();
		} catch (ConnectionException e) {
			setInternetErrorMessage();
		} catch (AuthenticationException a) {
			setAuthenticationErrorMessage();
		} finally {
			loginButton.setEnabled(true);
			parentPage.enableTabChange(true);
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
	}

	private void setAuthenticationErrorMessage() {
		parentPage.setErrorMessage("Wrong combinaison login/password");
	}

	public void reset() {
		loginButton.setEnabled(true);
		jtfUsername.setBorder(null);
		jtfPassword.setBorder(null);
	}
}
