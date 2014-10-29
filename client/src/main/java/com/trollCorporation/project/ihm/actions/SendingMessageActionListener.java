package com.trollCorporation.project.ihm.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import com.trollCorporation.project.controllers.ChatboxOperationsController;

public class SendingMessageActionListener implements ActionListener {

	private JTextField jTextField;
	private ChatboxOperationsController msgController;
	
	public SendingMessageActionListener(JTextField jTextField, ChatboxOperationsController msgController) {
		this.jTextField = jTextField;
		this.msgController = msgController;
	}
	
	public void actionPerformed(ActionEvent e) {
		//TODO sent message and reset textField
		String msg = jTextField.getText();
		msgController.sendMessage(msg);
		//reset
		jTextField.setText("");
	}

}
