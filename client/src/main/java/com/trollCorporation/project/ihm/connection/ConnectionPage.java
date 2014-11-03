package com.trollCorporation.project.ihm.connection;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.trollCorporation.project.ihm.HomePage;
import com.trollCorporation.services.ConnectionToServer;

public class ConnectionPage extends JFrame {
	
	private static final long serialVersionUID = -3298364427023876210L;

	private JTextField jtfUsername;
	private JTextField jtfPassword;
	private JPanel infoPanel;
	private JLabel infoLabel;
	private JFrame singleton;
	
	public ConnectionPage() {
		singleton = this;
		createConnexionView();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void createConnexionView() {
		Box content = Box.createVerticalBox();
		
		//Info pannel
		Box infoBox = Box.createHorizontalBox();
		infoPanel = new JPanel();
		infoLabel = new JLabel();
		infoPanel.add(infoLabel);
		infoBox.add(infoPanel);
		content.add(infoBox);
		
		//Name field
		Box nameField = Box.createHorizontalBox();
		JLabel nameLabel = new JLabel("Username : ");
		nameField.add(nameLabel);
		jtfUsername = new JTextField();
		nameField.add(jtfUsername);
		content.add(nameField);
		
		//Password field
		Box pswdField = Box.createHorizontalBox();
		JLabel pswdLabel = new JLabel("Password : ");
		pswdField.add(pswdLabel);
		jtfPassword = new JPasswordField();
		pswdField.add(jtfPassword);
		content.add(pswdField);
		
		//Buttons
		Box buttonsPanel = Box.createHorizontalBox();
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new LoginActionListener());
		buttonsPanel.add(loginButton);
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new registerActionListener());
		buttonsPanel.add(registerButton);
		content.add(buttonsPanel);
		
		this.add(content);
	}
	
	private class LoginActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (ConnectionToServer.isConnectionToServerPossible()) {
				if (!jtfUsername.getText().trim().isEmpty()) {
					new HomePage(jtfUsername.getText());
				} else {
					infoPanel.setBackground(Color.RED);
					infoLabel.setText("The user field cannot be empty!");
					singleton.pack();
				}
			} else {
				infoPanel.setBackground(Color.RED);
				infoLabel.setText("Verify your internet connection!");
				singleton.pack();
			}
		}
	}
	
	private class registerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			infoPanel.setBackground(Color.ORANGE);
			infoLabel.setText("Option not yet available!");
			singleton.pack();
		}
	}
}
