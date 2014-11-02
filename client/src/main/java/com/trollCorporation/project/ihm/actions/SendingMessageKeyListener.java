package com.trollCorporation.project.ihm.actions;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import com.trollCorporation.project.controllers.ChatboxOperationsController;

public class SendingMessageKeyListener implements KeyListener {

	private JTextField jTextField;
	private ChatboxOperationsController msgController;
	
	public SendingMessageKeyListener(JTextField jTextField, ChatboxOperationsController msgController) {
		this.jTextField = jTextField;
		this.msgController = msgController;
	}

	public void keyPressed(KeyEvent e) {
		// do nothing
	}

	public void keyReleased(KeyEvent e) {
		if (KeyEvent.VK_ENTER == e.getKeyCode()) {
			String msg = jTextField.getText();
			msgController.sendMessage(msg);
			//reset
			jTextField.setText("");
		}
	}

	public void keyTyped(KeyEvent e) {
		// do nothing
	}

}
