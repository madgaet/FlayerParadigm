package com.trollCorporation.project.ihm.actions;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.JTextComponent;

import com.trollCorporation.common.utils.MessageFormatter;
import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.ihm.objects.ChatUser;

public class SendingMessageKeyListener implements KeyListener {

	private ChatUser chatUser;
	private JTextComponent jTextField;
	private ChatboxOperationsController msgController;
	private List<String> receivers = new ArrayList<String>();
	private String sender;
	
	public SendingMessageKeyListener(final ChatUser chatUser, final JTextComponent jTextField,
			final ChatboxOperationsController msgController, final List<String> receivers, final String sender) {
		this.chatUser = chatUser;
		this.jTextField = jTextField;
		this.msgController = msgController;
		if (receivers != null) {
			this.receivers.addAll(receivers);
		}
		if (sender == null) {
			this.sender = "Server";
		} else {
			this.sender = sender;
		}
	}

	public void keyPressed(KeyEvent e) {
		// do nothing
	}

	public void keyReleased(KeyEvent e) {
		if (KeyEvent.VK_ENTER == e.getKeyCode()) {
			String msg = jTextField.getText();
			if (!msg.isEmpty()) {
				if (receivers.isEmpty()) {
					msgController.sendMessage(sender, msg);
				} else {
					String formatedMsg = MessageFormatter.formatSentMessage(msg, sender);
					chatUser.addTextMessage(formatedMsg, false);
					msgController.sendMessage(sender, msg, receivers);
				}
			}
			//reset
			jTextField.setText("");
		}
	}

	public void keyTyped(KeyEvent e) {
		// do nothing
	}

}
