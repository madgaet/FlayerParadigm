package com.trollCorporation.project.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.trollCorporation.common.exceptions.ConnectionException;
import com.trollCorporation.project.controllers.FriendsController;
import com.trollCorporation.project.controllers.FriendsControllerImpl;

public class FriendsActionsView extends JPanel {

	private static final long serialVersionUID = 2651647545566243673L;
	private JTextField jtf;
	private JComboBox<String> friendsList;
	private JLabel message;
	
	private JPanel jpNotif;
	private JLabel jlNotif;
	
	private boolean addFriends;
	private FriendsController friendsController;
	
	private static final String addMessage = "Enter the username to send a friend request!";
	private static final String remMessage = "Select a user to remove from your friend list!";
	
	public FriendsActionsView(boolean addFriends) throws ConnectionException {
		this.addFriends = addFriends;
		this.friendsController = FriendsControllerImpl.getInstance();
		this.setLayout(new BorderLayout());
		jpNotif = new JPanel();
		jlNotif = new JLabel();
		jpNotif.add(jlNotif);
		this.add(jpNotif, BorderLayout.NORTH);
		this.add(createActionView(), BorderLayout.CENTER);
	}
	
	private JPanel createActionView() {
		JPanel panel = new JPanel(new BorderLayout());
		this.message = getMessage();
		panel.add(message, BorderLayout.NORTH);
		Component comp = getActionComponent();
		panel.add(comp, BorderLayout.CENTER);
		JButton btn = getActionButton();
		panel.add(btn, BorderLayout.SOUTH);
		return panel;
	}
	
	private JLabel getMessage() {
		if (addFriends) {
			return new JLabel(addMessage);
		} else {
			return new JLabel(remMessage);
		}
	}
	
	private Component getActionComponent() {
		if (addFriends) {
			jtf = new JTextField(20);
			return jtf;
		} else {
			friendsList = new JComboBox<String>();
			List<String> friends = friendsController.getFriendsList();
			for (String friendName : friends) {
				friendsList.addItem(friendName);
			}
			return friendsList;
		}
	}
	
	private JButton getActionButton() {
		JButton btn;
		if (addFriends) {
			btn = new JButton("Add");
			btn.addActionListener(e->{addFriend();});
		} else {
			btn = new JButton("Remove");
			btn.addActionListener(e->{removeFriend();});
		}
		return btn;
	}
	
	private void addFriend() {
		if (jtf.getText().trim().isEmpty()) {
			setErrorMessage("field can't be empty");
		} else {
			try {
				friendsController.sendAddFriendsRequest(jtf.getText());
				setSuccessMessage("A request has been sent to " + jtf.getText());
				jtf.setText("");
			} catch (ConnectionException c) {
				setErrorMessage("The request wasn't sent due to a connection problem.");
			}
		}
	}
	
	private void removeFriend() {
		try {
			String username = (String)friendsList.getSelectedItem();
			if (username == null || username.isEmpty()) {
				return;
			}
			int confirmation = JOptionPane.showConfirmDialog(this,"Do you confirm you want to remove "
					+ username + " from your friends!", "Confirmation", JOptionPane.YES_NO_OPTION);
			if (confirmation == JOptionPane.YES_OPTION) {
				friendsController.sendRemoveFriendsRequest(username);
				friendsList.removeItem(username);
				setSuccessMessage(username + " has been removed from your friends");
			}
		} catch (ConnectionException c) {
			setErrorMessage("The request wasn't sent due to a connection problem.");
		}
	}
	
	private void setErrorMessage(final String msg) {
		jlNotif.setText(msg);
		jpNotif.setBackground(new Color(255,0,0,155));
	}
	
	private void setSuccessMessage(final String msg) {
		jlNotif.setText(msg);
		jpNotif.setBackground(new Color(0,255,0,155));
	}
}
