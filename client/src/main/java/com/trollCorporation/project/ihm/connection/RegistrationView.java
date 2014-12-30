package com.trollCorporation.project.ihm.connection;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.common.exceptions.TimeoutException;
import com.trollCorporation.common.model.User;
import com.trollCorporation.common.model.enums.ErrorType;
import com.trollCorporation.common.model.operations.Operation;
import com.trollCorporation.common.model.operations.RegisterOperation;
import com.trollCorporation.project.client.Game;

public class RegistrationView {

	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
			+ "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private ConnectionPage parentPage;
	private Box view;

	private JTextField jtfUsername;
	private JTextField jtfPassword;
	private JTextField jtfConfPassword;
	private JTextField jtfEmail;
	
	private JButton registerButton;

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
		nameField.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		JLabel nameLabel = new JLabel("Username : ");
		nameField.add(nameLabel);
		jtfUsername = new JTextField();
		nameField.add(jtfUsername);
		content.add(nameField);

		// Password field
		Box pswdField = Box.createHorizontalBox();
		pswdField.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		JLabel pswdLabel = new JLabel("Password : ");
		pswdField.add(pswdLabel);
		jtfPassword = new JPasswordField();
		pswdField.add(jtfPassword);
		content.add(pswdField);

		// Confirm Password field
		Box confPswdField = Box.createHorizontalBox();
		confPswdField.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		JLabel confPswdLabel = new JLabel("Password* : ");
		confPswdField.add(confPswdLabel);
		jtfConfPassword = new JPasswordField();
		confPswdField.add(jtfConfPassword);
		content.add(confPswdField);

		// email field
		Box emailField = Box.createHorizontalBox();
		emailField.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
		JLabel emailLabel = new JLabel("Email : ");
		emailField.add(emailLabel);
		jtfEmail = new JTextField();
		emailField.add(jtfEmail);
		content.add(emailField);

		// Buttons
		Box buttonsPanel = Box.createHorizontalBox();
		buttonsPanel.setAlignmentX(Box.CENTER_ALIGNMENT);
		registerButton = new JButton("Register");
		registerButton.addActionListener((e) -> {registerAction(e);});
		buttonsPanel.add(registerButton);
		content.add(buttonsPanel);

		return content;
	}

	private void registerAction(ActionEvent e) {
		if (isFieldsValid()) {
			parentPage.enableTabChange(false);
			registerButton.setEnabled(false);
			parentPage.setInfoMessage("Trying to register, please wait!");
			new Thread(() -> {register();}).start();
		} else {
			parentPage.setErrorMessage("Some field(s) are not valid!");
		}
	}
	
	private void register() {
		Operation registerRequest  = createRegisterOperation();
		try {
			RegisterOperation registerResponse = Game.getInstance().register(registerRequest);
			if (registerResponse.isRegistered()) {
				parentPage.setSuccessMessage("Registration succeded! You may now login");
			} else {
				if (registerResponse.getErrorType().equals(ErrorType.USER_ALREADY_EXISTS)) {
					parentPage.setInfoMessage("Your username or email address is/are already used");
				} else {
					parentPage.setErrorMessage("Something went wrong with the server! Please contact an administrator");
				}
			}
		} catch (ConnectionException e1) {
			parentPage.setErrorMessage("Verify your connection!");
		} catch (TimeoutException t) {
			parentPage.setErrorMessage("The server is currently overloaded! Please retry later.");
		} finally {
			reset();
		}
	}
	
	private Operation createRegisterOperation() {
		RegisterOperation registerOperation = null;
		if (isFieldsValid()) {
			registerOperation =  new RegisterOperation();
			User user = new User(jtfUsername.getText().trim());
			user.setPassword(jtfPassword.getText());
			user.setEmail(jtfEmail.getText().trim());
			registerOperation.setUser(user);
		}
		return registerOperation;
	}

	private boolean isFieldsValid() {
		boolean isValid = true;
		if (jtfUsername.getText().trim().isEmpty()) {
			jtfUsername.setBorder(BorderFactory.createLineBorder(Color.RED));
			jtfUsername.setToolTipText("The username can't be empty!");
			isValid = false;
		}
		if (jtfPassword.getText().isEmpty() || jtfPassword.getText().length() < 6) {
			jtfPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			jtfPassword.setToolTipText("The password is not long enough! (min 6 characters)");
			isValid = false;
		}
		if (!jtfPassword.getText().equals(jtfConfPassword.getText())) {
			jtfPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			jtfConfPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
			jtfConfPassword.setToolTipText("The confirmation is not the same as the password");
			isValid = false;
		}
		if (!jtfEmail.getText().trim().matches(EMAIL_REGEX)) {
			jtfEmail.setBorder(BorderFactory.createLineBorder(Color.RED));
			jtfEmail.setToolTipText("The email is not correctly formated!");
			isValid = false;
		}
		return isValid;
	}
	
	public void reset() {
		registerButton.setEnabled(true);
		parentPage.enableTabChange(true);
		jtfUsername.setBorder(null);
		jtfPassword.setBorder(null);
		jtfConfPassword.setBorder(null);
		jtfEmail.setBorder(null);
	}
}
