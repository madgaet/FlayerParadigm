package com.trollCorporation.project.ihm.connection;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.trollCorporation.project.client.Game;
import com.trollCorporation.project.exceptions.AuthenticationException;
import com.trollCorporation.project.exceptions.ConnectionException;

public class ConnectionView {

	private ConnectionPage parentPage;
	private Box view;

	private JTextField jtfUsername;
	private JTextField jtfPassword;

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
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new LoginActionListener());
		buttonsPanel.add(loginButton);
		content.add(buttonsPanel);

		return content;
	}

	private class LoginActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (!jtfUsername.getText().trim().isEmpty()
						& !jtfPassword.getText().isEmpty()) {
					Game.getInstance().connect(jtfUsername.getText(),
							jtfPassword.getText());
				} else {
					setUserErrorMessage();
				}
			} catch (ConnectionException e) {
				setInternetErrorMessage();
			} catch (AuthenticationException a) {
				setAuthenticationErrorMessage();
			}
		}
	}

	private void setInternetErrorMessage() {
		parentPage.setErrorMessage("Verify your internet connection!");
	}

	private void setUserErrorMessage() {
		if (jtfUsername.getText().trim().isEmpty()) {
			jtfUsername.setBorder(BorderFactory.createLineBorder(Color.RED));
		}
		if (jtfPassword.getText().isEmpty()) {
			jtfPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
		}
		parentPage.setErrorMessage("Some field(s) cannot be empty!");
	}

	private void setAuthenticationErrorMessage() {
		parentPage.setErrorMessage("Wrong combinaison login/password");
	}

	public void reset() {
		jtfUsername.setBorder(null);
		jtfPassword.setBorder(null);
	}
}
