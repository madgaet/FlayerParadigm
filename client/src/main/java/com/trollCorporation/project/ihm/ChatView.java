package com.trollCorporation.project.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.trollCorporation.common.utils.Observer;
import com.trollCorporation.project.controllers.ChatboxOperationsController;
import com.trollCorporation.project.ihm.actions.SendingMessageKeyListener;

public class ChatView extends JPanel implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7679035020047514901L;

	private Box chatBox;
	private Box userList;
	private JPanel textBox;
	private JTextField textField;
	private ChatboxOperationsController chatboxController;
	private Dimension dimension;
	
	public ChatView(ChatboxOperationsController msgController, Dimension viewDimension) {
		this.dimension = viewDimension;
		this.setPreferredSize(dimension);
		this.chatboxController = msgController;
		msgController.addObserver(this);
		createChatView();
		this.setBorder(new LineBorder(Color.PINK));
		this.setBackground(Color.GRAY);
	}
	
	public void createChatView() {
		Box chatView = Box.createHorizontalBox();
		//left part of chat view
		Box chat = Box.createVerticalBox();
		chat.add(createChatBox());
		chat.add(createTextBox());
		chatView.add(chat);
		//right part of chat view
		chatView.add(createUserList());
		
		this.add(chatView);
	}
	
	private JScrollPane createChatBox() {
		chatBox = Box.createVerticalBox();
		JScrollPane jspChatBox = new JScrollPane(chatBox);
		Border lineBorder =	BorderFactory.createLineBorder(Color.BLUE);
		String titleChatBox = "ChatBox";
		jspChatBox.setBorder(BorderFactory.createTitledBorder(lineBorder, titleChatBox));
		jspChatBox.setPreferredSize(new Dimension((dimension.width/3*2), dimension.height/6*5));
		return jspChatBox;
	}
	
	private JScrollPane createUserList() {
		userList = Box.createVerticalBox();
		JScrollPane jspUserList = new JScrollPane(userList);
		Border lineBorder = BorderFactory.createLineBorder(Color.GRAY, 2);
		String titleUserList = "Friends";
		jspUserList.setBorder(BorderFactory.createTitledBorder(lineBorder, titleUserList));
		jspUserList.setPreferredSize(new Dimension(dimension.width/48*15, dimension.height));
		return jspUserList;
	}
	
	private JPanel createTextBox() {
		textBox = new JPanel();
		textField = new JTextField(dimension.width/18);
		textField.addKeyListener(new SendingMessageKeyListener(textField, chatboxController));
		textBox.add(textField);
		textBox.setPreferredSize(new Dimension(dimension.width/3*2, dimension.height/6));
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
