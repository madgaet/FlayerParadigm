package com.trollCorporation.project.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.trollCorporation.common.utils.Observer;
import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.ihm.actions.SendingMessageActionListener;

public class ChatView extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7679035020047514901L;

	private JPanel chatBox;
	private JPanel userList;
	private JPanel textBox;
	private JTextField textField;
	private ChatboxOperationsController chatboxController;
	
	public ChatView(ChatboxOperationsController msgController) {
		this.chatboxController = msgController;
		msgController.addObserver(this);
		this.setLayout(new BorderLayout());
		createChatView();
	}
	
	public void createChatView() {
		this.add(createChatBox(), BorderLayout.CENTER);
		this.add(createUserList(), BorderLayout.EAST);
		this.add(createTextBox(), BorderLayout.SOUTH);
	}
	
	private JScrollPane createChatBox() {
		chatBox = new JPanel(new GridLayout(0, 1));
		String titleChatBox = "ChatBox";
		Border lineBorder =	BorderFactory.createLineBorder(Color.BLUE);
		chatBox.setBorder(BorderFactory.createTitledBorder(lineBorder, titleChatBox));
		return new JScrollPane(chatBox);
	}
	
	private JPanel createUserList() {
		userList = new JPanel(new GridLayout(0, 1));
		userList.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		return userList;
	}
	
	private JPanel createTextBox() {
		textBox = new JPanel();
		double frameWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		textField = new JTextField((int)Math.round(frameWidth/12));
		textBox.add(textField);
		JButton jbtn = new JButton("Send");
		jbtn.addActionListener(
				new SendingMessageActionListener(textField, chatboxController));
		textBox.add(jbtn);
		return textBox;
	}
	
	public void update() {
		updateMessages();
		updateUserList();
	}
	
	private void updateMessages() {
		if (chatboxController.isLastMessageChanged()) {
			chatBox.add(new JLabel(chatboxController.getMessage()));
			chatBox.revalidate();
		}
	}
	
	private void updateUserList() {
		if (chatboxController.isActiveUsersChanged()) {
			userList.removeAll();
			List<String> users = chatboxController.getActiveUsers();
			for (String user : users) {
				userList.add(new JLabel(user));
			}
			userList.revalidate();
		}
	}
}
