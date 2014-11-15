package com.trollCorporation.project.ihm.connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegistrationView {

	private ConnectionPage parentPage;
	private Box view;

	private JTextField jtfUsername;
	private JTextField jtfPassword;
	private JTextField jtfConfPassword;
	private JTextField jtfEmail;

	public RegistrationView(ConnectionPage page) {
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
		JLabel nameLabel = new JLabel("Username : ");
		nameField.add(nameLabel);
		jtfUsername = new JTextField();
		nameField.add(jtfUsername);
		content.add(nameField);

		// Password field
		Box pswdField = Box.createHorizontalBox();
		JLabel pswdLabel = new JLabel("Password : ");
		pswdField.add(pswdLabel);
		jtfPassword = new JPasswordField();
		pswdField.add(jtfPassword);
		content.add(pswdField);

		// Confirm Password field
		Box confPswdField = Box.createHorizontalBox();
		JLabel confPswdLabel = new JLabel("Password* : ");
		confPswdField.add(confPswdLabel);
		jtfConfPassword = new JPasswordField();
		confPswdField.add(jtfConfPassword);
		content.add(confPswdField);

		// email field
		Box emailField = Box.createHorizontalBox();
		JLabel emailLabel = new JLabel("Email : ");
		emailField.add(emailLabel);
		jtfEmail = new JTextField();
		emailField.add(jtfEmail);
		content.add(emailField);

		// Buttons
		Box buttonsPanel = Box.createHorizontalBox();
		buttonsPanel.setAlignmentX(Box.CENTER_ALIGNMENT);
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new RegisterActionListener());
		buttonsPanel.add(registerButton);
		content.add(buttonsPanel);

		return content;
	}

	private class RegisterActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			setUnavailableOptionMessage();
		}
	}

	private void setUnavailableOptionMessage() {
		parentPage.setInfoMessage("Option not yet available!");
	}
	
	public void reset() {
		jtfUsername.setBorder(null);
		jtfPassword.setBorder(null);
		jtfConfPassword.setBorder(null);
		jtfEmail.setBorder(null);
	}
}
